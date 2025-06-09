package org.amemeida.santiago.client.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import org.amemeida.santiago.registry.ModItems;
import org.amemeida.santiago.santiaguita.SantiaguitaMaterial;

import java.util.concurrent.CompletableFuture;

public class TagGenerator extends FabricTagProvider<Item> {
    public TagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.ITEM, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(SantiaguitaMaterial.REPAIRS_SANTIAGUITA)
                .add(ModItems.SANTIAGUITA_INGOT);
    }
}
