package org.amemeida.santiago.components;

import com.mojang.serialization.Codec;
import net.minecraft.item.ItemStack;

/**
 * @see net.minecraft.component.type.WritableBookContentComponent
 * @see net.minecraft.component.type.BookContent
 */

public interface TextContent {
    public abstract String text();
    public abstract void setComponent(String text, ItemStack stack);
    public abstract Codec<TextContent> getCodec();
}
