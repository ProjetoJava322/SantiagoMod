package org.amemeida.santiago.client.datagen.translations;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.sound.SoundEvent;
import org.amemeida.santiago.Santiago;
import org.amemeida.santiago.registry.ModBlocks;
import org.amemeida.santiago.registry.ModItems;
import org.amemeida.santiago.registry.ModSounds;

import java.util.concurrent.CompletableFuture;

public class EnglishGenerator extends Translator {
    public EnglishGenerator(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        // Specifying en_us is optional, as it's the default language code
        super(dataOutput, "en_us", registryLookup);
    }

    @Override
    public void translate() {
        add("itemGroup.santiago", "Santiago Mod");

        add(ModBlocks.CREATURE_BLOCK, "Block of Creature");
        add(ModBlocks.CAT_BLOCK, "Block of Cat");
        add(ModBlocks.MIKU_BLOCK, "Blocksune Miku");
        add(ModBlocks.SANTIAGUITA_ORE, "Santiaguita Ore");

        add(ModItems.HAMSTER, "Hamster");

        add(ModItems.SANTIAGUITA_AXE, "Santiaguita Axe");
        add(ModItems.SANTIAGUITA_HOE, "Santiaguita Hoe");
        add(ModItems.SANTIAGUITA_PICKAXE, "Santiaguita Pickaxe");
        add(ModItems.SANTIAGUITA_SWORD,  "Santiaguita Sword");
        add(ModItems.SANTIAGUITA_SHOVEL, "Santiaguita Shovel");
        add(ModItems.SANTIAGUITA_INGOT, "Santiaguita Ingot");
        add(ModItems.RAW_SANTIAGUITA, "Raw Santiaguita");
        add(ModItems.UNDERLINE, "Downwards Guide");

        add(ModItems.SUPER_SNOWBALL, "Super Snowball");
        add(ModItems.PUNCH_CARD, "Punch Card");

        add(ModSounds.TEMMIE, "Temmie Talking");
    }
}
