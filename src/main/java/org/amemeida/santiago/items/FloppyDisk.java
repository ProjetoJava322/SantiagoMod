package org.amemeida.santiago.items;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import org.amemeida.santiago.components.ScriptComponent;
import org.amemeida.santiago.components.TextContent;
import org.amemeida.santiago.file.Script;
import org.amemeida.santiago.net.OpenScreenS2CPayload;
import org.amemeida.santiago.registry.items.ModComponents;
import org.amemeida.santiago.util.RandomId;
/**
 * Classe que representa o item FloppyDisk (disquete),
 * que pode armazenar scripts e interagir com o jogador.
 */
public class FloppyDisk extends Item {

    /**
     * Construtor que recebe as configurações do item e repassa para a superclasse.
     * 
     * @param settings configurações do item
     */
    public FloppyDisk(Item.Settings settings) {
        super(settings);
    }

    /**
     * Método chamado para pós-processar os componentes do ItemStack.
     * Adiciona ou remove o efeito visual de brilho de encantamento (glint)
     * baseado na presença do componente "Script".
     * 
     * @param stack o ItemStack a ser processado
     */
    @Override
    public void postProcessComponents(ItemStack stack) {
        if (stack.contains(ModComponents.SCRIPT)) {
            // Adiciona efeito de brilho se o item contém um script
            stack.set(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true);
        } else {
            // Remove o efeito se o script não estiver presente
            stack.remove(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE);
        }
    }

    /**
     * Método chamado quando o jogador usa (clica com) o item.
     * 
     * Se o jogador estiver agachado e segurando dois disquetes iguais,
     * permite copiar o componente Script da mão principal para a mão secundária.
     * Se o item não possui um script, cria um novo script com ID aleatório.
     * 
     * Abre uma interface de edição do script para o jogador.
     * 
     * @param world mundo onde o evento ocorre
     * @param user jogador que usa o item
     * @param hand mão utilizada para o uso (principal ou secundária)
     * @return resultado da ação (sucesso, passagem, etc)
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
                    // Se ação com a mão principal, retorna sucesso sem copiar
                    return ActionResult.SUCCESS;
                } else if (!off.contains(ModComponents.SCRIPT) && main.contains(ModComponents.SCRIPT)) {
                    // Copia o componente Script da mão principal para a secundária
                    off.applyComponentsFrom(main.getComponents());
                    return ActionResult.SUCCESS;
                }
            }
        }

        // Pega o stack usado na mão clicada
        var stack = user.getStackInHand(hand);

        // Se o item não contém Script, cria um novo Script e adiciona ao componente
        if (!stack.contains(ModComponents.SCRIPT)) {
            var newScript = new Script(RandomId.genRandom(world.getRandom()));
            stack.set(ModComponents.SCRIPT, new ScriptComponent(newScript));
        }

        // Determina o slot do inventário onde o script está sendo editado
        int slot = hand == Hand.MAIN_HAND ? user.getInventory().getSelectedSlot() : 40;

        // Obtém o conteúdo de texto do item no slot do inventário
        var content = TextContent.get(user.getInventory().getStack(slot));

        if (content == null) {
            // Se não houver conteúdo, retorna sucesso sem abrir GUI
            return ActionResult.SUCCESS;
        }

        // Incrementa estatística de uso do item para o jogador
        user.incrementStat(Stats.USED.getOrCreateStat(this));

        // Envia pacote para abrir a interface de edição do script para o jogador
        var payload = new OpenScreenS2CPayload(slot, content.text());
        ServerPlayNetworking.send((ServerPlayerEntity) user, payload);

        return ActionResult.SUCCESS;
    }
}
