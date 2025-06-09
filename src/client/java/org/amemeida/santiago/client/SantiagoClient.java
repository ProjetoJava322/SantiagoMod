package org.amemeida.santiago.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.entity.FlyingItemEntity;
import org.amemeida.santiago.registry.ModEntities;

public class SantiagoClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityRenderer.render();
    }
}
