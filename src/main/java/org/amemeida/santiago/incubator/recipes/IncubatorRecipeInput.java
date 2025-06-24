package org.amemeida.santiago.incubator.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.input.RecipeInput;

/**
 * Representa a entrada da receita do incubador.
 * Contém apenas um ItemStack que será usado como ingrediente na receita.
 * Implementa a interface RecipeInput para fornecer acesso aos itens da receita.
 */
public record IncubatorRecipeInput(ItemStack input) implements RecipeInput {

    /**
     * Retorna o ItemStack presente no slot especificado.
     * Como esta entrada possui apenas um slot, sempre retorna o ItemStack 'input'
     * independentemente do índice passado.
     * 
     * @param slot o índice do slot (deve ser 0)
     * @return o ItemStack da entrada
     */
    @Override
    public ItemStack getStackInSlot(int slot) {
        return input;
    }

    /**
     * Retorna o tamanho da entrada, que é 1 pois só há um item.
     * 
     * @return tamanho da entrada (1)
     */
    @Override
    public int size() {
        return 1;
    }
}
