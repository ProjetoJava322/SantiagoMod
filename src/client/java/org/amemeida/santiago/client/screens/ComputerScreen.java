package org.amemeida.santiago.client.screens;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.amemeida.santiago.Santiago;
import org.amemeida.santiago.computer.Computer;
import org.amemeida.santiago.computer.ComputerEntity;
import org.amemeida.santiago.computer.ComputerScreenHandler;
import org.amemeida.santiago.net.PCDataC2SPayload;
import org.amemeida.santiago.net.TriggerPCC2SPayload;

/**
 * @see net.minecraft.client.gui.screen.ingame.CrafterScreen
 * @see net.minecraft.client.gui.screen.ingame.BeaconScreen
 * @see net.minecraft.client.gui.screen.ingame.HopperScreen
 * @see net.minecraft.client.gui.screen.ingame.AbstractFurnaceScreen
 * @see net.minecraft.client.gui.screen.TitleScreen
 * @see net.minecraft.client.gui.screen.TitleScreen
 */

/**
 * Tela gráfica do computador, exibindo a interface e controlando interações do jogador.
 */
public class ComputerScreen extends HandledScreen<ComputerScreenHandler> {
    private final ClientWorld world;

    public static final Identifier TEXTURE = Identifier.of(Santiago.MOD_ID, "textures/gui/computer_in_out_gui.png");
    private ButtonWidget run;

    /**
     * Construtor da tela do computador.
     *
     * @param handler o ScreenHandler do computador
     * @param inventory inventário do jogador
     * @param title título da tela
     */
    public ComputerScreen(ComputerScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 243;
        this.backgroundHeight = 251;
        this.playerInventoryTitleX = 9;
        this.playerInventoryTitleY = this.backgroundHeight - 94;

        this.world = (ClientWorld) inventory.player.getWorld();
    }

    /**
     * Inicializa os widgets da interface.
     */
    @Override
    protected void init() {
        super.init();

        addDrawableChild(ButtonWidget.builder(Text.literal(handler.getData().output().toString()),
                (button) -> {
                    var newWrite = handler.getOutputMode().cycleNext();
                    handler.setOutputMode(newWrite);

                    this.sendPayload();
                    button.setMessage(Text.literal(handler.getOutputMode().toString()));
                }).position(width/2 + 66, height/2 - 107).size(50, 15).build());

        addDrawableChild(ButtonWidget.builder(Text.literal(handler.getData().result().toString()),
                (button) -> {
                    var newAnd = handler.getResultMode().cycleNext();
                    handler.setResultMode(newAnd);

                    System.out.println(newAnd);

                    this.sendPayload();
                    button.setMessage(Text.literal(handler.getResultMode().toString()));
                }).position(width/2 + 66, height/2 - 87).size(50, 15).build());

        this.run = (ButtonWidget.builder(Text.literal("▶"), (button) -> {
            this.trigger();
        }).position(width/2 + 76, height/2 - 67).size(30, 15).build());

        addDrawableChild(run);
    }

    /**
     * Obtém o estado atual do computador no mundo.
     *
     * @return estado do computador
     */
    public Computer.ComputerState getState() {
        return world.getBlockState(handler.getData().pos()).get(Computer.STATE);
    }

    /**
     * Envia dados atualizados do computador para o servidor.
     */
    public void sendPayload() {
        var data = new ComputerEntity.ComputerData(handler.getData().pos(), handler.getOutputMode(),
                handler.getResultMode());

        var payload = new PCDataC2SPayload(this.getScreenHandler().syncId, data);
        ClientPlayNetworking.send(payload);
    }

    /**
     * Envia um comando para disparar a execução do computador.
     */
    public void trigger() {
        var payload = new TriggerPCC2SPayload(handler.getData().pos());
        ClientPlayNetworking.send(payload);
    }

    /**
     * Renderiza a tela e atualiza o botão de execução com o estado do computador.
     */
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        Computer.ComputerState state = getState();
        String message = " ";
        switch(state){
            case RUNNING:
                message = "...";
                break;
            case SUCCESS:
                message = "✔";
                break;
            case FAILURE:
                message = "✖";
                break;
            case IDLE:
                message = "▶";
                break;
            case LOCKED:
                message = "LKD";
                break;
            case ERROR:
                message = "ERR";
                break;
        }
        run.setMessage(Text.literal(message));
        drawMouseoverTooltip(context, mouseX, mouseY);
    }

    /**
     * Desenha o fundo da interface.
     */
    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        context.drawTexture(RenderLayer::getGuiTextured, TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight, 256,256);
    }
}

