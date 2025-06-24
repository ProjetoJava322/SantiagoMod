package org.amemeida.santiago.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;

/**
 * Classe que representa o item SuperSnowball,
 * uma versão melhorada da bola de neve que funciona como projétil.
 */
public class SuperSnowball extends Item implements ProjectileItem {
    
    /** 
     * Constante que define a força do lançamento do projétil.
     */
    public final static float POWER = 1.5F;

    /**
     * Construtor que recebe as configurações do item e repassa para a superclasse.
     * 
     * @param settings configurações do item
     */
    public SuperSnowball(Item.Settings settings) {
        super(settings);
    }

    /**
     * Método chamado quando o jogador usa (lança) o item.
     * Toca o som do lançamento de bola de neve e cria o projétil no mundo servidor,
     * aplicando a velocidade definida pela constante POWER.
     * 
     * Também decrementa a quantidade do item no inventário do jogador, a não ser que ele esteja em modo criativo.
     * 
     * @param world mundo onde o evento ocorre
     * @param user jogador que usa o item
     * @param hand mão utilizada para o uso (principal ou secundária)
     * @return resultado da ação (sucesso, passagem, etc)
     */
    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        // Reproduz o som de lançamento da bola de neve
        world.playSound(
                null,
                user.getX(),
                user.getY(),
                user.getZ(),
                SoundEvents.ENTITY_SNOWBALL_THROW,
                SoundCategory.NEUTRAL,
                0.5F,
                0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F)
        );

        // Se estiver no servidor, cria a entidade do projétil e a lança
        if (world instanceof ServerWorld serverWorld) {
            ProjectileEntity.spawnWithVelocity(
                SuperSnowballEntity::new, 
                serverWorld, 
                itemStack, 
                user, 
                0.0F, 
                POWER, 
                1.0F
            );
        }

        // Incrementa a estatística de uso do item para o jogador
        user.incrementStat(Stats.USED.getOrCreateStat(this));

        // Decrementa a quantidade do item no inventário, exceto se estiver em modo criativo
        itemStack.decrementUnlessCreative(1, user);

        return ActionResult.SUCCESS;
    }

    /**
     * Cria a entidade projétil associada a este item.
     * 
     * @param world mundo onde a entidade será criada
     * @param pos posição inicial da entidade
     * @param stack o ItemStack usado para criar a entidade
     * @param direction direção do projétil
     * @return a nova entidade de projétil (SuperSnowballEntity)
     */
    @Override
    public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
        return new SuperSnowballEntity(world, pos, stack);
    }
}
