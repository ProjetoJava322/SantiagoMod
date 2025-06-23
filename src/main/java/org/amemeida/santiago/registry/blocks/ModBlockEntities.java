package org.amemeida.santiago.registry.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.amemeida.santiago.Santiago;
import org.amemeida.santiago.computer.ComputerEntity;
import org.amemeida.santiago.incubator.IncubatorBlockEntity;
import org.amemeida.santiago.revolution_table.RevolutionTableBlockEntity;

public class ModBlockEntities {

    public static final BlockEntityType<IncubatorBlockEntity> INCUBATOR = register( "incubator",
            FabricBlockEntityTypeBuilder.create(IncubatorBlockEntity::new, ModBlocks.INCUBATOR).build());

    public static final BlockEntityType<RevolutionTableBlockEntity> REVOLUTION_TABLE = register("revolution_table",
            FabricBlockEntityTypeBuilder.create(RevolutionTableBlockEntity::new, ModBlocks.REVOLUTION_TABLE).build());

    public static final BlockEntityType<ComputerEntity> COMPUTER = register("computer",
            FabricBlockEntityTypeBuilder.create(ComputerEntity::new, ModBlocks.COMPUTER).build());

    protected static <T extends BlockEntityType<?>> T register(String name, T blockEntity) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(Santiago.MOD_ID, name), blockEntity);
    }

    public static void initialize() {}
}
