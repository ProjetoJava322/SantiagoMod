package org.amemeida.santiago.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import org.amemeida.santiago.registry.items.ModComponents;

public record ScriptComponent(String text) implements TextContent {
    public static final Codec<TextContent> CODEC = RecordCodecBuilder.create(builder -> {
        return builder.group(
                Codec.STRING.fieldOf("file").forGetter(TextContent::text)
        ).apply(builder, ScriptComponent::new);
    });

    @Override
    public void setComponent(String text, ItemStack stack) {
        stack.set(ModComponents.ENDER_TEXT, new ScriptComponent(text));
    }

    @Override
    public Codec<TextContent> getCodec() {
        return CODEC;
    }
}
