package net.bunten.enderscape.client.hud;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.item.MagniaAttractorItem;
import net.bunten.enderscape.item.MirrorContext;
import net.bunten.enderscape.item.MirrorItem;
import net.bunten.enderscape.item.NebuliteToolContext;
import net.bunten.enderscape.item.NebuliteToolItem;
import net.bunten.enderscape.util.NineSliceBlitUtil;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class NebuliteToolHud extends HudElement {

    public NebuliteToolHud() {
        super(Enderscape.id("nebulite_tool"), RenderPhase.AFTER_HUD);
    }

    private static final ResourceLocation[] EMPTY_SEGMENTS = {
            Enderscape.id("nebulite_tool/hud/empty_segments/start"),
            Enderscape.id("nebulite_tool/hud/empty_segments/loop"),
            Enderscape.id("nebulite_tool/hud/empty_segments/end")
    };

    private static final ResourceLocation[] FUELED_SEGMENTS = {
            Enderscape.id("nebulite_tool/hud/fueled_segments/start"),
            Enderscape.id("nebulite_tool/hud/fueled_segments/loop"),
            Enderscape.id("nebulite_tool/hud/fueled_segments/end")
    };

    private static final ResourceLocation[] INVALID_OVERLAY_SEGMENTS = {
            Enderscape.id("nebulite_tool/hud/invalid_overlay_segments/start"),
            Enderscape.id("nebulite_tool/hud/invalid_overlay_segments/loop"),
            Enderscape.id("nebulite_tool/hud/invalid_overlay_segments/end")
    };

    private static final ResourceLocation TRANSDIMENSIONAL_OUTLINE = Enderscape.id("nebulite_tool/hud/transdimensional_outline");
    private static final ResourceLocation COST_OVERLAY_SEGMENT = Enderscape.id("nebulite_tool/hud/cost_overlay_segment");

    private float heightOffset;
    private float totalAlpha;
    private float costAlpha;
    private int costDisplayTicks;
    private float invalidAlpha;
    private int invalidDisplayTicks;
    private float transdimensionalAlpha;
    private int transdimensionalDisplayTicks;
    private int costOverlayPosition = -1;
    private int fuel;
    private int maxFuel;
    private int cost;

    @Override
    public void render(GuiGraphics graphics, DeltaTracker delta) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (!config.nebuliteToolHudEnabled || player == null || minecraft.options.hideGui || totalAlpha <= 0.01F ||
                !minecraft.options.getCameraType().isFirstPerson() || player.isSpectator()) {
            return;
        }

        graphics.pose().pushPose();
        graphics.pose().translate(0.0F, heightOffset, 0.0F);

        float opacity = totalAlpha * config.nebuliteToolHudOpacity / 100.0F;
        int x = graphics.guiWidth() / 2 - (((11 * maxFuel) + 1) / 2);
        int y = graphics.guiHeight() / 2 - 13 - config.nebuliteToolHudOffset;

        renderFuelBar(graphics, x, y, opacity);
        renderTransdimensionalOutline(graphics, x, y, opacity);
        renderInvalidOverlay(graphics, x, y, opacity);
        renderCostOverlay(graphics, y, opacity);

        graphics.pose().popPose();
    }

    private void renderFuelBar(GuiGraphics graphics, int x, int y, float opacity) {
        int lastFueled = -1;
        int rx = x;

        for (int i = 0; i < maxFuel; i++) {
            boolean isFueled = i < fuel;
            if (i > 0) rx += 11;
            if (isFueled) lastFueled = rx;

            ResourceLocation[] segments = isFueled ? FUELED_SEGMENTS : EMPTY_SEGMENTS;
            int index = i == 0 ? 0 : i == maxFuel - 1 ? 2 : 1;
            int width = i == maxFuel - 1 ? 12 : 11;
            TextureAtlasSprite sprite = Minecraft.getInstance().getGuiSprites().getSprite(segments[index]);
            graphics.blit(rx, y, 0, width, 5, sprite, 1.0F, 1.0F, 1.0F, opacity);
        }

        costOverlayPosition = lastFueled;
    }

    private void renderTransdimensionalOutline(GuiGraphics graphics, int x, int y, float opacity) {
        NineSliceBlitUtil nineSliceBlit = new NineSliceBlitUtil(Minecraft.getInstance().getGuiSprites(), graphics, transdimensionalAlpha * opacity);
        nineSliceBlit.blitSprite(TRANSDIMENSIONAL_OUTLINE, x - 6, y - 6, 0, maxFuel * 11 + 13, 17);
    }

    private void renderInvalidOverlay(GuiGraphics graphics, int x, int y, float opacity) {
        int rx = x;
        for (int i = 0; i < maxFuel; i++) {
            if (i > 0) rx += 11;
            int index = i == 0 ? 0 : i == maxFuel - 1 ? 2 : 1;
            int width = i == maxFuel - 1 ? 12 : 11;
            TextureAtlasSprite sprite = Minecraft.getInstance().getGuiSprites().getSprite(INVALID_OVERLAY_SEGMENTS[index]);
            graphics.blit(rx, y, 0, width, 5, sprite, 1.0F, 1.0F, 1.0F, invalidAlpha * opacity);
        }
    }

    private void renderCostOverlay(GuiGraphics graphics, int y, float opacity) {
        if (costAlpha > 0.0F && costOverlayPosition >= 0 && fuel >= cost) {
            TextureAtlasSprite sprite = Minecraft.getInstance().getGuiSprites().getSprite(COST_OVERLAY_SEGMENT);
            for (int i = 0; i < cost; i++) {
                graphics.blit(costOverlayPosition - i * 11, y, 0, 11, 5, sprite, 1.0F, 1.0F, 1.0F, costAlpha * opacity);
            }
        }
    }

    @Override
    public void tick() {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        ItemStack mainHand = player.getMainHandItem();
        ItemStack stack = NebuliteToolItem.is(mainHand) ? mainHand : player.getOffhandItem();
        NebuliteToolContext context = new NebuliteToolContext(stack, player.level(), player);

        boolean displayUI = NebuliteToolItem.is(stack) && !player.isSpectator() && player.getUseItem().isEmpty();
        boolean displayOutline = false;
        boolean displayTransdimensional = false;
        boolean enoughFuel = false;

        if (displayUI) {
            enoughFuel = NebuliteToolItem.fuelExceedsCost(context);
            displayOutline = !enoughFuel;

            if (MirrorItem.is(stack)) {
                MirrorContext mirror = MirrorContext.of(context);
                boolean linked = MirrorItem.isLinked(stack);
                boolean sameDimension = linked && MirrorItem.isSameDimension(mirror, mirror.linkedDimension());
                boolean hasTransdimensional = linked && MirrorItem.hasTransdimensional(mirror);
                boolean validDestination = sameDimension || (hasTransdimensional && MirrorItem.isSameCoordinateScale(mirror));
                displayOutline = !(linked && enoughFuel && validDestination);
                displayTransdimensional = hasTransdimensional && !sameDimension && !displayOutline;
            } else if (MagniaAttractorItem.is(stack)) {
                displayUI = enoughFuel && MagniaAttractorItem.isEnabled(stack);
                displayOutline = !enoughFuel;
            }
        }

        if (displayUI) {
            fuel = NebuliteToolItem.currentFuel(stack);
            maxFuel = NebuliteToolItem.maxFuel(stack);
            cost = NebuliteToolItem.of(stack).fuelCost(context);
            totalAlpha = Mth.lerp(0.5F, totalAlpha, 1.0F);
            heightOffset = Mth.lerp(0.5F, heightOffset, -3.0F);
        } else {
            totalAlpha = Mth.lerp(0.5F, totalAlpha, 0.0F);
            heightOffset = Mth.lerp(0.5F, heightOffset, 0.0F);
        }

        invalidAlpha = Mth.lerp(0.35F, invalidAlpha, displayUI && displayOutline
                ? Mth.clamp(Mth.sin(invalidDisplayTicks * 0.4F) * 0.5F + 0.5F, 0.75F, 1.0F) : 0.0F);
        costAlpha = Mth.lerp(0.35F, costAlpha, displayUI && !displayOutline
                ? Mth.clamp(Mth.sin(costDisplayTicks * 0.25F) * 0.2F + 0.5F, 0.75F, 1.0F) : 0.0F);
        transdimensionalAlpha = Mth.lerp(0.35F, transdimensionalAlpha, displayUI && displayTransdimensional
                ? Mth.clamp(Mth.sin(transdimensionalDisplayTicks * 0.1F) * 0.5F + 0.5F, 0.4F, 1.0F) : 0.0F);

        invalidDisplayTicks = invalidAlpha > 0.0F ? invalidDisplayTicks + 1 : 0;
        costDisplayTicks = costAlpha > 0.0F ? costDisplayTicks + 1 : 0;
        transdimensionalDisplayTicks = transdimensionalAlpha > 0.0F ? transdimensionalDisplayTicks + 1 : 0;
    }
}
