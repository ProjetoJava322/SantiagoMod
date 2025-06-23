package org.amemeida.santiago.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import org.amemeida.santiago.client.screens.ModScreens;
import org.amemeida.santiago.client.screens.TextEditScreen;
import org.amemeida.santiago.net.OpenScreenS2CPayload;

public class SantiagoClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRenderer.render();
        ModScreens.initialize();

        ClientPlayNetworking.registerGlobalReceiver(OpenScreenS2CPayload.ID, (payload, context) -> {
            ItemStack stack = context.player().getInventory().getStack(payload.slot());

            var screen = new TextEditScreen(payload.slot(), stack, payload.text());
            MinecraftClient.getInstance().setScreen(screen);
        });
    }
}
