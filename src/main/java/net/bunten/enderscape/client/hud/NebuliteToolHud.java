package net.bunten.enderscape.client.hud;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.item.MagniaAttractorItem;
import net.bunten.enderscape.item.MirrorContext;
import net.bunten.enderscape.item.MirrorItem;
import net.bunten.enderscape.item.NebuliteToolContext;
import net.bunten.enderscape.item.NebuliteToolItem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class NebuliteToolHud extends HudElement {

    private static final ResourceLocation[] EMPTY_SEGMENTS = {
            Enderscape.id("nebulite_tool/tooltip/empty_segments/start"),
            Enderscape.id("nebulite_tool/tooltip/empty_segments/loop"),
            Enderscape.id("nebulite_tool/tooltip/empty_segments/end")
    };
    private static final ResourceLocation FUELED_SEGMENT = Enderscape.id("nebulite_tool/tooltip/fueled_segment");

    private float heightOffset;
    private float totalAlpha;
    private float costAlpha;
    private int costDisplayTicks;
    private float invalidAlpha;
    private int invalidDisplayTicks;
    private float transdimensionalAlpha;
    private int transdimensionalDisplayTicks;
    private int fuel;
    private int maxFuel;
    private int cost;
    private boolean invalid;
    private boolean transdimensional;
    private int costOverlayPosition = -1;

    public NebuliteToolHud() {
        super(Enderscape.id("nebulite_tool"), RenderPhase.AFTER_HUD);
    }

    @Override
    public void render(GuiGraphics graphics, DeltaTracker delta) {
        Minecraft minecraft = Minecraft.getInstance();
        if (!config.nebuliteToolHudEnabled || minecraft.player == null || minecraft.options.hideGui ||
                totalAlpha <= 0.01F || !minecraft.options.getCameraType().isFirstPerson() || minecraft.player.isSpectator()) {
            return;
        }

        graphics.pose().pushPose();
        graphics.pose().translate(0.0F, heightOffset, 0.0F);

        float opacity = totalAlpha * config.nebuliteToolHudOpacity / 100.0F;
        int x = graphics.guiWidth() / 2 - (((11 * maxFuel) + 1) / 2);
        int y = graphics.guiHeight() / 2 - 13 - config.nebuliteToolHudOffset;

        renderFuelBar(graphics, x, y);
        renderTransdimensionalOutline(graphics, x, y, opacity);
        renderInvalidOverlay(graphics, x, y, opacity);
        renderCostOverlay(graphics, y, opacity);

        graphics.pose().popPose();
    }

    private void renderFuelBar(GuiGraphics graphics, int x, int y) {
        int rx = x;
        int lastFueled = -1;

        for (int i = 0; i < maxFuel; i++) {
            boolean fueled = i < fuel;
            if (i > 0) rx += 11;
            if (fueled) lastFueled = rx;

            int index = i == 0 ? 0 : i == maxFuel - 1 ? 2 : 1;
            int width = i == maxFuel - 1 ? 12 : 11;
            graphics.blitSprite(EMPTY_SEGMENTS[index], rx, y, width, 5);

            if (fueled) {
                graphics.blitSprite(FUELED_SEGMENT, rx + 1, y + 1, width - 2, 3);
            }
        }

        costOverlayPosition = lastFueled;
    }

    private void renderTransdimensionalOutline(GuiGraphics graphics, int x, int y, float opacity) {
        if (transdimensionalAlpha <= 0.01F) return;
        int alpha = Mth.clamp(Mth.floor(transdimensionalAlpha * opacity * 150.0F), 0, 255);
        int color = (alpha << 24) | 0x0047D7FF;
        int width = maxFuel * 11 + 1;
        graphics.fill(x - 3, y - 3, x + width + 3, y - 2, color);
        graphics.fill(x - 3, y + 5, x + width + 3, y + 6, color);
        graphics.fill(x - 3, y - 2, x - 2, y + 5, color);
        graphics.fill(x + width + 2, y - 2, x + width + 3, y + 5, color);
    }

    private void renderInvalidOverlay(GuiGraphics graphics, int x, int y, float opacity) {
        if (invalidAlpha <= 0.01F) return;
        int alpha = Mth.clamp(Mth.floor(invalidAlpha * opacity * 105.0F), 0, 255);
        int color = (alpha << 24) | 0x00D62F3D;
        graphics.fill(x, y, x + maxFuel * 11 + 1, y + 5, color);
    }

    private void renderCostOverlay(GuiGraphics graphics, int y, float opacity) {
        if (costAlpha <= 0.01F || costOverlayPosition < 0 || fuel < cost) return;
        int alpha = Mth.clamp(Mth.floor(costAlpha * opacity * 180.0F), 0, 255);
        int color = (alpha << 24) | 0x00E6B5FF;
        for (int i = 0; i < cost; i++) {
            int x = costOverlayPosition - i * 11;
            graphics.fill(x + 1, y + 1, x + 10, y + 4, color);
        }
    }

    @Override
    public void tick() {
        Player player = Minecraft.getInstance().player;
        if (player == null || player.isSpectator() || !player.getUseItem().isEmpty()) {
            updateFade();
            updateAnimations();
            return;
        }

        ItemStack mainHand = player.getMainHandItem();
        ItemStack stack = isSupportedTool(mainHand) ? mainHand : player.getOffhandItem();
        if (!isSupportedTool(stack)) {
            updateFade();
            updateAnimations();
            return;
        }

        NebuliteToolContext context = new NebuliteToolContext(stack, player.level(), player);
        invalid = false;
        transdimensional = false;

        if (MirrorItem.is(stack)) {
            MirrorContext mirror = MirrorContext.of(context);
            boolean linked = MirrorItem.isLinked(stack);
            boolean sameDimension = linked && MirrorItem.isSameDimension(mirror, mirror.linkedDimension());
            boolean transdimensionalAllowed = linked && MirrorItem.hasTransdimensional(mirror) &&
                    MirrorItem.isSameCoordinateScale(mirror);
            boolean enoughFuel = linked && NebuliteToolItem.fuelExceedsCost(context);

            invalid = !linked || !enoughFuel || !(sameDimension || transdimensionalAllowed);
            transdimensional = !invalid && !sameDimension && transdimensionalAllowed;
            updateDisplay(stack, context, true);
        } else {
            boolean active = MagniaAttractorItem.isEnabled(stack) && NebuliteToolItem.fuelExceedsCost(context);
            updateDisplay(stack, context, active);
        }

        updateAnimations();
    }

    private void updateDisplay(ItemStack stack, NebuliteToolContext context, boolean show) {
        if (!show) {
            updateFade();
            return;
        }

        fuel = NebuliteToolItem.currentFuel(stack);
        maxFuel = NebuliteToolItem.maxFuel(stack);
        try {
            cost = NebuliteToolItem.of(stack).fuelCost(context);
        } catch (RuntimeException exception) {
            cost = maxFuel;
            invalid = true;
        }

        totalAlpha = Mth.lerp(0.5F, totalAlpha, 1.0F);
        heightOffset = Mth.lerp(0.5F, heightOffset, -3.0F);
    }

    private void updateFade() {
        totalAlpha = Mth.lerp(0.5F, totalAlpha, 0.0F);
        heightOffset = Mth.lerp(0.5F, heightOffset, 0.0F);
        invalidAlpha = Mth.lerp(0.35F, invalidAlpha, 0.0F);
        costAlpha = Mth.lerp(0.35F, costAlpha, 0.0F);
        transdimensionalAlpha = Mth.lerp(0.35F, transdimensionalAlpha, 0.0F);
    }

    private void updateAnimations() {
        invalidAlpha = Mth.lerp(0.35F, invalidAlpha, invalid ?
                Mth.clamp((Mth.sin(invalidDisplayTicks * 0.4F) * 0.5F) + 0.5F, 0.75F, 1.0F) : 0.0F);
        costAlpha = Mth.lerp(0.35F, costAlpha, totalAlpha > 0.01F && !invalid ?
                Mth.clamp((Mth.sin(costDisplayTicks * 0.25F) * 0.2F) + 0.5F, 0.75F, 1.0F) : 0.0F);
        transdimensionalAlpha = Mth.lerp(0.35F, transdimensionalAlpha, transdimensional ?
                Mth.clamp((Mth.sin(transdimensionalDisplayTicks * 0.1F) * 0.5F) + 0.5F, 0.4F, 1.0F) : 0.0F);

        invalidDisplayTicks = invalidAlpha > 0.01F ? invalidDisplayTicks + 1 : 0;
        costDisplayTicks = costAlpha > 0.01F ? costDisplayTicks + 1 : 0;
        transdimensionalDisplayTicks = transdimensionalAlpha > 0.01F ? transdimensionalDisplayTicks + 1 : 0;
    }

    private static boolean isSupportedTool(ItemStack stack) {
        return MirrorItem.is(stack) || MagniaAttractorItem.is(stack);
    }
}
