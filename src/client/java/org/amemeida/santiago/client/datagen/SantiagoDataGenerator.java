package org.amemeida.santiago.client.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import org.amemeida.santiago.client.datagen.loot.BlockLootGenerator;
import org.amemeida.santiago.client.datagen.loot.ChestLootGenerator;
import org.amemeida.santiago.client.datagen.tags.BlockTagGenerator;
import org.amemeida.santiago.client.datagen.tags.ItemTagGenerator;
import org.amemeida.santiago.client.datagen.translations.EnglishGenerator;
import org.amemeida.santiago.client.datagen.translations.PortugueseGenerator;

public class SantiagoDataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(ItemTagGenerator::new);
        pack.addProvider(BlockTagGenerator::new);

        pack.addProvider(EnglishGenerator::new);
        pack.addProvider(PortugueseGenerator::new);

        pack.addProvider(AdvancementsGenerator::new);
        pack.addProvider(RecipeGenerator::new);

        pack.addProvider(BlockLootGenerator::new);
        pack.addProvider(ChestLootGenerator::new);

        pack.addProvider(ModelGenerator::new);

        pack.addProvider(SoundGenerator::new);
    }
}
