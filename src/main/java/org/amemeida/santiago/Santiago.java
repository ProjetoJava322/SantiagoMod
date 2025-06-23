package org.amemeida.santiago;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import org.amemeida.santiago.components.TextContent;
import org.amemeida.santiago.computer.Computer;
import org.amemeida.santiago.computer.ComputerScreenHandler;
import org.amemeida.santiago.file.Script;
import org.amemeida.santiago.net.OpenScreenS2CPayload;
import org.amemeida.santiago.net.PCDataC2SPayload;
import org.amemeida.santiago.net.TriggerPCC2SPayload;
import org.amemeida.santiago.net.UpdateStackC2SPayload;
import org.amemeida.santiago.registry.*;
import org.amemeida.santiago.registry.blocks.ModBlockEntities;
import org.amemeida.santiago.registry.blocks.ModBlocks;
import org.amemeida.santiago.registry.blocks.ModScreenHandlers;
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
        PayloadTypeRegistry.playC2S().register(PCDataC2SPayload.ID, PCDataC2SPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(TriggerPCC2SPayload.ID, TriggerPCC2SPayload.CODEC);

        ServerLifecycleEvents.SERVER_STARTED.register(Script::setServer);

        ServerPlayNetworking.registerGlobalReceiver(UpdateStackC2SPayload.ID, (payload, context) -> {
            var playerStack = context.player().getInventory().getStack(payload.slot());
            playerStack.applyComponentsFrom(payload.stack().getComponents());

            var component = TextContent.get(playerStack);
            assert component != null;

            Script.setServer(context.server());
            component.setComponent(payload.text(), playerStack);
        });

        ServerPlayNetworking.registerGlobalReceiver(PCDataC2SPayload.ID, (payload, context) -> {
            if (context.player().isSpectator() || context.player().currentScreenHandler.syncId != payload.screenHandlerId()) {
                return;
            }

            var entity = (ComputerScreenHandler) context.player().currentScreenHandler;
            entity.setOutputMode(payload.data().output());
            entity.setResultMode(payload.data().result());
        });

        ServerPlayNetworking.registerGlobalReceiver(TriggerPCC2SPayload.ID, (payload, context) -> {
            var block = context.player().getServerWorld().getBlockState(payload.pos());

           if (block.getBlock() instanceof Computer computer) {
               computer.trigger(block, context.player().getServerWorld(), payload.pos());
           }
        });
    }
}
