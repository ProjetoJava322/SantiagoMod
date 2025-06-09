package org.amemeida.santiago.client.datagen.translations;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;
import org.amemeida.santiago.registry.ModBlocks;
import org.amemeida.santiago.registry.ModItems;

import java.util.concurrent.CompletableFuture;

public class PortugueseGenerator extends Translator {
    public PortugueseGenerator(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        // Specifying en_us is optional, as it's the default language code
        super(dataOutput, "pt_br", registryLookup);
    }

    @Override
    public void translate() {
        add("itemGroup.santiago", "Mod do Santiago");

        add(ModBlocks.CREATURE_BLOCK, "Bloco de Criatura");
        add(ModBlocks.CAT_BLOCK, "Bloco de Gato");
        add(ModBlocks.MIKU_BLOCK, "Blocosune Miku");

        add(ModItems.HAMSTER, "Hamster");

        add(ModItems.SANTIAGUITA_AXE, "Machado de Santiaguita");
        add(ModItems.SANTIAGUITA_HOE, "Enxada de Santiaguita");
        add(ModItems.SANTIAGUITA_PICKAXE, "Picareta de Santiaguita");
        add(ModItems.SANTIAGUITA_SWORD,  "Espada de Santiaguita");
        add(ModItems.SANTIAGUITA_SHOVEL, "Pá de Santiaguita");
        add(ModItems.SANTIAGUITA_INGOT, "Barra de Santiaguita");
        add(ModItems.UNDERLINE, "Guião Baixo");

        add(ModItems.SUPER_SNOWBALL, "Rasenga");
    }
}
