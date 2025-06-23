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

public class StrikeTotem extends Item {
    public StrikeTotem(Settings settings) {
        super(settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerWorld world, Entity entity, @Nullable EquipmentSlot slot) {
        if (world.isClient) {
            return;
        }

        if (entity instanceof PlayerEntity player) {
            if (player.getOffHandStack().getItem() == ModItems.STRIKE_TOTEM) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 5, 2));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 5, 2));
            }

            if (player.getMainHandStack().getItem() == ModItems.STRIKE_TOTEM) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 5, 2));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 5, 2));
            }
        }
    }
}
