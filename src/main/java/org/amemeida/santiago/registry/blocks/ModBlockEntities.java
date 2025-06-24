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

/**
 * Classe responsável pelo registro dos tipos de BlockEntity customizadas do mod.
 * Cada BlockEntityType é registrado com um identificador único e associado ao respectivo bloco.
 */
public class ModBlockEntities {

    /**
     * BlockEntityType para a incubadora, vinculada ao bloco ModBlocks.INCUBATOR.
     */
    public static final BlockEntityType<IncubatorBlockEntity> INCUBATOR = register(
            "incubator",
            FabricBlockEntityTypeBuilder.create(IncubatorBlockEntity::new, ModBlocks.INCUBATOR).build());

    /**
     * BlockEntityType para a mesa de revolução, vinculada ao bloco ModBlocks.REVOLUTION_TABLE.
     */
    public static final BlockEntityType<RevolutionTableBlockEntity> REVOLUTION_TABLE = register(
            "revolution_table",
            FabricBlockEntityTypeBuilder.create(RevolutionTableBlockEntity::new, ModBlocks.REVOLUTION_TABLE).build());

    /**
     * BlockEntityType para o computador, vinculada ao bloco ModBlocks.COMPUTER.
     */
    public static final BlockEntityType<ComputerEntity> COMPUTER = register(
            "computer",
            FabricBlockEntityTypeBuilder.create(ComputerEntity::new, ModBlocks.COMPUTER).build());

    /**
     * Método genérico para registrar um BlockEntityType no registro do Minecraft.
     *
     * @param name      nome único do BlockEntityType no formato string
     * @param blockEntity instância do BlockEntityType a ser registrada
     * @param <T>       tipo do BlockEntityType
     * @return a instância registrada do BlockEntityType
     */
    protected static <T extends BlockEntityType<?>> T register(String name, T blockEntity) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(Santiago.MOD_ID, name), blockEntity);
    }

    /**
     * Método para inicializar o registro dos BlockEntities.
     * Deve ser chamado durante o setup do mod para garantir o registro.
     */
    public static void initialize() {}
}

