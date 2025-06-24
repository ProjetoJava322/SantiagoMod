package org.amemeida.santiago.registry.blocks;

import org.amemeida.santiago.incubator.IncubatorScreenHandler;
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
import org.amemeida.santiago.computer.ComputerEntity;
import org.amemeida.santiago.computer.ComputerScreenHandler;
import org.amemeida.santiago.revolution_table.RevolutionTableScreenHandler;

/**
 * Classe responsável pelo registro dos tipos de ScreenHandler customizados do mod.
 * Cada tipo de tela é registrado com um identificador e, quando necessário, com um codec para dados estendidos.
 */
public class ModScreenHandlers {

    /**
     * Tipo de ScreenHandler para a Incubadora.
     */
    public static final ScreenHandlerType<IncubatorScreenHandler> INCUBATOR_SCREEN_HANDLER = register("incubator",
            IncubatorScreenHandler::new, BlockPos.PACKET_CODEC);

    /**
     * Tipo de ScreenHandler para a Mesa de Revolução.
     */
    public static final ScreenHandlerType<RevolutionTableScreenHandler> REVOLUTION_TABLE_SCREEN_HANDLER = register("revolution_table",
            RevolutionTableScreenHandler::new, BlockPos.PACKET_CODEC);

    /**
     * Tipo de ScreenHandler para o Computador.
     */
    public static final ScreenHandlerType<ComputerScreenHandler> COMPUTER_SCREEN_HANDLER = register("computer",
            ComputerScreenHandler::new, ComputerEntity.ComputerData.PACKET_CODEC);

    /**
     * Registra um ScreenHandler simples com fábrica fornecida.
     *
     * @param name    nome identificador do screen handler
     * @param factory fábrica para criar a instância do screen handler
     * @param <T>     tipo do ScreenHandler
     * @return tipo registrado do ScreenHandler
     */
    public static <T extends ScreenHandler> ScreenHandlerType<T> register(String name, ScreenHandlerType.Factory<T> factory) {
        return Registry.register(Registries.SCREEN_HANDLER, Identifier.of(Santiago.MOD_ID, name),
                new ScreenHandlerType<>(factory, FeatureFlags.VANILLA_FEATURES));
    }

    /**
     * Registra um ScreenHandler estendido com fábrica e codec para dados adicionais.
     *
     * @param name    nome identificador do screen handler
     * @param factory fábrica para criar a instância do screen handler
     * @param codec   codec para serialização dos dados estendidos
     * @param <T>     tipo do ScreenHandler
     * @param <D>     tipo dos dados estendidos
     * @return tipo registrado do ScreenHandler estendido
     */
    public static <T extends ScreenHandler, D> ExtendedScreenHandlerType<T, D>
    register(String name, ExtendedScreenHandlerType.ExtendedFactory<T, D> factory, PacketCodec<? super RegistryByteBuf, D> codec) {
        return Registry.register(Registries.SCREEN_HANDLER, Identifier.of(Santiago.MOD_ID, name),
                new ExtendedScreenHandlerType<>(factory, codec));
    }

    /**
     * Método de inicialização vazio para garantir o carregamento da classe.
     */
    public static void initialize() {

    }
}

