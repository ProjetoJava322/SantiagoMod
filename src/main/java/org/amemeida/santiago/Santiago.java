package org.amemeida.santiago;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import org.amemeida.santiago.file.Script;
import org.amemeida.santiago.net.OpenScreenS2CPayload;
import org.amemeida.santiago.net.UpdateStackC2SPayload;
import org.amemeida.santiago.registry.*;
import org.amemeida.santiago.registry.blocks.ModBlockEntities;
import org.amemeida.santiago.registry.blocks.ModBlocks;
import org.amemeida.santiago.registry.blocks.ModScreenHandlers;
import org.amemeida.santiago.registry.items.ModComponents;
import org.amemeida.santiago.registry.items.ModGroups;
import org.amemeida.santiago.registry.items.ModItems;
import org.amemeida.santiago.registry.recipes.ModRecipeBooks;
import org.amemeida.santiago.registry.recipes.ModRecipeSerializers;
import org.amemeida.santiago.registry.recipes.ModRecipeTypes;

public class Santiago implements ModInitializer {
    public static final String MOD_ID = "santiago";

    @Override
    public void onInitialize() {
        ModEntities.initialize();

        ModBlocks.initialize();
        ModBlockEntities.initialize();
        ModScreenHandlers.initialize();

        ModGroups.initialize();
        ModItems.initialize();

        ModTags.initialize();
        ModSounds.initialize();

        ModRecipeBooks.initialize();
        ModRecipeSerializers.initialize();
        ModRecipeTypes.initialize();
        ModFeatures.initialize();

        PayloadTypeRegistry.playS2C().register(OpenScreenS2CPayload.ID, OpenScreenS2CPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(UpdateStackC2SPayload.ID, UpdateStackC2SPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(UpdateStackC2SPayload.ID, (payload, context) -> {
            var playerStack = context.player().getInventory().getStack(payload.slot());
            playerStack.applyComponentsFrom(payload.stack().getComponents());

            var component = playerStack.get(ModComponents.IO);
            if (component == null) {
                component = playerStack.get(ModComponents.SCRIPT);
            }

            if (component == null) {
                return;
            }

            Script.setServer(context.server());

            component.setComponent(payload.text(), playerStack);
        });
    }
}
