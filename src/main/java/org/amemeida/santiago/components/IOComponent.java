package org.amemeida.santiago.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import org.amemeida.santiago.registry.items.ModComponents;

public record IOComponent(String text) implements TextContent {
    public static final Codec<TextContent> CODEC = RecordCodecBuilder.create(builder -> {
        return builder.group(
                Codec.STRING.fieldOf("script").forGetter(TextContent::text)
        ).apply(builder, IOComponent::new);
    });

    @Override
    public void setComponent(String script, ItemStack stack) {
        stack.set(ModComponents.IO, new IOComponent(script));
    }
}
