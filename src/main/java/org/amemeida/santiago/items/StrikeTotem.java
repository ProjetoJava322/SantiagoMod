package org.amemeida.santiago.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import org.amemeida.santiago.registry.items.ModItems;
import org.jetbrains.annotations.Nullable;

/**
 * Classe que representa o item StrikeTotem,
 * um item que concede efeitos de status ao jogador enquanto estiver no inventário.
 */
public class StrikeTotem extends Item {

    /**
     * Construtor que recebe as configurações do item e repassa para a superclasse.
     * 
     * @param settings configurações do item
     */
    public StrikeTotem(Settings settings) {
        super(settings);
    }

    /**
     * Método chamado a cada tick enquanto o item estiver no inventário do jogador.
     * Aplica efeitos de status positivos ao jogador dependendo se o item está
     * na mão principal ou na mão secundária.
     * 
     * @param stack o ItemStack contendo este item
     * @param world o mundo onde o tick ocorre
     * @param entity a entidade que possui o item no inventário (espera-se um jogador)
     * @param slot slot de equipamento onde o item está (pode ser nulo)
     */
    @Override
    public void inventoryTick(ItemStack stack, ServerWorld world, Entity entity, @Nullable EquipmentSlot slot) {
        if (world.isClient) {
            // Não executa lógica no lado cliente
            return;
        }

        if (entity instanceof PlayerEntity player) {
            // Se o jogador estiver segurando o StrikeTotem na mão principal,
            // aplica os efeitos de Força (Strength) e Velocidade (Speed)
            if (player.getMainHandStack().getItem() == ModItems.STRIKE_TOTEM) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 5, 2));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 5, 2));
            }

            // Se o jogador estiver segurando o StrikeTotem na mão secundária,
            // aplica os efeitos de Regeneração (Regeneration) e Resistência (Resistance)
            if (player.getOffHandStack().getItem() == ModItems.STRIKE_TOTEM) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 5, 2));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 5, 2));
            }
        }
    }
}
