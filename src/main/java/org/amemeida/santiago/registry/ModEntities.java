package org.amemeida.santiago.registry;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import org.amemeida.santiago.Santiago;
import org.amemeida.santiago.items.SuperSnowballEntity;

/**
 * Classe responsável pelo registro das entidades personalizadas do mod.
 */
public class ModEntities {

    /**
     * Entidade do tipo SuperSnowball, com configurações específicas de tamanho e rastreamento.
     */
    public static final EntityType<SuperSnowballEntity> SUPER_SNOWBALL = register(
            "super_snowball",
            EntityType.Builder.<SuperSnowballEntity>create(SuperSnowballEntity::new, SpawnGroup.MISC)
                    .dropsNothing()
                    .dimensions(0.25F, 0.25F)
                    .maxTrackingRange(4)
                    .trackingTickInterval(10)
    );

    /**
     * Registra uma entidade no registro do Minecraft com o nome e tipo especificados.
     *
     * @param name Nome identificador da entidade.
     * @param type Builder da entidade a ser registrada.
     * @param <T> Tipo da entidade.
     * @return A entidade registrada.
     */
    protected static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> type) {
        var key = RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(Santiago.MOD_ID, name));
        return Registry.register(Registries.ENTITY_TYPE, key, type.build(key));
    }

    /**
     * Método vazio para forçar a inicialização estática da classe.
     */
    public static void initialize() {}
}
