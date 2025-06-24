package org.amemeida.santiago.items;

import net.minecraft.entity.*;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import org.amemeida.santiago.registry.ModEntities;
import net.minecraft.util.math.Position;
import org.amemeida.santiago.registry.items.ModItems;

/**
 * Entidade que representa a bola de neve lançada pelo item SuperSnowball.
 * É um projétil lançado no mundo que causa dano e efeitos especiais ao atingir algo.
 */
public class SuperSnowballEntity extends ThrownItemEntity {

    /**
     * Construtor usado para criar a entidade com um dono (quem lançou),
     * no mundo especificado e com o item associado.
     * 
     * @param world o mundo onde a entidade existe
     * @param owner a entidade que lançou este projétil
     * @param stack o ItemStack do projétil
     */
    public SuperSnowballEntity(World world, LivingEntity owner, ItemStack stack) {
        super(ModEntities.SUPER_SNOWBALL, owner, world, stack);
    }

    /**
     * Construtor que cria a entidade na posição especificada, com o item associado,
     * dentro do mundo dado.
     * 
     * @param world o mundo onde a entidade será criada
     * @param pos a posição inicial do projétil
     * @param stack o ItemStack do projétil
     */
    public SuperSnowballEntity(World world, Position pos, ItemStack stack) {
        super(ModEntities.SUPER_SNOWBALL, pos.getX(), pos.getY(), pos.getZ(), world, stack);
    }

    /**
     * Construtor padrão usado internamente pelo Minecraft para criar entidades a partir do tipo.
     * 
     * @param entityType o tipo da entidade
     * @param world o mundo onde a entidade existe
     */
    public SuperSnowballEntity(EntityType<? extends SuperSnowballEntity> entityType, World world) {
        super(entityType, world);
    }

    /**
     * Método chamado quando o projétil atinge uma entidade.
     * Causa dano à entidade atingida. Se a entidade for Blaze, causa mais dano.
     * 
     * @param entityHitResult resultado da colisão com a entidade
     */
    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);

        Entity entity = entityHitResult.getEntity();
        int damageAmount = entity instanceof BlazeEntity ? 3 : 1;

        if (getWorld() instanceof ServerWorld serverWorld) {
            entity.damage(serverWorld, this.getDamageSources().thrown(this, this.getOwner()), damageAmount);
        }
    }

    /**
     * Método chamado quando o projétil colide com qualquer objeto (bloco ou entidade).
     * Ao colidir, toca som, cria um raio (Lightning) na posição do impacto
     * e remove a entidade projétil do mundo.
     * 
     * @param hitResult resultado da colisão
     */
    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);

        if (!this.getWorld().isClient) {
            // Reproduz som e partículas de impacto
            this.getWorld().sendEntityStatus(this, EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES);

            // Cria um raio na posição da colisão
            var lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, getWorld());
            lightning.setPosition(hitResult.getPos());
            getWorld().spawnEntity(lightning);

            // Remove o projétil do mundo
            this.discard();
        }
    }

    /**
     * Retorna o item padrão associado a essa entidade (SuperSnowball).
     * 
     * @return o item padrão deste projétil
     */
    @Override
    protected Item getDefaultItem() {
        return ModItems.SUPER_SNOWBALL;
    }
}
