package org.amemeida.santiago.revolution_table.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.recipe.input.RecipeInput;

import java.util.List;

/**
 * Representa a entrada (input) para a receita da Revolution Table.
 * Contém uma lista fixa de 12 slots de itens, representados por {@link ItemStack}.
 * Essa classe implementa {@link RecipeInput} para ser utilizada no sistema de receitas.
 * 
 * Além disso, mantém um {@link RecipeFinder} para facilitar a correspondência dos ingredientes,
 * e calcula a quantidade efetiva de itens não vazios na entrada.
 */
public class RevolutionTableRecipeInput implements RecipeInput {
    
    /** Número fixo de slots de entrada esperados (12). */
    private static final int INPUT_SLOTS = 12;

    /** Lista de stacks dos itens nos slots da receita. */
    private final List<ItemStack> stacks;

    /** Utilitário para verificar correspondência entre ingredientes da receita. */
    private final RecipeFinder matcher = new RecipeFinder();

    /** Quantidade de slots que contêm itens não vazios. */
    private final int stackCount;

    /**
     * Construtor privado que inicializa a lista de stacks e o RecipeFinder.
     * Conta quantos stacks não estão vazios e adiciona cada item ao matcher.
     * 
     * @param stacks lista de ItemStacks (deve ter tamanho 12)
     */
    private RevolutionTableRecipeInput(List<ItemStack> stacks) {
        this.stacks = stacks;
        int count = 0;

        for (int i = 0; i < INPUT_SLOTS; i++) {
            ItemStack itemStack = this.stacks.get(i);
            if (!itemStack.isEmpty()) {
                count++;
                this.matcher.addInput(itemStack, 1);
            }
        }

        this.stackCount = count;
    }

    /**
     * Factory method para criar uma instância a partir de uma lista de ItemStacks.
     * 
     * @param stacks lista de stacks (tamanho esperado 12)
     * @return nova instância de RevolutionTableRecipeInput
     */
    public static RevolutionTableRecipeInput create(List<ItemStack> stacks) {
        return new RevolutionTableRecipeInput(stacks);
    }

    /**
     * Retorna o ItemStack do slot especificado.
     * 
     * @param slot índice do slot (0 a 11)
     * @return ItemStack no slot
     */
    @Override
    public ItemStack getStackInSlot(int slot) {
        return this.stacks.get(slot);
    }

    /**
     * Retorna o tamanho total da lista de stacks (12).
     * 
     * @return tamanho da lista de stacks
     */
    @Override
    public int size() {
        return this.stacks.size();
    }

    /**
     * Indica se a entrada está vazia (nenhum slot contém itens).
     * 
     * @return true se nenhum slot contém item, false caso contrário
     */
    @Override
    public boolean isEmpty() {
        return this.stackCount == 0;
    }

    /**
     * Retorna o objeto RecipeFinder usado para correspondência da receita.
     * 
     * @return RecipeFinder com os ingredientes adicionados
     */
    public RecipeFinder getRecipeMatcher() {
        return this.matcher;
    }

    /**
     * Retorna a lista de ItemStacks que compõem esta entrada.
     * 
     * @return lista imutável ou modificável (dependendo da implementação) de stacks
     */
    public List<ItemStack> getStacks() {
        return this.stacks;
    }

    /**
     * Retorna a contagem de stacks que não estão vazios nesta entrada.
     * 
     * @return quantidade de slots ocupados
     */
    public int getStackCount() {
        return this.stackCount;
    }

    /**
     * Método estático que compara duas listas de ItemStacks, verificando se são exatamente iguais.
     * Usa ItemStack.areEqual para comparação detalhada.
     * 
     * @param left lista esquerda
     * @param right lista direita
     * @return true se forem iguais em tamanho e conteúdo; false caso contrário
     */
    public static boolean stacksEqual(List<ItemStack> left, List<ItemStack> right) {
        if (left.size() != right.size()) {
            return false;
        }
        for (int i = 0; i < left.size(); i++) {
            if (!ItemStack.areEqual(left.get(i), right.get(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Compara esta entrada com outro objeto para verificar igualdade.
     * Duas entradas são iguais se tiverem o mesmo número de stacks não vazios
     * e se as listas de stacks forem iguais.
     * 
     * @param o outro objeto para comparar
     * @return true se forem iguais, false caso contrário
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RevolutionTableRecipeInput other)) {
            return false;
        }
        return this.stackCount == other.stackCount && stacksEqual(this.stacks, other.stacks);
    }

    /**
     * Gera o hash code baseado na lista de ItemStacks.
     * 
     * @return valor hash da entrada
     */
    @Override
    public int hashCode() {
        int hash = 0;
        for (ItemStack itemStack : this.stacks) {
            hash = hash * 31 + ItemStack.hashCode(itemStack);
        }
        return hash;
    }
}
