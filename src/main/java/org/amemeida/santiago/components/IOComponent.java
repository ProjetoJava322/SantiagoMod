package org.amemeida.santiago.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import org.amemeida.santiago.registry.items.ModComponents;

public record IOComponent(String text) implements TextContent {
    public static final Codec<TextContent> CODEC = RecordCodecBuilder.create(builder -> {
        return builder.group(
                Codec.STRING.fieldOf("text").forGetter(TextContent::text)
        ).apply(builder, IOComponent::new);
    });

    @Override
    public void setComponent(String text, ItemStack stack) {
        stack.set(ModComponents.LOCAL_TEXT, new IOComponent(text));
    }

    @Override
    public Codec<TextContent> getCodec() {
        return CODEC;
    }
}
