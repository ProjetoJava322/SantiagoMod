package org.amemeida.santiago.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import org.amemeida.santiago.client.screens.ModScreens;
import org.amemeida.santiago.client.screens.TextEditScreen;
import org.amemeida.santiago.net.OpenScreenS2CPayload;

/**
 * Inicializador do lado cliente para o mod Santiago.
 * Registra renderizadores de entidades, inicializa telas e trata pacotes de rede do cliente.
 */
public class SantiagoClient implements ClientModInitializer {

    /**
     * Método chamado na inicialização do cliente.
     * Configura renderizadores, telas e escuta pacotes de rede para abrir telas personalizadas.
     */
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
