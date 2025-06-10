package org.amemeida.santiago.client;

import net.fabricmc.api.ClientModInitializer;

public class SantiagoClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityRenderer.render();
    }
}
