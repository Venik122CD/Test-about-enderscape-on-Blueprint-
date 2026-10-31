package net.bunten.enderscape.criteria;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public class StunAttackCriterion extends SimpleCriterionTrigger<StunAttackCriterion.Conditions> {
    public void trigger(ServerPlayer player, boolean backstab) {
        trigger(player, instance -> instance.matches(backstab));
    }

    @Override
    public Codec<Conditions> codec() {
        return Conditions.CODEC;
    }

    public record Conditions(Optional<ContextAwarePredicate> player, Optional<Boolean> backstab) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(Conditions::player),
                Codec.BOOL.optionalFieldOf("backstab").forGetter(Conditions::backstab)
        ).apply(instance, Conditions::new));

        public boolean matches(boolean wasBackstab) {
            return backstab.isEmpty() || backstab.get() == wasBackstab;
        }
    }
}
