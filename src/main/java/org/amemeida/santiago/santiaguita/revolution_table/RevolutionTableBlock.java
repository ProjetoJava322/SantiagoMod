package org.amemeida.santiago.santiaguita.revolution_table;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RevolutionTableBlock extends Block {
    public static final MapCodec<RevolutionTableBlock> CODEC = createCodec(RevolutionTableBlock::new);

    public MapCodec<RevolutionTableBlock> getCodec() {
        return CODEC;
    }

    public RevolutionTableBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient) {
            player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
        }
        return ActionResult.SUCCESS;
    }

    public Text getTitle() {
        return Text.translatable(getTranslationKey());
    }

    //screen handler
}
