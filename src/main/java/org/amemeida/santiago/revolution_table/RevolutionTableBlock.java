package org.amemeida.santiago.revolution_table;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.amemeida.santiago.registry.blocks.ModBlockEntities;
import org.jetbrains.annotations.Nullable;

/**
 * Bloco da Revolution Table com entidade de bloco associada.
 * Define forma, interação, criação de entidade e comportamento de tick.
 */
public class RevolutionTableBlock extends BlockWithEntity {

    /** Codec para serialização do bloco. */
    public static final MapCodec<RevolutionTableBlock> CODEC = createCodec(RevolutionTableBlock::new);

    /**
     * Retorna o codec do bloco.
     * @return codec de serialização
     */
    public MapCodec<RevolutionTableBlock> getCodec() {
        return CODEC;
    }

    /**
     * Construtor do bloco com as configurações fornecidas.
     * @param settings configurações do bloco
     */
    public RevolutionTableBlock(Settings settings) {
        super(settings);
    }

    /**
     * Define a forma do contorno do bloco para colisão e renderização.
     * @param state estado atual do bloco
     * @param view visão do mundo
     * @param pos posição do bloco
     * @param context contexto da forma
     * @return forma do contorno do bloco
     */
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(0f, 0f, 0f, 1f, 0.96875f, 1f);
    }

    /**
     * Ação ao usar o bloco (clicar com a mão).
     * Abre a tela associada ao bloco no lado servidor.
     * @param state estado do bloco
     * @param world mundo onde o bloco está
     * @param pos posição do bloco
     * @param player jogador que usou o bloco
     * @param hit resultado do clique
     * @return resultado da ação
     */
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient) {
            player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
        }
        return ActionResult.SUCCESS;
    }

    /**
     * Retorna o título traduzido do bloco para exibição.
     * @return título do bloco
     */
    public Text getTitle() {
        return Text.translatable(getTranslationKey());
    }

    /**
     * Cria a entidade de bloco associada a este bloco.
     * @param pos posição do bloco
     * @param state estado do bloco
     * @return nova entidade de bloco
     */
    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new RevolutionTableBlockEntity(pos, state);
    }

    /**
     * Define o tipo de renderização do bloco (modelo).
     * @param state estado do bloco
     * @return tipo de renderização
     */
    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    /**
     * Ação ao usar o bloco com um item na mão.
     * Abre a interface do bloco se aplicável.
     * @param stack item usado
     * @param state estado do bloco
     * @param world mundo onde o bloco está
     * @param pos posição do bloco
     * @param player jogador que usou o item
     * @param hand mão usada
     * @param hit resultado do clique
     * @return resultado da ação
     */
    @Override
    protected ActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos,
                                        PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            NamedScreenHandlerFactory screenHandlerFactory = ((RevolutionTableBlockEntity) world.getBlockEntity(pos));
            if (screenHandlerFactory != null) {
                player.openHandledScreen(screenHandlerFactory);
            }
        }
        return ActionResult.SUCCESS;
    }

    /**
     * Retorna o ticker para a entidade de bloco, chamado a cada tick do mundo.
     * @param world mundo onde está o bloco
     * @param state estado do bloco
     * @param type tipo da entidade de bloco
     * @param <T> tipo genérico da entidade de bloco
     * @return ticker para atualização ou null se lado cliente
     */
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if(world.isClient()) {
            return null;
        }
        return validateTicker(type, ModBlockEntities.REVOLUTION_TABLE,
                (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1));
    }
}

