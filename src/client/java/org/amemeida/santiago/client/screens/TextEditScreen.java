package org.amemeida.santiago.client.screens;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.client.util.SelectionManager;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.StringHelper;
import net.minecraft.client.gui.widget.MultilineTextWidget;
import org.amemeida.santiago.components.LocalText;
import org.amemeida.santiago.components.EnderText;
import org.amemeida.santiago.components.TextContent;
import org.amemeida.santiago.registry.items.ModComponents;


/**
 * @see net.minecraft.client.gui.screen.ingame.BookEditScreen
 */

@Environment(EnvType.CLIENT)
public class TextEditScreen extends Screen {
    private final ItemStack stack;

    private Text text;
    private final TextContent textContent;

    private final SelectionManager selectionManager;

    public TextEditScreen(ItemStack stack, TextContent textContent) {
        super(NarratorManager.EMPTY);
        this.stack = stack;

        this.textContent = textContent;
        this.text = Text.literal(textContent.text());

        this.selectionManager = new SelectionManager(
                () -> this.text.getString(),
                (str) -> this.text = Text.literal(str),
                this::getClipboard,
                this::setClipboard,
                (str) -> str.length() < 1024 && this.textRenderer.getWrappedLinesHeight(str, 114) <= 128
        );
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
        stack.set(ModComponents.LOCAL_TEXT, new LocalText(text));
//        this.textContent.setComponent(text, this.stack);
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
