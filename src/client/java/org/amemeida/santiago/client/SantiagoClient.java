package org.amemeida.santiago.client;

import net.fabricmc.api.ClientModInitializer;
import org.amemeida.santiago.client.screens.ModScreens;

public class SantiagoClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityRenderer.render();
        ModScreens.initialize();
    }
}
