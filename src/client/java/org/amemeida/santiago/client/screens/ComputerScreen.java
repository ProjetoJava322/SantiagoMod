package org.amemeida.santiago.client.screens;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.amemeida.santiago.Santiago;
import org.amemeida.santiago.computer.ComputerScreenHandler;
import org.amemeida.santiago.net.PCModeC2SPayload;

/**
 * @see net.minecraft.client.gui.screen.ingame.CrafterScreen
 * @see net.minecraft.client.gui.screen.ingame.BeaconScreen
 * @see net.minecraft.client.gui.screen.ingame.HopperScreen
 * @see net.minecraft.client.gui.screen.ingame.AbstractFurnaceScreen
 * @see net.minecraft.client.gui.screen.TitleScreen
 * @see net.minecraft.client.gui.screen.TitleScreen
 */

public class ComputerScreen extends HandledScreen<ComputerScreenHandler> {
    public static final Identifier TEXTURE = Identifier.of(Santiago.MOD_ID, "textures/gui/computer_in_out_gui.png");
    public ComputerScreen(ComputerScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 243;
        this.backgroundHeight = 251;
        this.playerInventoryTitleX = 9;
        this.playerInventoryTitleY = this.backgroundHeight - 94;
    }

    @Override
    protected void init() {
        super.init();

        var btn_oper = ButtonWidget.builder(Text.literal(handler.getData().write() ? "Write" : "Compare"),
                (button) -> {
            System.out.println("click: " + handler.getWrite());
            var newWrite = !handler.getWrite();
            handler.setWrite(newWrite);

            var payload = new PCModeC2SPayload(this.getScreenHandler().syncId,  newWrite);
            ClientPlayNetworking.send(payload);

            if (handler.getWrite()) {
                button.setMessage(Text.literal("Write"));
            } else {
                button.setMessage(Text.literal("Compare"));
            }
        }).position(width/2 + 66, height/2 - 105).size(50, 15).build();

        var btn_and =  ButtonWidget.builder(Text.literal(handler.getData().and() ? "And" : "Or"),
                (button) -> {
                    System.out.println("click: " + handler.getAnd());
                    var newAnd = !handler.getAnd();
                    handler.setAnd(newAnd);

                    System.out.println(newAnd);

                    var payload = new PCModeC2SPayload(this.getScreenHandler().syncId,  newAnd);
                    ClientPlayNetworking.send(payload);

                    if (handler.getAnd()) {
                        button.setMessage(Text.literal("And"));
                    } else {
                        button.setMessage(Text.literal("Or"));
                    }
                }).position(width/2 + 66, height/2 - 81).size(50, 15).build();

        addDrawableChild(btn_oper);
        addDrawableChild(btn_and);
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
        context.drawTexture(RenderLayer::getGuiTextured, TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight, 256,256);
    }
}
