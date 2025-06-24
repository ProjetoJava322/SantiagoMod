package org.amemeida.santiago.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import org.amemeida.santiago.registry.items.ModComponents;

/**
 * Componente que armazena um texto/script dentro de um ItemStack.
 * Pode ser usado para associar scripts personalizados a itens.
 *
 * COMENTARIOS FEITOS POR IA
 *
 * @param text O conteúdo textual/script armazenado.
 */
public record IOComponent(String text) implements TextContent {

    /**
     * Codec usado para serializar e desserializar o IOComponent.
     * Associa o campo "script" ao valor do método {@link TextContent#text()}.
     */
    public static final Codec<TextContent> CODEC = RecordCodecBuilder.create(builder -> {
        return builder.group(
                Codec.STRING.fieldOf("script").forGetter(TextContent::text)
        ).apply(builder, IOComponent::new);
    });

    /**
     * Define este componente no {@link ItemStack}, armazenando o script fornecido.
     *
     * @param script O texto/script a ser salvo.
     * @param stack  O item no qual o componente será definido.
     */
    @Override
    public void setComponent(String script, ItemStack stack) {
        stack.set(ModComponents.IO, new IOComponent(script));
    }
}
