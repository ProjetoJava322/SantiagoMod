package org.amemeida.santiago.client.screens;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.ScrollableTextWidget;
import net.minecraft.text.Text;

public class ComputerScrollableTextWidget extends ScrollableTextWidget {
    public ComputerScrollableTextWidget(int x, int y, int width, int height, Text message, TextRenderer textRenderer) {
        super(x, y, width, height, message, textRenderer);
    }

    public void setScrollAmount(double amount) {
        this.setScrollY(amount);
    }

    public double getScrollAmount() {
        return this.getScrollY();
    }

    public double getScrollStep() {
        return this.getDeltaYPerScroll();
    }
}
