package org.amemeida.santiago.incubator;

import com.mojang.serialization.MapCodec;

import net.minecraft.block.*;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.amemeida.santiago.registry.blocks.ModBlockEntities;
import org.jetbrains.annotations.Nullable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;

/**
 * Classe que implementa um bloco de incubadora no jogo.
 * A incubadora é um bloco interativo que possui uma entidade de bloco associada
 * e permite a interação do jogador para abrir uma interface.
 * A forma do bloco é reduzida para metade da altura de um bloco normal.
 */
public class IncubatorBlock extends BlockWithEntity {
    /**
     * CODEC para serialização e deserialização do bloco de incubadora.
     */
    public static final MapCodec<IncubatorBlock> CODEC = 
            IncubatorBlock.createCodec(IncubatorBlock::new);

    /**
     * Construtor para o bloco de incubadora.
     * 
     * @param settings As configurações do bloco
     */
    public IncubatorBlock(Settings settings) {
        super(settings);
    }

    /**
     * Define o formato visual do bloco de incubadora.
     * Cria um formato que tem aproximadamente metade da altura de um bloco normal.
     * 
     * @param state O estado do bloco
     * @param view A visualização do bloco
     * @param pos A posição do bloco
     * @param context O contexto da forma
     * @return A forma de contorno do bloco
     */
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(0f, 0f, 0f, 1f, 0.5625f, 1f);
    }

    /**
     * Obtém o CODEC para este bloco.
     * 
     * @return O CODEC para serialização/deserialização
     */
    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    /**
     * Cria uma nova entidade de bloco para este bloco.
     * 
     * @param pos A posição do bloco
     * @param state O estado do bloco
     * @return Uma nova instância de IncubatorBlockEntity
     */
    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new IncubatorBlockEntity(pos, state);
    }

    /**
     * Define o tipo de renderização para este bloco.
     * 
     * @param state O estado do bloco
     * @return O tipo de renderização, que neste caso é MODEL
     */
    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    /**
     * Manipula a interação do jogador com o bloco.
     * Abre a interface da incubadora quando o jogador interage com ela.
     * 
     * @param stack O item na mão do jogador
     * @param state O estado do bloco
     * @param world O mundo do jogo
     * @param pos A posição do bloco
     * @param player O jogador que está interagindo
     * @param hand A mão que o jogador está usando
     * @param hit O resultado do hit no bloco
     * @return O resultado da ação
     */
    @Override
    protected ActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos,
                                         PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            NamedScreenHandlerFactory screenHandlerFactory = (NamedScreenHandlerFactory) world.getBlockEntity(pos);
            if (screenHandlerFactory != null) {
                player.openHandledScreen(screenHandlerFactory);
            }
        }
        return ActionResult.SUCCESS;
    }

    /**
     * Configura o ticker da entidade de bloco.
     * O ticker é responsável por atualizar a lógica da incubadora no servidor.
     * No cliente, não é necessário um ticker.
     * 
     * @param world O mundo do jogo
     * @param state O estado do bloco
     * @param type O tipo da entidade de bloco
     * @return O ticker da entidade de bloco, ou null no cliente
     */
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if(world.isClient()) {
            return null;
        }

        return validateTicker(type, ModBlockEntities.INCUBATOR,
                (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1));
    }
}