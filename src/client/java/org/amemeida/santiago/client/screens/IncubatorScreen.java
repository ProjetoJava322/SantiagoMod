package org.amemeida.santiago.client.screens;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.amemeida.santiago.Santiago;
import org.amemeida.santiago.incubator.IncubatorScreenHandler;

/**
 * Tela da incubadora que exibe a interface gráfica do IncubatorBlock.
 */
public class IncubatorScreen extends HandledScreen<IncubatorScreenHandler> {
    private static final Identifier GUI_TEXTURE =
            Identifier.of(Santiago.MOD_ID, "textures/gui/incubator_gui.png");
    private static final Identifier ARROW_TEXTURE =
            Identifier.of(Santiago.MOD_ID, "textures/gui/arrow_progress.png");

    /**
     * Construtor da tela da incubadora.
     *
     * @param handler ScreenHandler da incubadora
     * @param inventory inventário do jogador
     * @param title título da interface
     */
    public IncubatorScreen(IncubatorScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    /**
     * Desenha o fundo da interface.
     */
    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        context.drawTexture(RenderLayer::getGuiTextured, GUI_TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight, 256, 256);

        renderProgressArrow(context, x, y);
    }

    /**
     * Renderiza a seta de progresso da incubação.
     *
     * @param context contexto gráfico para desenhar
     * @param x coordenada x do canto superior esquerdo da tela
     * @param y coordenada y do canto superior esquerdo da tela
     */
    private void renderProgressArrow(DrawContext context, int x, int y) {
        if(handler.isCrafting()) {
            context.drawTexture(RenderLayer::getGuiTextured, ARROW_TEXTURE, x + 73, y + 35, 0, 0,
                    handler.getScaledArrowProgress(), 16, 24, 16);
        }
    }

    /**
     * Renderiza a tela e as tooltips.
     */
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }
}
