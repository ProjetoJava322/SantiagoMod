package org.amemeida.santiago.items;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.amemeida.santiago.components.TextContent;
import org.amemeida.santiago.net.OpenScreenS2CPayload;

/**
 * Classe que representa o item PunchCard, um tipo básico de cartão de
 * processamento ou armazenamento utilizado para scripts ou dados.
 */
public class PunchCard extends Item {

    /**
     * Construtor que recebe as configurações do item e repassa para a superclasse.
     * 
     * @param settings configurações do item
     */
    public PunchCard(Item.Settings settings) {
        super(settings);
    }

    /**
     * Método chamado quando o jogador usa (clica com) o item.
     * 
     * Se o jogador estiver agachado e segurando dois PunchCards iguais,
     * permite copiar os componentes do PunchCard da mão principal para a mão secundária.
     * 
     * Caso contrário, tenta abrir a interface de edição de conteúdo do PunchCard,
     * enviando um pacote ao jogador com o conteúdo textual armazenado no item.
     * 
     * @param world mundo onde o evento ocorre
     * @param user jogador que usa o item
     * @param hand mão utilizada para o uso (principal ou secundária)
     * @return resultado da ação (sucesso, passagem, etc)
     */
    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient) {
            // No cliente, apenas passa a ação sem executar
            return ActionResult.PASS;
        }

        if (user.isSneaking()) {
            // Se o jogador estiver agachado
            ItemStack main = user.getMainHandStack();
            ItemStack off = user.getOffHandStack();

            // Verifica se o item na mão principal é igual ao da mão secundária
            if (main.getItem() == off.getItem()) {
                if (hand == Hand.MAIN_HAND) {
                    // Se ação foi com a mão principal, apenas retorna sucesso
                    return ActionResult.SUCCESS;
                } else {
                    // Copia todos os componentes do item da mão principal para a mão secundária
                    off.applyComponentsFrom(main.getComponents());
                    return ActionResult.SUCCESS;
                }
            }
        }

        // Determina o slot do inventário dependendo da mão usada (principal ou secundária)
        int slot = hand == Hand.MAIN_HAND ? user.getInventory().getSelectedSlot() : 40;

        // Obtém o conteúdo textual do item no slot selecionado
        var content = TextContent.get(user.getInventory().getStack(slot));

        if (content == null) {
            // Se não existir conteúdo, retorna sucesso sem abrir GUI
            return ActionResult.SUCCESS;
        }

        // Incrementa a estatística de uso do item para o jogador
        user.incrementStat(Stats.USED.getOrCreateStat(this));

        // Cria e envia o pacote para abrir a interface de edição do conteúdo para o jogador
        var payload = new OpenScreenS2CPayload(slot, content.text());
        ServerPlayNetworking.send((ServerPlayerEntity) user, payload);

        return ActionResult.SUCCESS;
    }
}
