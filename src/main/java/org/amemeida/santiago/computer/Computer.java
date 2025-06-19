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
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.block.WireOrientation;
import org.jetbrains.annotations.Nullable;

/**
 * @see net.minecraft.item.Items
 * @see net.minecraft.block.Blocks
 * @see net.minecraft.block.DispenserBlock
 */

public class Computer extends BlockWithEntity {
    public static final BooleanProperty RUNNING = BooleanProperty.of("running");

    public static final MapCodec<Computer> CODEC = createCodec(Computer::new);

    public MapCodec<Computer> getCodec() {
        return CODEC;
    }

    public Computer(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(RUNNING, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(RUNNING);
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
        boolean already_running = state.get(RUNNING);

        if (has_redstone && !already_running) {
            world.scheduleBlockTick(pos, this, 4);
            world.setBlockState(pos, state.with(RUNNING, true), Block.NOTIFY_LISTENERS);
        } else if (!has_redstone && already_running) {
            world.setBlockState(pos, state.with(RUNNING, false), Block.NOTIFY_LISTENERS);
        }
    }

    @Override
    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        var entity = (ComputerEntity) world.getBlockEntity(pos);
        assert entity != null;
        entity.trigger(world);
    }

    @Override
    protected boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    protected int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return state.get(RUNNING) ? 15 : 0;
    }
}
