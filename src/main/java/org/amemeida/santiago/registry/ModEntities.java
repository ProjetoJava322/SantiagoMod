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

public class ModEntities {
    public static final EntityType<SuperSnowballEntity> SUPER_SNOWBALL = register(
            "super_snowball",
            EntityType.Builder.<SuperSnowballEntity>create(SuperSnowballEntity::new, SpawnGroup.MISC)
                    .dropsNothing()
                    .dimensions(0.25F, 0.25F)
                    .maxTrackingRange(4)
                    .trackingTickInterval(10)
    );

    static {

    }

    protected static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> type) {
        var key = RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(Santiago.MOD_ID, name));

        return Registry.register(Registries.ENTITY_TYPE, key, type.build(key));
    }

    public static void initialize() {}
}
