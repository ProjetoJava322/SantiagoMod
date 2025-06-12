package org.amemeida.santiago.santiaguita.encubadora;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.amemeida.santiago.registry.blocks.ModBlockEntities;

/**
 * @see net.minecraft.block.entity.FurnaceBlockEntity
 * @see net.minecraft.block.entity.SmokerBlockEntity
 *
 * @see net.minecraft.inventory.EnderChestInventory
 * @see net.minecraft.block.entity.EnderChestBlockEntity
 * @see net.minecraft.block.EnderChestBlock
 */

public class IncubatorBlockEntity extends AbstractFurnaceBlockEntity {
    public IncubatorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.INCUBATOR, pos, state, RecipeType.SMELTING);
    }

    @Override
    public Text getContainerName() {
        return Text.translatable(getCachedState().getBlock().getTranslationKey());
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new IncubatorScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }
}
