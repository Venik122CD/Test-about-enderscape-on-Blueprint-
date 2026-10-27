package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.entity.DashJumpUser;
import net.bunten.enderscape.entity.DashJumpUserData;
import net.bunten.enderscape.entity.EndTrialSpawnable;
import net.bunten.enderscape.entity.EndTrialSpawnableData;
import net.bunten.enderscape.entity.magnia.MagniaMoveable;
import net.bunten.enderscape.entity.magnia.MagniaMovingData;
import net.bunten.enderscape.network.ClientboundDashJumpDataPayload;
import net.bunten.enderscape.network.ClientboundEndTrialSpawnableDataPayload;
import net.bunten.enderscape.network.ClientboundMagniaDataPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

@EventBusSubscriber(modid = Enderscape.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class EnderscapeDataAttachments {
    public static final Supplier<AttachmentType<MagniaMovingData>> MAGNIA_MOVING_DATA = register("magnia_moving_data", () -> AttachmentType
            .builder(() -> new MagniaMovingData(0))
            .serialize(MagniaMovingData.CODEC)
            .build()
    );
    public static final Supplier<AttachmentType<DashJumpUserData>> DASH_JUMP_USER_DATA = register("dash_jump_user_data", () -> AttachmentType
            .builder(() -> new DashJumpUserData(false, 0))
            .serialize(DashJumpUserData.CODEC)
            .build()
    );
    public static final Supplier<AttachmentType<EndTrialSpawnableData>> END_TRIAL_SPAWNABLE_DATA = register("end_trial_spawnable_data", () -> AttachmentType
            .builder(() -> new EndTrialSpawnableData(false))
            .serialize(EndTrialSpawnableData.CODEC)
            .build()
    );
    
    @SubscribeEvent
    public static void onEntityStartTracking(PlayerEvent.StartTracking event) {
        var player = event.getEntity();
        var target = event.getTarget();
        if (!player.level().isClientSide()) {
            var serverPlayer = (ServerPlayer) player;
            if (target instanceof DashJumpUser) {
                var data = target.getData(DASH_JUMP_USER_DATA.get());
                serverPlayer.connection.send(new ClientboundDashJumpDataPayload(target.getId(), data));
            }
            if (target instanceof MagniaMoveable) {
                var data = target.getData(MAGNIA_MOVING_DATA.get());
                serverPlayer.connection.send(new ClientboundMagniaDataPayload(target.getId(), data));
            }
            if (target instanceof EndTrialSpawnable) {
                var data = target.getData(END_TRIAL_SPAWNABLE_DATA.get());
                serverPlayer.connection.send(new ClientboundEndTrialSpawnableDataPayload(target.getId(), data));
            }
        }
    }
    
    private static <T> Supplier<AttachmentType<T>> register(String name, Supplier<AttachmentType<T>> entry) {
        return RegistryHelper.register(NeoForgeRegistries.ATTACHMENT_TYPES, Enderscape.id(name), entry);
    }
}
