package org.amemeida.santiago.client;

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import org.amemeida.santiago.registry.ModEntities;

public class EntityRenderer {
    public static void render() {
        EntityRendererRegistry.register(ModEntities.SUPER_SNOWBALL, FlyingItemEntityRenderer::new);
    }
}
