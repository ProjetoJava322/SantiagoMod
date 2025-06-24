package org.amemeida.santiago.client.screens;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.client.util.SelectionManager;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.StringHelper;
import net.minecraft.client.gui.widget.MultilineTextWidget;
import org.amemeida.santiago.net.UpdateStackC2SPayload;

import java.awt.event.KeyEvent;

@Environment(EnvType.CLIENT)
/**
 * Tela para edição de texto com suporte a múltiplas linhas, seleção e manipulação de texto.
 */
public class TextEditScreen extends Screen {
    private final int slot;
    private final ItemStack stack;
    private String[] lines;
    private Text text;
    private int currCursorLinePos;
    private final SelectionManager selectionManager;

    /**
     * Construtor da tela de edição de texto.
     *
     * @param slot  Slot do inventário que contém o item.
     * @param stack Item sendo editado.
     * @param text  Texto inicial exibido na tela.
     */
    public TextEditScreen(int slot, ItemStack stack, String text) {
        super(NarratorManager.EMPTY);
        this.slot = slot;
        this.stack = stack;
        this.currCursorLinePos = 0;
        this.text = Text.literal(text);
        this.lines = getLines(this.text);

        this.selectionManager = new SelectionManager(
                () -> this.text.getString(),
                (str) -> this.text = Text.literal(str),
                this::getClipboard,
                this::setClipboard,
                (str) -> str.length() < Integer.MAX_VALUE && this.textRenderer.getWrappedLinesHeight(str, 114) <= 128
        );
    }

    /**
     * Divide o texto em linhas, adicionando o caractere '\n' ao final de cada uma.
     *
     * @param text Texto a ser dividido.
     * @return Array de linhas.
     */
    private String[] getLines(Text text) {
        String string = text.getString();
        String[] result = string.split("\n");
        for (int i = 0; i < result.length; i++) {
            result[i] = result[i].concat("\n");
        }
        return result;
    }

    /**
     * Remove espaços em branco do início e fim do texto e atualiza o campo de texto.
     *
     * @return Texto já cortado.
     */
    public String trim() {
        this.text = Text.literal(this.text.getString().trim());
        return this.text.getString();
    }

    /**
     * Fecha a tela e envia os dados do texto editado para o servidor.
     */
    @Override
    public void close() {
        this.writeNbtData();
        super.close();
    }

    /**
     * Renderiza a tela e o cursor de texto.
     */
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);
        this.setFocused(null);
        MultilineTextWidget textWidget = new MultilineTextWidget(20, 20, this.text, this.textRenderer);
        textWidget.renderWidget(context, mouseX, mouseY, deltaTicks);

        this.lines = getLines(this.text);

        int cursorPos = this.selectionManager.getSelectionStart();
        cursorPos = Math.min(cursorPos, this.text.getString().length());

        int lineIndex = 0, beforeCursor = 0;

        for (int i = 0; i < cursorPos; i++) {
            beforeCursor++;
            if (this.text.getString().charAt(i) == '\n') {
                lineIndex++;
                beforeCursor = 0;
            }
        }
        int x = beforeCursor == 0 ? 20 : 20 + getXPos(beforeCursor, lines[lineIndex]);
        int y = 20 + lineIndex * this.textRenderer.fontHeight + 1; // 1 pixel abaixo da linha
        context.drawText(this.textRenderer,"_", x, y, 16777215, false);
    }

    /**
     * Calcula a posição horizontal do cursor com base na largura dos caracteres antes dele.
     *
     * @param beforeCursor Número de caracteres antes do cursor na linha atual.
     * @param line         Linha de texto atual.
     * @return Posição horizontal em pixels.
     */
    private int getXPos(int beforeCursor, String line){
        int width = 0;
        for (int i = 0; i < beforeCursor; i++) {
            char currChar = line.charAt(i);
            if (currChar == '\n' || ((currChar >= 'A' &&  currChar <= 'Z') && currChar != 'I') || currChar == ' ' || (currChar >= '0' && currChar <= '9')) {
                width += 6;
                //sabemos que isso é redundante, mas foi mantido intencionalmente
            } else if ((currChar >= 'a' && currChar <= 'z') && currChar != 'i' && currChar != 'l' && currChar != 't' && currChar != 'f' && currChar != 'k'|| currChar == '(' || currChar == ')') {
                width += 6;
            } else if (currChar == ',' || currChar == '.' || currChar == ';' || currChar == '\'' || currChar == 'i') {
                width += 2;
            } else if (currChar == 't' || currChar == '[' || currChar == ']' || currChar == '{' || currChar == '}' || currChar == '"') {
                width += 4;
            } else if (currChar == 'f' || currChar == 'k') {
                width += 5;
            } else {
                width += 3;
            }
        }
        return width;
    }

    /**
     * Renderiza o fundo da tela.
     */
    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        this.renderInGameBackground(context);
        this.renderDarkening(context);
    }

    /**
     * Inicializa a tela.
     */
    @Override
    protected void init() {
        super.init();
    }

    /**
     * Envia os dados atualizados do texto para o servidor.
     */
    private void writeNbtData() {
        var text = this.trim();
        var payload = new UpdateStackC2SPayload(this.slot, this.stack, text);
        ClientPlayNetworking.send(payload);
    }

    /**
     * Define o conteúdo da área de transferência.
     */
    private void setClipboard(String clipboard) {
        if (this.client != null) {
            SelectionManager.setClipboard(this.client, clipboard);
        }
    }

    /**
     * Obtém o conteúdo da área de transferência.
     */
    private String getClipboard() {
        return this.client != null ? SelectionManager.getClipboard(this.client) : "";
    }

    /**
     * Processa pressionamentos de tecla.
     */
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (super.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        } else {
            return this.keyPressedEditMode(keyCode, scanCode, modifiers);
        }
    }

    /**
     * Processa caracteres digitados.
     */
    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (super.charTyped(chr, modifiers)) {
            return true;
        } else if (StringHelper.isValidChar(chr)) {
            this.selectionManager.insert(Character.toString(chr));
            return true;
        } else {
            return false;
        }
    }

    /**
     * Processa pressionamentos de tecla específicos do modo edição.
     */
    private boolean keyPressedEditMode(int keyCode, int scanCode, int modifiers) {
        if (Screen.isSelectAll(keyCode)) {
            this.selectionManager.selectAll();
            return true;
        }
        if (Screen.isCopy(keyCode)) {
            this.selectionManager.copy();
            return true;
        } else if (Screen.isPaste(keyCode)) {
            this.selectionManager.paste();
            return true;
        } else if (Screen.isCut(keyCode)) {
            this.selectionManager.cut();
            return true;
        } else {
            SelectionManager.SelectionType selectionType = Screen.hasControlDown() ? SelectionManager.SelectionType.WORD : SelectionManager.SelectionType.CHARACTER;
            return switch (keyCode) {
                case 257, 335 -> {
                    this.selectionManager.insert("\n");
                    yield true;
                }
                case 259 -> {
                    this.selectionManager.delete(-1, selectionType);
                    yield true;
                }
                case 261 -> {
                    this.selectionManager.delete(1, selectionType);
                    yield true;
                }
                case 262 -> {
                    this.selectionManager.moveCursor(1, Screen.hasShiftDown(), selectionType);
                    yield true;
                }
                case 263 -> {
                    this.selectionManager.moveCursor(-1, Screen.hasShiftDown(), selectionType);
                    yield true;
                }
                case 264 -> {
                    this.moveDownLine();
                    yield true;
                }
                case 265 -> {
                    this.moveUpLine();
                    yield true;
                }
                case 268 -> {
                    this.moveToLineStart();
                    yield true;
                }
                case 269 -> {
                    this.moveToLineEnd();
                    yield true;
                }
                case 9 -> {
                    this.selectionManager.insert("    ");
                    yield true;
                }
                default -> false;
            };
        }
    }

    /**
     * Move o cursor uma linha para cima.
     */
    private void moveUpLine() {
        this.moveVertically(-1);
    }

    /**
     * Move o cursor uma linha para baixo.
     */
    private void moveDownLine() {
        this.moveVertically(1);
    }

    /**
     * Move verticalmente o cursor um número especificado de linhas.
     *
     * @param lines Quantidade de linhas para mover (positivo para baixo, negativo para cima).
     */
    private void moveVertically(int lines) {
        int i = this.selectionManager.getSelectionStart();
        // int j = this.getPageContent().getVerticalOffset(i, lines);
        this.selectionManager.moveCursorTo(10, Screen.hasShiftDown());
    }

    /**
     * Move o cursor para o início da linha atual ou para o início do texto se Ctrl estiver pressionado.
     */
    private void moveToLineStart() {
        if (Screen.hasControlDown()) {
            this.selectionManager.moveCursorToStart(Screen.hasShiftDown());
        } else {
            int i = this.selectionManager.getSelectionStart();
            // int j = this.getPageContent().getLineStart(i);
            this.selectionManager.moveCursorTo(10, Screen.hasShiftDown());
        }
    }

    /**
     * Move o cursor para o fim da linha atual ou para o fim do texto se Ctrl estiver pressionado.
     */
    private void moveToLineEnd() {
        if (Screen.hasControlDown()) {
            this.selectionManager.moveCursorToEnd(Screen.hasShiftDown());
        } else {
            // BookEditScreen.PageContent pageContent = this.getPageContent();
            int i = this.selectionManager.getSelectionStart();
            int j = this.text.getString().split("\n")[i].length() - 1;
            this.selectionManager.moveCursorTo(j, Screen.hasShiftDown());
        }
    }
}

