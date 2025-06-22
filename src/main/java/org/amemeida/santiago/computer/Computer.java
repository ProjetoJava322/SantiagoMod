package org.amemeida.santiago.computer;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.block.WireOrientation;
import org.amemeida.santiago.file.runner.PythonRunner;
import org.jetbrains.annotations.Nullable;

/**
 * @see net.minecraft.item.Items
 * @see net.minecraft.block.Blocks
 * @see net.minecraft.block.DispenserBlock
 *
 * @see net.minecraft.block.CrafterBlock
 */

public class Computer extends BlockWithEntity {
    public static final EnumProperty<ComputerState> STATE = EnumProperty.of("state", ComputerState.class);

    public static enum ComputerState implements StringIdentifiable {
        IDLE(0),
        LOCKED(0),
        RUNNING(5),
        SUCCESS(15),
        FAILURE(10),
        ERROR(1);

        public final int redstone;

        private ComputerState(int redstone) {
            this.redstone = redstone;
        }

        @Override
        public String asString() {
            return this.name().toLowerCase();
        }
    }

    public static final MapCodec<Computer> CODEC = createCodec(Computer::new);

    public MapCodec<Computer> getCodec() {
        return CODEC;
    }

    public Computer(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(STATE, ComputerState.IDLE));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(STATE);
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

    @Override
    protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, @Nullable WireOrientation wireOrientation, boolean notify) {
        boolean has_redstone = world.isReceivingRedstonePower(pos);
        var curr_state = state.get(STATE);

        if (has_redstone && curr_state == ComputerState.IDLE) {
            world.setBlockState(pos, state.with(STATE, ComputerState.RUNNING), Block.NOTIFY_LISTENERS);
            world.scheduleBlockTick(pos, this, 4);
        }

        if (!has_redstone && curr_state == ComputerState.LOCKED) {
            world.setBlockState(pos, state.with(STATE, ComputerState.IDLE), Block.NOTIFY_LISTENERS);
        }
    }

    @Override
    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (state.get(STATE) != ComputerState.RUNNING && state.get(STATE) != ComputerState.IDLE) {
            world.setBlockState(pos, state.with(STATE, ComputerState.LOCKED), Block.NOTIFY_LISTENERS);
            world.updateNeighbors(pos, state.getBlock());
            return;
        }

        var entity = (ComputerEntity) world.getBlockEntity(pos);
        assert entity != null;
        var cases = entity.testCases(world);
        System.out.println(cases);

        new Thread(() -> {
            try {
                boolean result = true;

                for (var test : cases) {
                    result = test.run() && result;
                }

                if (result) {
                    world.setBlockState(pos, state.with(STATE, ComputerState.SUCCESS), Block.NOTIFY_LISTENERS);
                } else {
                    world.setBlockState(pos, state.with(STATE, ComputerState.FAILURE), Block.NOTIFY_LISTENERS);
                }
            } catch (PythonRunner.RunningException e) {
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
