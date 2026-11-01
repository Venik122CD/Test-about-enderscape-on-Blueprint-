package net.bunten.enderscape.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.item.NebuliteToolContext;
import net.bunten.enderscape.item.NebuliteToolItem;
import net.bunten.enderscape.registry.EnderscapeAttributes;
import net.bunten.enderscape.registry.EnderscapeCriteria;
import net.bunten.enderscape.registry.EnderscapeDataComponents;
import net.bunten.enderscape.registry.EnderscapeItemSounds;
import net.bunten.enderscape.registry.EnderscapeMobEffects;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.util.List;

                                                                                                                             
public record StunAttack(
        float backstabEffectDurationMultiplier,
        Vec2 range,
        Holder<SoundEvent> normalSound,
        MobEffectInstance effect,
        ResourceLocation particle
) {
    public static final float DEFAULT_BACKSTAB_EFFECT_DURATION_MULTIPLIER = 4.0F;
    public static final Vec2 DEFAULT_RANGE = new Vec2(4.0F, 0.5F);
    public static final Holder<SoundEvent> DEFAULT_NORMAL_SOUND = EnderscapeItemSounds.DAGGER_STUN;
    public static final ResourceLocation DEFAULT_PARTICLE = Enderscape.id("void_poof");
                                                                                                             
    public static final int COOLDOWN_TICKS = 7 * 20;

    private static final Codec<Vec2> VEC2_CODEC = Codec.FLOAT.listOf().comapFlatMap(
            list -> list.size() == 2 ? DataResult.success(new Vec2(list.get(0), list.get(1))) : DataResult.error(() -> "Expected a list of two floats"),
            vec -> List.of(vec.x, vec.y)
    );

    private static final StreamCodec<RegistryFriendlyByteBuf, Vec2> VEC2_STREAM = StreamCodec.composite(
            ByteBufCodecs.FLOAT, vec -> vec.x,
            ByteBufCodecs.FLOAT, vec -> vec.y,
            Vec2::new
    );

    public static final Codec<StunAttack> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.optionalFieldOf("backstab_effect_duration_multiplier", DEFAULT_BACKSTAB_EFFECT_DURATION_MULTIPLIER).forGetter(StunAttack::backstabEffectDurationMultiplier),
            VEC2_CODEC.optionalFieldOf("range", DEFAULT_RANGE).forGetter(StunAttack::range),
            SoundEvent.CODEC.optionalFieldOf("normal_sound", DEFAULT_NORMAL_SOUND).forGetter(StunAttack::normalSound),
            MobEffectInstance.CODEC.optionalFieldOf("effect").forGetter(attack -> java.util.Optional.ofNullable(attack.effect)),
            ResourceLocation.CODEC.optionalFieldOf("particle", DEFAULT_PARTICLE).forGetter(StunAttack::particle)
    ).apply(instance, (multiplier, range, sound, effect, particle) -> new StunAttack(multiplier, range, sound, effect.orElse(null), particle)));

    public static final StreamCodec<RegistryFriendlyByteBuf, StunAttack> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, StunAttack::backstabEffectDurationMultiplier,
            VEC2_STREAM, StunAttack::range,
            SoundEvent.STREAM_CODEC, StunAttack::normalSound,
            MobEffectInstance.STREAM_CODEC, StunAttack::effect,
            ResourceLocation.STREAM_CODEC, StunAttack::particle,
            StunAttack::new
    );

    public static final StunAttack DEFAULT = new StunAttack(
            DEFAULT_BACKSTAB_EFFECT_DURATION_MULTIPLIER,
            DEFAULT_RANGE,
            DEFAULT_NORMAL_SOUND,
            null,
            DEFAULT_PARTICLE
    );

    @Override
    public MobEffectInstance effect() {
        return effect != null ? effect : new MobEffectInstance(EnderscapeMobEffects.STUNNED, 20);
    }

    public static boolean is(ItemStack stack) {
        return stack.has(EnderscapeDataComponents.STUN_ATTACK);
    }

    public static StunAttack get(ItemStack stack) {
        return stack.get(EnderscapeDataComponents.STUN_ATTACK);
    }

                                                                                                                        
    public ParticleOptions particleOptions() {
        ParticleType<?> type = BuiltInRegistries.PARTICLE_TYPE.get(particle);
        return type instanceof SimpleParticleType simple ? simple : net.minecraft.core.particles.ParticleTypes.POOF;
    }

    public static boolean apply(ServerLevel level, LivingEntity attacker, LivingEntity victim, ItemStack stack) {
        NebuliteToolContext context = new NebuliteToolContext(stack, level, attacker);
        StunAttack attack = get(stack);

        if (canStun(attacker, context)) {
            boolean backstab = stack.has(EnderscapeDataComponents.BACKSTAB_ANGLE) && EnderscapeAttributes.isBackstab(stack, attacker.position(), victim);
            List<Entity> affectedEntities = List.of(victim);

            doSingularStun(context, victim, attack, backstab);

            if (attacker instanceof ServerPlayer player) {
                EnderscapeCriteria.STUN_ATTACK.trigger(player, stack, victim, affectedEntities, backstab);
                player.getCooldowns().addCooldown(stack.getItem(), COOLDOWN_TICKS);
            }

            ThresholdCounter.useFuel(level, attacker, stack);
            return true;
        }

        return false;
    }

    private static boolean canStun(LivingEntity attacker, NebuliteToolContext context) {
        ItemStack stack = context.stack();
        boolean notOnCooldown = !(attacker instanceof Player player) || !player.getCooldowns().isOnCooldown(stack.getItem());
        boolean hasFuel = !NebuliteToolItem.is(stack) || NebuliteToolItem.fuelExceedsCost(context);
        return notOnCooldown && hasFuel;
    }

    private static void doSingularStun(NebuliteToolContext context, LivingEntity victim, StunAttack attack, boolean backstab) {
        LivingEntity attacker = context.user();
        context.level().playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(), attack.normalSound().value(), attacker.getSoundSource(), 1.0F, 1.0F);
        stun(context, victim, attack, backstab);
    }

    private static void stun(NebuliteToolContext context, LivingEntity victim, StunAttack attack, boolean backstab) {
        Vec3 pos = victim.position();
        MobEffectInstance effect = attack.effect();

        if (effect != null) {
            float multiplier = backstab ? attack.backstabEffectDurationMultiplier() : 1;
            int duration = (int) (effect.getDuration() * multiplier);
            victim.addEffect(new MobEffectInstance(effect.getEffect(), duration, effect.getAmplifier(), effect.isAmbient(), effect.isVisible(), effect.showIcon()), context.user());
        }

        if (context.level() instanceof ServerLevel server) {
            server.sendParticles(attack.particleOptions(), pos.x, pos.y + 0.5, pos.z, 15, 1, 1, 1, 0.1);
        }
    }
}
