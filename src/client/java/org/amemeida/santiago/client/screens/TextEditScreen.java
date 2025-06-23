package org.amemeida.santiago.client.screens;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.client.util.SelectionManager;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.StringHelper;
import net.minecraft.client.gui.widget.MultilineTextWidget;
import org.amemeida.santiago.net.UpdateStackC2SPayload;

@Environment(EnvType.CLIENT)
public class TextEditScreen extends Screen {
    private final int slot;
    private final ItemStack stack;
    private String[] lines;
    private Text text;
    private int currCursorLinePos;
    private final SelectionManager selectionManager;

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
                (str) -> str.length() < 50 && this.textRenderer.getWrappedLinesHeight(str, 114) <= 128
        );
    }

    private String[] getLines(Text text) {
        String string = text.getString();
        String[] result = string.split("\n");
        for (int i = 0; i < result.length; i++) {
            result[i] = result[i].concat("\n");
        }
        return result;
    }

//    private void finalizeBook(boolean signBook) {
//        if (this.dirty) {
//            this.writeNbtData();
//            int i = this.hand == Hand.MAIN_HAND ? this.player.getInventory().getSelectedSlot() : 40;
//            this.client.getNetworkHandler().sendPacket(new BookUpdateC2SPacket(i, this.pages, signBook ? Optional.of(this.trim()) : Optional.empty()));
//        }
//    }

    public String trim() {
        this.text = Text.literal(this.text.getString().trim());
        return this.text.getString();
    }

    @Override
    public void close() {
        this.writeNbtData();
        super.close();
    }

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
        int y = 20 + lineIndex * this.textRenderer.fontHeight + 1; // 1 pixel a baixo da linha
        context.drawText(this.textRenderer,"_", x, y, 16777215, false);

    }

    private int getXPos(int beforeCursor, String line){
        int width = 0;
        for (int i = 0; i < beforeCursor; i++) {
            char currChar = line.charAt(i);
            if (currChar == '\n' || ((currChar >= 'A' &&  currChar <= 'Z') && currChar != 'I') || currChar == ' ' || (currChar >= '0' && currChar <= '9')) {
                width += 6;
            //eu sei que isso é redundante, eu simplesmente não quis reescrever
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

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        this.renderInGameBackground(context);
        this.renderDarkening(context);
    }

    @Override
    protected void init() {
        super.init();
    }

    private void writeNbtData() {
        var text = this.trim();

        var payload = new UpdateStackC2SPayload(this.slot, this.stack, text);
        ClientPlayNetworking.send(payload);
    }

    private void setClipboard(String clipboard) {
        if (this.client != null) {
            SelectionManager.setClipboard(this.client, clipboard);
        }
    }

    private String getClipboard() {
        return this.client != null ? SelectionManager.getClipboard(this.client) : "";
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (super.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        } else {
            return this.keyPressedEditMode(keyCode, scanCode, modifiers);
        }
    }

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
                default -> false;
            };
        }
    }

    private void moveUpLine() {
        this.moveVertically(-1);
    }

    private void moveDownLine() {
        this.moveVertically(1);
    }

    private void moveVertically(int lines) {
        int i = this.selectionManager.getSelectionStart();
//        int j = this.getPageContent().getVerticalOffset(i, lines);
        this.selectionManager.moveCursorTo(10, Screen.hasShiftDown());
    }

    private void moveToLineStart() {
        if (Screen.hasControlDown()) {
            this.selectionManager.moveCursorToStart(Screen.hasShiftDown());
        } else {
            int i = this.selectionManager.getSelectionStart();
//            int j = this.getPageContent().getLineStart(i);
            this.selectionManager.moveCursorTo(10, Screen.hasShiftDown());
        }
    }

    private void moveToLineEnd() {
        if (Screen.hasControlDown()) {
            this.selectionManager.moveCursorToEnd(Screen.hasShiftDown());
        } else {
//            BookEditScreen.PageContent pageContent = this.getPageContent();
            int i = this.selectionManager.getSelectionStart();
            int j = this.text.getString().split("\n")[i].length() - 1;
            this.selectionManager.moveCursorTo(j, Screen.hasShiftDown());
        }
    }
}
