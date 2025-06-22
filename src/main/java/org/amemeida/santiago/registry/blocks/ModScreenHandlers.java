package org.amemeida.santiago.registry.blocks;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.amemeida.santiago.Santiago;
import org.amemeida.santiago.computer.ComputerScreenHandler;
import org.amemeida.santiago.santiaguita.encubadora.IncubatorScreenHandler;
import org.amemeida.santiago.santiaguita.revolution_table.RevolutionTableScreenHandler;

public class ModScreenHandlers {
    public static final ScreenHandlerType<IncubatorScreenHandler> INCUBATOR_SCREEN_HANDLER = register("incubator",
            IncubatorScreenHandler::new);
    public static final ScreenHandlerType<RevolutionTableScreenHandler> REVOLUTION_TABLE_SCREEN_HANDLER = register("revolution_table",
            RevolutionTableScreenHandler::new, BlockPos.PACKET_CODEC);
    public static final ScreenHandlerType<ComputerScreenHandler> COMPUTER_SCREEN_HANDLER = register("computer",
            ComputerScreenHandler::new, ComputerScreenHandler.ComputerData.PACKET_CODEC);

    public static <T extends ScreenHandler> ScreenHandlerType<T> register(String name, ScreenHandlerType.Factory<T> factory) {
        return Registry.register(Registries.SCREEN_HANDLER, Identifier.of(Santiago.MOD_ID, name),
                new ScreenHandlerType<>(factory, FeatureFlags.VANILLA_FEATURES));
    }

    public static <T extends ScreenHandler, D> ExtendedScreenHandlerType<T, D>
    register(String name, ExtendedScreenHandlerType.ExtendedFactory<T, D> factory, PacketCodec<? super RegistryByteBuf, D> codec) {
        return Registry.register(Registries.SCREEN_HANDLER, Identifier.of(Santiago.MOD_ID, name),
                new ExtendedScreenHandlerType<>(factory, codec));
    }

    public static void initialize() {

    }
}
