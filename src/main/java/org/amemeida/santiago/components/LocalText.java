package org.amemeida.santiago.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import org.amemeida.santiago.registry.items.ModComponents;

public record LocalText(String text) implements TextContent {
    public static final Codec<LocalText> CODEC = RecordCodecBuilder.create(builder -> {
        return builder.group(
                Codec.STRING.fieldOf("text").forGetter(TextContent::text)
        ).apply(builder, LocalText::new);
    });

    @Override
    public void setComponent(String text, ItemStack stack) {
        System.out.print("LOCAL!!!!! ");
        System.out.println(text);
        stack.set(ModComponents.LOCAL_TEXT, new LocalText(text));
    }
}
