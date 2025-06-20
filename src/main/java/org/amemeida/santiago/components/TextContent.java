package org.amemeida.santiago.components;

import com.mojang.serialization.Codec;
import net.minecraft.item.ItemStack;
import org.amemeida.santiago.registry.items.ModComponents;

/**
 * @see net.minecraft.component.type.WritableBookContentComponent
 * @see net.minecraft.component.type.BookContent
 */

public interface TextContent {
    public abstract String text();
    public abstract void setComponent(String script, ItemStack stack);

    public static TextContent get(ItemStack stack) {
        if (stack.contains(ModComponents.IO)) {
            return stack.get(ModComponents.IO);
        } else if (stack.contains(ModComponents.SCRIPT)) {
            return stack.get(ModComponents.SCRIPT);
        }

        return null;
    }
}
