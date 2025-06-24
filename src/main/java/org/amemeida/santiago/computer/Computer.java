package org.amemeida.santiago.computer;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.block.WireOrientation;
import org.amemeida.santiago.exceptions.RunningException;
import org.amemeida.santiago.file.runner.PythonRunner;
import org.jetbrains.annotations.Nullable;

/**
 * @see net.minecraft.item.Items
 * @see net.minecraft.block.Blocks
 * @see net.minecraft.block.DispenserBlock
 *
 * @see net.minecraft.block.CrafterBlock
 */

/**
 * Representa o bloco "Computer" que funciona como uma máquina programável com estados e interação com redstone.
 * O bloco pode estar em vários estados (idle, running, locked, sucesso, falha, erro) e responde a sinais de redstone.
 */
public class Computer extends BlockWithEntity {

    /** Propriedade que armazena o estado atual do computador. */
    public static final EnumProperty<ComputerState> STATE = EnumProperty.of("state", ComputerState.class);

    /** Propriedade que armazena a direção para qual o computador está virado. */
    public static final EnumProperty<Direction> FACING_COMPUTER = FacingBlock.FACING;

    /**
     * Enum que define os estados possíveis do computador, cada um com sua lógica para o próximo estado.
     */
    public static enum ComputerState implements StringIdentifiable {
        /**
         * Estado inativo (idle). Se receber sinal de redstone, passa para RUNNING.
         */
        IDLE(0) {
            @Override
            public ComputerState next(BlockState state, World world, BlockPos pos) {
                if (world.isReceivingRedstonePower(pos)) {
                    world.scheduleBlockTick(pos, state.getBlock(), 4);
                    return RUNNING;
                } else {
                    return IDLE;
                }
            }
        },
        /**
         * Estado bloqueado, permanece bloqueado enquanto houver redstone, caso contrário volta para IDLE.
         */
        LOCKED(0) {
            @Override
            public ComputerState next(BlockState state, World world, BlockPos pos) {
                return world.isReceivingRedstonePower(pos) ? LOCKED : IDLE;
            }
        },
        /**
         * Estado em execução. O método next não deve ser chamado nesse estado (lança exceção).
         */
        RUNNING(5) {
            @Override
            public ComputerState next(BlockState state, World world, BlockPos pos) {
                throw new IllegalStateException();
            }
        },
        /**
         * Estado de sucesso, aguarda sinal de redstone para passar para LOCKED ou volta para IDLE.
         */
        SUCCESS(15) {
            @Override
            public ComputerState next(BlockState state, World world, BlockPos pos) {
                return world.isReceivingRedstonePower(pos) ? ComputerState.LOCKED : ComputerState.IDLE;
            }
        },
        /**
         * Estado de falha, aguarda sinal de redstone para passar para LOCKED ou volta para IDLE.
         */
        FAILURE(10) {
            @Override
            public ComputerState next(BlockState state, World world, BlockPos pos) {
                return world.isReceivingRedstonePower(pos) ? ComputerState.LOCKED : ComputerState.IDLE;
            }
        },
        /**
         * Estado de erro, aguarda sinal de redstone para passar para LOCKED ou volta para IDLE.
         */
        ERROR(1) {
            @Override
            public ComputerState next(BlockState state, World world, BlockPos pos) {
                return world.isReceivingRedstonePower(pos) ? ComputerState.LOCKED : ComputerState.IDLE;
            }
        };

        /** Valor de potência de redstone associada ao estado. */
        public final int redstone;

        /**
         * Retorna o próximo estado do computador com base no estado atual, mundo e posição.
         * @param state Estado atual do bloco
         * @param world Mundo onde o bloco está
         * @param pos Posição do bloco
         * @return Próximo estado do computador
         */
        public abstract ComputerState next(BlockState state, World world, BlockPos pos);

        private ComputerState(int redstone) {
            this.redstone = redstone;
        }

        @Override
        public String asString() {
            return this.name().toLowerCase();
        }
    }

    /** Codec para serialização/deserialização do bloco. */
    public static final MapCodec<Computer> CODEC = createCodec(Computer::new);

    /**
     * Retorna o codec deste bloco.
     * @return Codec do bloco
     */
    public MapCodec<Computer> getCodec() {
        return CODEC;
    }

    /**
     * Construtor do bloco Computer.
     * Inicializa o estado padrão como IDLE e direção para Norte.
     * @param settings Configurações do bloco
     */
    public Computer(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(STATE, ComputerState.IDLE)
                .with(FACING_COMPUTER, Direction.NORTH));
    }

    /**
     * Adiciona as propriedades (STATE e FACING) ao gerenciador de estados do bloco.
     */
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(STATE);
        builder.add(FACING_COMPUTER);
    }

    /**
     * Define o estado do bloco no momento da colocação, ajustando a direção conforme o olhar do jogador.
     */
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING_COMPUTER, ctx.getPlayerLookDirection());
    }

    /**
     * Evento de interação do jogador com o bloco. Abre a interface gráfica no lado servidor.
     */
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient) {
            player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
        }
        return ActionResult.SUCCESS;
    }

    /**
     * Cria a entidade do bloco (block entity) associada a este bloco.
     */
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ComputerEntity(pos, state);
    }

    /**
     * Método para iniciar a execução do computador, alterando o estado para RUNNING caso esteja IDLE e haja disco presente.
     * Agenda um tick para a execução.
     */
    public void trigger(BlockState state, World world, BlockPos pos) {
        var currentState = state.get(STATE);

        if (currentState != ComputerState.IDLE) {
            return;
        }

        var entity = (ComputerEntity) world.getBlockEntity(pos);

        if (entity == null || !entity.hasDisk()) {
            return;
        }

        world.setBlockState(pos, state.with(STATE, ComputerState.RUNNING), Block.NOTIFY_LISTENERS);
        world.scheduleBlockTick(pos, state.getBlock(), 4);
    }

    /**
     * Atualiza o estado do bloco em resposta a mudanças em blocos vizinhos.
     * Se o estado for IDLE ou LOCKED, atualiza para o próximo estado conforme lógica do estado atual.
     */
    @Override
    protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock,
                                  @Nullable WireOrientation wireOrientation, boolean notify) {
        var curr = state.get(STATE);

        if (curr == ComputerState.IDLE || curr == ComputerState.LOCKED) {
            world.setBlockState(pos, state.with(STATE, curr.next(state, world, pos)), Block.NOTIFY_LISTENERS);
        }
    }

    /**
     * Executa a lógica programada a cada tick do bloco.
     * - Se estado for RUNNING, executa testes em uma thread separada e atualiza o estado conforme resultado.
     * - Para outros estados, atualiza o estado conforme a lógica do próximo estado.
     */
    @Override
    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        var curr = state.get(STATE);

        if (curr == ComputerState.IDLE) {
            return;
        }

        if (curr != ComputerState.RUNNING) {
            world.setBlockState(pos, state.with(STATE, curr.next(state, world, pos)), Block.NOTIFY_LISTENERS);
            world.updateNeighbors(pos, state.getBlock());
            return;
        }

        var entity = (ComputerEntity) world.getBlockEntity(pos);
        assert entity != null;
        var cases = entity.testCases(world);
        System.out.println(cases);

        var resultMode = entity.getResult();

        new Thread(() -> {
            try {
                boolean result = switch(resultMode) {
                    case AND -> true;
                    case OR -> false;
                };

                for (var test : cases) {
                    result = switch(resultMode) {
                        case AND -> test.run() && result;
                        case OR -> test.run() || result;
                    };
                }

                if (result) {
                    world.setBlockState(pos, state.with(STATE, ComputerState.SUCCESS), Block.NOTIFY_LISTENERS);
                } else {
                    world.setBlockState(pos, state.with(STATE, ComputerState.FAILURE), Block.NOTIFY_LISTENERS);
                }
            } catch (RunningException e) {
                System.err.println(e.getMessage());
                world.setBlockState(pos, state.with(STATE, ComputerState.ERROR), Block.NOTIFY_LISTENERS);
            } finally {
                world.scheduleBlockTick(pos, this, 4);
                world.updateNeighbors(pos, state.getBlock());
            }
        }).start();
    }

    /**
     * Indica que o bloco suporta saída de sinal para comparador.
     */
    @Override
    protected boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    /**
     * Define a potência do sinal do comparador com base no estado atual do computador.
     */
    @Override
    protected int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return state.get(STATE).redstone;
    }
}

