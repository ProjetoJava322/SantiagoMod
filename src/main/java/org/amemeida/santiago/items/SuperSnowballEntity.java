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

public class SuperSnowballEntity extends ThrownItemEntity {
    public SuperSnowballEntity(World world, LivingEntity owner, ItemStack stack) {
        super(ModEntities.SUPER_SNOWBALL, owner, world, stack);
    }

    public SuperSnowballEntity(World world, Position pos, ItemStack stack) {
        super(ModEntities.SUPER_SNOWBALL, pos.getX(), pos.getY(), pos.getZ(), world, stack);
    }

    public SuperSnowballEntity(EntityType<? extends SuperSnowballEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);

        Entity entity = entityHitResult.getEntity();
        int i = entity instanceof BlazeEntity ? 3 : 1;

        if (getWorld() instanceof ServerWorld serverWorld) {
            entity.damage(serverWorld, this.getDamageSources().thrown(this, this.getOwner()), i);
        }
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);

        if (!this.getWorld().isClient) {
            this.getWorld().sendEntityStatus(this, EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES);

            var lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, getWorld());
            lightning.setPosition(hitResult.getPos());
            getWorld().spawnEntity(lightning);

            this.discard();
        }
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.SUPER_SNOWBALL;
    }
}
