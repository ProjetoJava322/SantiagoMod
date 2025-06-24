package org.amemeida.santiago.components;

import net.minecraft.item.ItemStack;
import org.amemeida.santiago.registry.items.ModComponents;

/**
 * Interface para componentes que armazenam conteúdo textual em {@link ItemStack}s.
 * Pode representar diferentes tipos de dados como scripts, strings simples ou texto persistente.
 *
 * <p>Essa interface permite leitura e escrita de conteúdo textual dentro dos componentes de item.</p>
 *
 * @see net.minecraft.component.type.WritableBookContentComponent
 * @see net.minecraft.component.type.BookContent
 */
public interface TextContent {

    /**
     * Retorna o conteúdo textual representado por este componente.
     *
     * @return texto armazenado
     */
    public abstract String text();

    /**
     * Define o conteúdo textual deste componente e aplica no {@link ItemStack}.
     *
     * @param script texto a ser armazenado
     * @param stack  item no qual o componente será inserido
     */
    public abstract void setComponent(String script, ItemStack stack);

    /**
     * Recupera uma instância de {@link TextContent} de um {@link ItemStack},
     * verificando os componentes disponíveis (IO, SCRIPT, ENDER).
     *
     * @param stack item de onde será extraído o componente
     * @return instância de {@link TextContent}, ou {@code null} se não encontrado
     */
    public static TextContent get(ItemStack stack) {
        if (stack.contains(ModComponents.IO)) {
            return stack.get(ModComponents.IO);
        } else if (stack.contains(ModComponents.SCRIPT)) {
            return stack.get(ModComponents.SCRIPT);
        } else if (stack.contains(ModComponents.ENDER)) {
            return stack.get(ModComponents.ENDER);
        }

        return null;
    }
}
