package org.amemeida.santiago.client.screens;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.amemeida.santiago.Santiago;
import org.amemeida.santiago.revolution_table.RevolutionTableScreenHandler;

//206x185

/**
 * Tela (GUI) para o bloco Revolution Table.
 * Exibe a interface gráfica personalizada para o container RevolutionTableScreenHandler.
 */
public class RevolutionTableScreen extends HandledScreen<RevolutionTableScreenHandler> {
    public static final Identifier TEXTURE = Identifier.of(Santiago.MOD_ID, "textures/gui/revolution_table_gui.png");

    /**
     * Construtor da tela, configura tamanho e posição da GUI.
     *
     * @param handler  O ScreenHandler ligado a esta tela.
     * @param inventory Inventário do jogador.
     * @param title    Título da tela.
     */
    public RevolutionTableScreen(RevolutionTableScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 206;
        this.backgroundHeight = 185;
        this.playerInventoryTitleX = 23;
        this.playerInventoryTitleY = this.backgroundHeight - 94;
    }

    /**
     * Renderiza a tela e os tooltips do mouse.
     */
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }

    /**
     * Desenha o fundo da tela (GUI).
     */
    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        context.drawTexture(RenderLayer::getGuiTextured, TEXTURE, x, y, 0, 0,
                backgroundWidth, backgroundHeight, 256,256);
    }
}
