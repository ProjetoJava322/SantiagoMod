package org.amemeida.santiago.items;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.amemeida.santiago.components.EnderIOComponent;
import org.amemeida.santiago.registry.items.ModComponents;
import org.amemeida.santiago.util.RandomId;

/**
 * Classe que representa o item EnderCard, uma variação de PunchCard
 * que possui uma funcionalidade especial relacionada ao componente "Ender".
 */
public class EnderCard extends PunchCard {

    /**
     * Construtor que recebe as configurações do item e repassa para a superclasse.
     * 
     * @param settings configurações do item
     */
    public EnderCard(Settings settings) {
        super(settings);
    }

    /**
     * Método chamado para pós-processar os componentes do ItemStack.
     * Adiciona ou remove o efeito visual de brilho de encantamento (glint)
     * baseado na presença do componente "Ender".
     * 
     * @param stack o ItemStack a ser processado
     */
    @Override
    public void postProcessComponents(ItemStack stack) {
        if (stack.contains(ModComponents.ENDER)) {
            // Adiciona o efeito visual de brilho se o componente "Ender" estiver presente
            stack.set(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true);
        } else {
            // Remove o efeito de brilho caso o componente não esteja presente
            stack.remove(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE);
        }
    }

    /**
     * Método chamado quando o jogador usa (clica com) o item.
     * Permite copiar o componente "Ender" para a mão secundária se o jogador estiver agachado
     * e segurando o mesmo item nas duas mãos.
     * Se o item ainda não possui o componente "Ender", cria um novo componente com ID aleatório.
     * 
     * @param world mundo onde o evento ocorre
     * @param user jogador que usa o item
     * @param hand mão utilizada para o uso (principal ou secundária)
     * @return o resultado da ação (sucesso, passagem, etc)
     */
    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient) {
            // No cliente, não executa a ação, apenas passa
            return ActionResult.PASS;
        }

        if (user.isSneaking()) {
            // Se o jogador estiver agachado
            ItemStack main = user.getMainHandStack();
            ItemStack off = user.getOffHandStack();

            // Verifica se os itens da mão principal e mão secundária são iguais
            if (main.getItem() == off.getItem()) {
                if (hand == Hand.MAIN_HAND) {
                    // Se a ação foi com a mão principal, retorna sucesso sem fazer nada
                    return ActionResult.SUCCESS;
                } else if (!off.contains(ModComponents.ENDER) && main.contains(ModComponents.ENDER)) {
                    // Copia os componentes "Ender" da mão principal para a secundária
                    off.applyComponentsFrom(main.getComponents());
                    return ActionResult.SUCCESS;
                }
            }
        }

        // Se o item usado não contém o componente "Ender", cria um novo componente com ID aleatório
        var stack = user.getStackInHand(hand);
        if (!stack.contains(ModComponents.ENDER)) {
            var text = RandomId.genRandom(world.getRandom());
            stack.set(ModComponents.ENDER, new EnderIOComponent(text));
        }

        // Chama o método use da superclasse para comportamentos padrão
        return super.use(world, user, hand);
    }
}
