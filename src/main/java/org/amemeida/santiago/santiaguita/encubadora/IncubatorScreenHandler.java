package org.amemeida.santiago.santiaguita.encubadora;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.RecipePropertySet;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.book.RecipeBookType;
import net.minecraft.screen.AbstractFurnaceScreenHandler;
import net.minecraft.screen.PropertyDelegate;
import org.amemeida.santiago.registry.blocks.ModScreenHandlers;

/**
 * @see net.minecraft.screen.FurnaceScreenHandler
 * @see net.minecraft.screen.SmokerScreenHandler
 */

public class IncubatorScreenHandler extends AbstractFurnaceScreenHandler {
    public IncubatorScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(ModScreenHandlers.INCUBATOR_SCREEN_HANDLER, RecipeType.SMELTING, RecipePropertySet.FURNACE_INPUT, RecipeBookType.FURNACE, syncId, playerInventory);
    }

    public IncubatorScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(
                ModScreenHandlers.INCUBATOR_SCREEN_HANDLER,
                RecipeType.SMELTING,
                RecipePropertySet.FURNACE_INPUT,
                RecipeBookType.FURNACE,
                syncId,
                playerInventory,
                inventory,
                propertyDelegate
        );
    }
}
