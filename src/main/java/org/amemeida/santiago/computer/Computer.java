package org.amemeida.santiago.computer;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
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
import org.jetbrains.annotations.Nullable;

/**
 * Bloco customizado "Computer" com lógica baseada em redstone e execução de scripts/testes.
 * Possui múltiplos estados que representam seu comportamento ao receber sinal de redstone.
 *
 * COMENTARIOS FEITOS POR IA
 *
 * @see net.minecraft.item.Items
 * @see net.minecraft.block.Blocks
 * @see net.minecraft.block.DispenserBlock
 * @see net.minecraft.block.CrafterBlock
 */
public class Computer extends BlockWithEntity {

    /**
     * Propriedade que representa o estado atual do computador.
     */
    public static final EnumProperty<ComputerState> STATE = EnumProperty.of("state", ComputerState.class);

    /**
     * Direção que o bloco está voltado (como pistões ou fornalhas).
     */
    public static final EnumProperty<Direction> FACING_COMPUTER = EnumProperty.of("facing", Direction.class);

    /**
     * Enumeração dos estados possíveis do bloco computador.
     * Cada estado pode definir sua própria transição para o próximo estado.
     */
    public static enum ComputerState implements StringIdentifiable {
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
        LOCKED(0) {
            @Override
            public ComputerState next(BlockState state, World world, BlockPos pos) {
                return world.isReceivingRedstonePower(pos) ? LOCKED : IDLE;
            }
        },
        RUNNING(5) {
            @Override
            public ComputerState next(BlockState state, World world, BlockPos pos) {
                throw new IllegalStateException();
            }
        },
        SUCCESS(15) {
            @Override
            public ComputerState next(BlockState state, World world, BlockPos pos) {
                return world.isReceivingRedstonePower(pos) ? LOCKED : IDLE;
            }
        },
        FAILURE(10) {
            @Override
            public ComputerState next(BlockState state, World world, BlockPos pos) {
                return world.isReceivingRedstonePower(pos) ? LOCKED : IDLE;
            }
        },
        ERROR(1) {
            @Override
            public ComputerState next(BlockState state, World world, BlockPos pos) {
                return world.isReceivingRedstonePower(pos) ? LOCKED : IDLE;
            }
        };

        public final int redstone;

        public abstract ComputerState next(BlockState state, World world, BlockPos pos);

        private ComputerState(int redstone) {
            this.redstone = redstone;
        }

        @Override
        public String asString() {
            return this.name().toLowerCase();
        }
    }

    /**
     * Codec usado para serializar e registrar o bloco no sistema do Minecraft.
     */
    public static final MapCodec<Computer> CODEC = createCodec(Computer::new);

    public MapCodec<Computer> getCodec() {
        return CODEC;
    }

    /**
     * Construtor do bloco computador.
     *
     * @param settings as configurações do bloco (dureza, material etc).
     */
    public Computer(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(STATE, ComputerState.IDLE)
                .with(FACING_COMPUTER, Direction.NORTH));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(STATE);
        builder.add(FACING_COMPUTER);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING_COMPUTER, ctx.getPlayerLookDirection());
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient) {
            player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ComputerEntity(pos, state);
    }

    /**
     * Dispara a execução do computador caso esteja em estado IDLE e possua disco válido.
     *
     * @param state o estado atual do bloco
     * @param world o mundo onde está
     * @param pos   posição do bloco
     */
    public void trigger(BlockState state, World world, BlockPos pos) {
        var currentState = state.get(STATE);

        if (currentState != ComputerState.IDLE) return;

        var entity = (ComputerEntity) world.getBlockEntity(pos);
        if (entity == null || !entity.hasDisk()) return;

        world.setBlockState(pos, state.with(STATE, ComputerState.RUNNING), Block.NOTIFY_LISTENERS);
        world.scheduleBlockTick(pos, state.getBlock(), 4);
    }

    @Override
    protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock,
                                  @Nullable WireOrientation wireOrientation, boolean notify) {
        var curr = state.get(STATE);

        if (curr == ComputerState.IDLE || curr == ComputerState.LOCKED) {
            world.setBlockState(pos, state.with(STATE, curr.next(state, world, pos)), Block.NOTIFY_LISTENERS);
        }
    }

    /**
     * Executado após o bloco entrar em estado RUNNING. Roda os testes associados e atualiza o estado.
     */
    @Override
    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        var curr = state.get(STATE);

        if (curr == ComputerState.IDLE) return;

        if (curr != ComputerState.RUNNING) {
            world.setBlockState(pos, state.with(STATE, curr.next(state, world, pos)), Block.NOTIFY_LISTENERS);
            world.updateNeighbors(pos, state.getBlock());
            return;
        }

        var entity = (ComputerEntity) world.getBlockEntity(pos);
        assert entity != null;

        var cases = entity.testCases(world);
        var resultMode = entity.getResult();

        new Thread(() -> {
            try {
                boolean result = switch (resultMode) {
                    case AND -> true;
                    case OR -> false;
                };

                for (var test : cases) {
                    result = switch (resultMode) {
                        case AND -> test.run() && result;
                        case OR -> test.run() || result;
                    };
                }

                world.setBlockState(pos, state.with(STATE,
                                result ? ComputerState.SUCCESS : ComputerState.FAILURE),
                        Block.NOTIFY_LISTENERS);
            } catch (RunningException e) {
                System.err.println(e.getMessage());
                world.setBlockState(pos, state.with(STATE, ComputerState.ERROR), Block.NOTIFY_LISTENERS);
            } finally {
                world.scheduleBlockTick(pos, this, 4);
                world.updateNeighbors(pos, state.getBlock());
            }
        }).start();
    }

    @Override
    protected boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    protected int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return state.get(STATE).redstone;
    }
}
