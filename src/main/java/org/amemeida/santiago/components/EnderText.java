package org.amemeida.santiago.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import org.amemeida.santiago.registry.items.ModComponents;

public record EnderText(String text) implements TextContent {
    public static final Codec<EnderText> CODEC = RecordCodecBuilder.create(builder -> {
        return builder.group(
                Codec.STRING.fieldOf("file").forGetter(TextContent::text)
        ).apply(builder, EnderText::new);
    });

    @Override
    public void setComponent(String text, ItemStack stack) {
        stack.set(ModComponents.ENDER_TEXT, new EnderText(text));
    }
}
