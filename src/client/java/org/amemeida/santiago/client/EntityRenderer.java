package org.amemeida.santiago.client;

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import org.amemeida.santiago.registry.ModEntities;

/**
 * Registra os renderizadores de entidades personalizados para o mod.
 */
public class EntityRenderer {

    /**
     * Registra os renderizadores de entidades.
     * Atualmente registra o renderizador da entidade SUPER_SNOWBALL usando FlyingItemEntityRenderer.
     */
    public static void render() {
        EntityRendererRegistry.register(ModEntities.SUPER_SNOWBALL, FlyingItemEntityRenderer::new);
    }
}
