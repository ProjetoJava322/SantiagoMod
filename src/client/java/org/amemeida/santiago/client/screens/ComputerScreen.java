package org.amemeida.santiago.client.screens;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import org.amemeida.santiago.computer.ComputerScreenHandler;
import org.amemeida.santiago.net.PCModeC2SPayload;

/**
 * @see net.minecraft.client.gui.screen.ingame.CrafterScreen
 * @see net.minecraft.client.gui.screen.ingame.BeaconScreen
 * @see net.minecraft.client.gui.screen.TitleScreen
 * @see net.minecraft.client.gui.screen.TitleScreen
 */

public class ComputerScreen extends HandledScreen<ComputerScreenHandler> {
    public ComputerScreen(ComputerScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    private boolean test = false;

    @Override
    protected void init() {
        super.init();

        var btn = ButtonWidget.builder(Text.literal(handler.getWrite() ? "Write" : "Compare"), (button) -> {
            if (handler.toggleWrite()) {
                button.setMessage(Text.literal("Write"));
            } else {
                button.setMessage(Text.literal("Compare"));
            }

            var payload = new PCModeC2SPayload(this.getScreenHandler().syncId);
            ClientPlayNetworking.send(payload);
        }).position(50, 50).build();

        addDrawableChild(btn);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
//        context.drawTexture(RenderLayer::getGuiTextured, TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight, 256,256);
    }
}
