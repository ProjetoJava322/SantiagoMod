package org.amemeida.santiago.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;

import org.amemeida.santiago.file.Script;
import org.amemeida.santiago.registry.items.ModComponents;

/**
 * Componente que armazena um {@link Script} dentro de um {@link ItemStack}.
 * Permite salvar, recuperar e atualizar scripts personalizados vinculados a itens.
 *
 * COMENTARIOS FEITOS POR IA
 */
public class ScriptComponent implements TextContent {

    /**
     * Codec utilizado para serializar e desserializar um {@link ScriptComponent}.
     * Usa o codec interno do {@link Script} para persistência.
     */
    public static final Codec<ScriptComponent> CODEC = RecordCodecBuilder
            .create(builder -> builder.group(
                    Script.CODEC.fieldOf("script")
                            .forGetter(ScriptComponent::getScript)
            ).apply(builder, ScriptComponent::new));

    /**
     * Script associado a este componente.
     */
    private final Script script;

    /**
     * Construtor que define o script interno.
     *
     * @param script O script a ser armazenado neste componente.
     */
    public ScriptComponent(Script script) {
        this.script = script;
    }

    /**
     * Retorna o {@link Script} armazenado neste componente.
     *
     * @return o objeto {@link Script} atual.
     */
    public Script getScript() {
        return this.script;
    }

    /**
     * Retorna o conteúdo do script como {@link String}.
     * Se o script for nulo, retorna uma string vazia.
     *
     * @return conteúdo textual do script.
     */
    @Override
    public String text() {
        if (script == null) {
            return "";
        }

        return script.getScript();
    }

    /**
     * Atualiza o conteúdo do script e define o componente atualizado no {@link ItemStack}.
     *
     * @param script novo conteúdo do script em forma de texto.
     * @param stack  o item no qual o componente será definido.
     */
    @Override
    public void setComponent(String script, ItemStack stack) {
        this.script.writeScript(script);
        stack.set(ModComponents.SCRIPT, new ScriptComponent(this.script));
    }
}
