package com.kingtyphon.kaijucraft.entity;

import com.kingtyphon.kaijucraft.KaijuCraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import org.lwjgl.BufferUtils;

import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public class ProjectileEntity extends Entity implements IEntityAdditionalSpawnData {
    protected LivingEntity shooter;
    protected int shooterId;
    protected int life;
    private ItemStack weapon = ItemStack.EMPTY;


    public ProjectileEntity(EntityType<? extends Entity> entityType, Level worldIn)
    {
        super(entityType, worldIn);
    }
    public ProjectileEntity(EntityType<?> pEntityType, Level pLevel, LivingEntity shooter, Level worldIn, ItemStack weapon) {
        this(pEntityType, worldIn);
        this.shooterId = shooter.getId();
        this.shooter = shooter;


        Vec3 dir = this.getDirection(shooter, weapon);
        double speed = 1;
        this.setDeltaMovement(dir.x * speed, dir.y * speed, dir.z * speed);
        this.updateHeading();

    }
    public void updateHeading()
    {
        double horizontalDistance = this.getDeltaMovement().horizontalDistance();
        this.setYRot((float) (Mth.atan2(this.getDeltaMovement().x(), this.getDeltaMovement().z()) * (180D / Math.PI)));
        this.setXRot((float) (Mth.atan2(this.getDeltaMovement().y(), horizontalDistance) * (180D / Math.PI)));
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
    }
    protected void onHitBlock(BlockState state, BlockPos pos, Direction face, double x, double y, double z)
    {
        //add personal packet
        //PacketHandler.getPlayChannel().sendToTrackingChunk(() -> this.level.getChunkAt(pos), new S2CMessageProjectileHitBlock(x, y, z, pos, face));
    }
    @Override
    protected void readAdditionalSaveData(CompoundTag compound)
    {
        this.life = compound.getInt("MaxLife");
    }
    @Override
    protected void addAdditionalSaveData(CompoundTag compound)
    {
        compound.putInt("MaxLife", this.life);
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer)
    {
        buffer.writeInt(this.shooterId);
        //BufferwriteItemStackToBufIgnoreTag(buffer, this.item);
        buffer.writeVarInt(this.life);
    }
    private Vec3 getVectorFromRotation(float pitch, float yaw)
    {
        float f = Mth.cos(-yaw * 0.017453292F - (float) Math.PI);
        float f1 = Mth.sin(-yaw * 0.017453292F - (float) Math.PI);
        float f2 = -Mth.cos(-pitch * 0.017453292F);
        float f3 = Mth.sin(-pitch * 0.017453292F);
        return new Vec3((double) (f1 * f2), (double) f3, (double) (f * f2));
    }
    public LivingEntity getShooter()
    {
        return this.shooter;
    }
    public Entity getOwner() {
        return this.shooter;
    }
    private Vec3 getDirection(LivingEntity shooter, ItemStack weapon)
    {

        return this.getVectorFromRotation(shooter.getXRot(), shooter.getYRot());
    }
    @Override
    protected void defineSynchedData() {

    }

    @Override
    public void readSpawnData(FriendlyByteBuf friendlyByteBuf) {

    }
    private static <T> T performRayTrace(ClipContext context, BiFunction<ClipContext, BlockPos, T> hitFunction, Function<ClipContext, T> p_217300_2_)
    {
        Vec3 startVec = context.getFrom();
        Vec3 endVec = context.getTo();
        if(startVec.equals(endVec))
        {
            return p_217300_2_.apply(context);
        }
        else
        {
            double startX = Mth.lerp(-0.0000001, endVec.x, startVec.x);
            double startY = Mth.lerp(-0.0000001, endVec.y, startVec.y);
            double startZ = Mth.lerp(-0.0000001, endVec.z, startVec.z);
            double endX = Mth.lerp(-0.0000001, startVec.x, endVec.x);
            double endY = Mth.lerp(-0.0000001, startVec.y, endVec.y);
            double endZ = Mth.lerp(-0.0000001, startVec.z, endVec.z);
            int blockX = Mth.floor(endX);
            int blockY = Mth.floor(endY);
            int blockZ = Mth.floor(endZ);
            BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos(blockX, blockY, blockZ);
            T t = hitFunction.apply(context, mutablePos);
            if(t != null)
            {
                return t;
            }

            double deltaX = startX - endX;
            double deltaY = startY - endY;
            double deltaZ = startZ - endZ;
            int signX = Mth.sign(deltaX);
            int signY = Mth.sign(deltaY);
            int signZ = Mth.sign(deltaZ);
            double d9 = signX == 0 ? Double.MAX_VALUE : (double) signX / deltaX;
            double d10 = signY == 0 ? Double.MAX_VALUE : (double) signY / deltaY;
            double d11 = signZ == 0 ? Double.MAX_VALUE : (double) signZ / deltaZ;
            double d12 = d9 * (signX > 0 ? 1.0D - Mth.frac(endX) : Mth.frac(endX));
            double d13 = d10 * (signY > 0 ? 1.0D - Mth.frac(endY) : Mth.frac(endY));
            double d14 = d11 * (signZ > 0 ? 1.0D - Mth.frac(endZ) : Mth.frac(endZ));

            while(d12 <= 1.0D || d13 <= 1.0D || d14 <= 1.0D)
            {
                if(d12 < d13)
                {
                    if(d12 < d14)
                    {
                        blockX += signX;
                        d12 += d9;
                    }
                    else
                    {
                        blockZ += signZ;
                        d14 += d11;
                    }
                }
                else if(d13 < d14)
                {
                    blockY += signY;
                    d13 += d10;
                }
                else
                {
                    blockZ += signZ;
                    d14 += d11;
                }

                T t1 = hitFunction.apply(context, mutablePos.set(blockX, blockY, blockZ));
                if(t1 != null)
                {
                    return t1;
                }
            }

            return p_217300_2_.apply(context);
        }
    }

    private static BlockHitResult rayTraceBlocks(Level world, ClipContext context, Predicate<BlockState> ignorePredicate)
    {
        return performRayTrace(context, (rayTraceContext, blockPos) -> {
            BlockState blockState = world.getBlockState(blockPos);
            if(ignorePredicate.test(blockState)) return null;
            FluidState fluidState = world.getFluidState(blockPos);
            Vec3 startVec = rayTraceContext.getFrom();
            Vec3 endVec = rayTraceContext.getTo();
            VoxelShape blockShape = rayTraceContext.getBlockShape(blockState, world, blockPos);
            BlockHitResult blockResult = world.clipWithInteractionOverride(startVec, endVec, blockPos, blockShape, blockState);
            VoxelShape fluidShape = rayTraceContext.getFluidShape(fluidState, world, blockPos);
            BlockHitResult fluidResult = fluidShape.clip(startVec, endVec, blockPos);
            double blockDistance = blockResult == null ? Double.MAX_VALUE : rayTraceContext.getFrom().distanceToSqr(blockResult.getLocation());
            double fluidDistance = fluidResult == null ? Double.MAX_VALUE : rayTraceContext.getFrom().distanceToSqr(fluidResult.getLocation());
            return blockDistance <= fluidDistance ? blockResult : fluidResult;
        }, (rayTraceContext) -> {
            Vec3 Vector3d = rayTraceContext.getFrom().subtract(rayTraceContext.getTo());
            return BlockHitResult.miss(rayTraceContext.getTo(), Direction.getNearest(Vector3d.x, Vector3d.y, Vector3d.z), BlockPos.containing(rayTraceContext.getTo()));
        });
    }

//    @Override
//    public void tick()
//    {
//        super.tick();
//        this.updateHeading();
//
//        if(!this.level().isClientSide())
//        {
//            Vec3 startVec = this.position();
//            Vec3 endVec = startVec.add(this.getDeltaMovement());
//            HitResult result = rayTraceBlocks(this.level(), new ClipContext(startVec, endVec, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this), );
//            if(result.getType() != HitResult.Type.MISS)
//            {
//                endVec = result.getLocation();
//            }
//
//            List<EntityHitResult> hitEntities = null;
//
//                EntityHitResult entityResult = this.findEntityOnPath(startVec, endVec);
//                if(entityResult != null)
//                {
//                    hitEntities = Collections.singletonList(entityResult);
//                }
//            if(hitEntities != null && hitEntities.size() > 0)
//            {
//                for(EntityResult entityResult : hitEntities)
//                {
//                    result = new ExtendedEntityRayTraceResult(entityResult);
//                    if(((EntityHitResult) result).getEntity() instanceof Player)
//                    {
//                        Player player = (Player) ((EntityHitResult) result).getEntity();
//
//                        if(this.shooter instanceof Player && !((Player) this.shooter).canHarmPlayer(player))
//                        {
//                            result = null;
//                        }
//                    }
//                    if(result != null)
//                    {
//                        this.onHit(result, startVec, endVec);
//                    }
//                }
//            }
//            else
//            {
//                this.onHit(result, startVec, endVec);
//            }
//        }
//            if(this.tickCount >= this.life)
//    {
//        if(this.isAlive())
//        {
//            this.onExpired();
//        }
//        this.remove(RemovalReason.KILLED);
//    }
//
//        private void onHit(HitResult result, Vec3 startVec, Vec3 endVec)
//        {
//            if(MinecraftForge.EVENT_BUS.post(new GunProjectileHitEvent(result, this)))
//            {
//                return;
//            }
//
//            if(result instanceof BlockHitResult blockHitResult)
//            {
//                if(blockHitResult.getType() == HitResult.Type.MISS)
//                {
//                    return;
//                }
//
//                Vec3 hitVec = result.getLocation();
//                BlockPos pos = blockHitResult.getBlockPos();
//                BlockState state = this.level.getBlockState(pos);
//                Block block = state.getBlock();
//
//                if(Config.COMMON.gameplay.griefing.enableGlassBreaking.get() && state.is(ModTags.Blocks.FRAGILE))
//                {
//                    float destroySpeed = state.getDestroySpeed(this.level, pos);
//                    if(destroySpeed >= 0)
//                    {
//                        float chance = Config.COMMON.gameplay.griefing.fragileBaseBreakChance.get().floatValue() / (destroySpeed + 1);
//                        if(this.random.nextFloat() < chance)
//                        {
//                            this.level.destroyBlock(pos, Config.COMMON.gameplay.griefing.fragileBlockDrops.get());
//                        }
//                    }
//                }
//
//                if(!state.getMaterial().isReplaceable())
//                {
//                    this.remove(RemovalReason.KILLED);
//                }
//
//                if(block instanceof IDamageable)
//                {
//                    ((IDamageable) block).onBlockDamaged(this.level, state, pos, this, this.getDamage(), (int) Math.ceil(this.getDamage() / 2.0) + 1);
//                }
//
//                this.onHitBlock(state, pos, blockHitResult.getDirection(), hitVec.x, hitVec.y, hitVec.z);
//
//                if(block instanceof TargetBlock targetBlock)
//                {
//                    int power = ReflectionUtil.updateTargetBlock(targetBlock, this.level, state, blockHitResult, this);
//                    if(this.shooter instanceof ServerPlayer serverPlayer)
//                    {
//                        serverPlayer.awardStat(Stats.TARGET_HIT);
//                        CriteriaTriggers.TARGET_BLOCK_HIT.trigger(serverPlayer, this, blockHitResult.getLocation(), power);
//                    }
//                }
//
//                if(block instanceof BellBlock bell)
//                {
//                    bell.attemptToRing(this.level, pos, blockHitResult.getDirection());
//                }
//
//                int fireStarterLevel = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.FIRE_STARTER.get(), this.weapon);
//                if(fireStarterLevel > 0 && Config.COMMON.gameplay.griefing.setFireToBlocks.get())
//                {
//                    BlockPos offsetPos = pos.relative(blockHitResult.getDirection());
//                    if(BaseFireBlock.canBePlacedAt(this.level, offsetPos, blockHitResult.getDirection()))
//                    {
//                        BlockState fireState = BaseFireBlock.getState(this.level, offsetPos);
//                        this.level.setBlock(offsetPos, fireState, 11);
//                        ((ServerLevel) this.level).sendParticles(ParticleTypes.LAVA, hitVec.x - 1.0 + this.random.nextDouble() * 2.0, hitVec.y, hitVec.z - 1.0 + this.random.nextDouble() * 2.0, 4, 0, 0, 0, 0);
//                    }
//                }
//                return;
//            }
//            protected void onHitEntity(Entity entity, Vec3 hitVec, Vec3 startVec, Vec3 endVec, boolean headshot)
//            {
//                float damage = this.getDamage();
//                float newDamage = this.getCriticalDamage(this.weapon, this.random, damage);
//                boolean critical = damage != newDamage;
//                damage = newDamage;
//
//                if(headshot)
//                {
//                    damage *= Config.COMMON.gameplay.headShotDamageMultiplier.get();
//                }
//
//                DamageSource source = ModDamageTypes.Sources.projectile(this.level.registryAccess(), this, this.shooter);
//                entity.hurt(source, damage);
//
//                if(this.shooter instanceof Player)
//                {
//                    int hitType = critical ? S2CMessageProjectileHitEntity.HitType.CRITICAL : headshot ? S2CMessageProjectileHitEntity.HitType.HEADSHOT : S2CMessageProjectileHitEntity.HitType.NORMAL;
//                    PacketHandler.getPlayChannel().sendToPlayer(() -> (ServerPlayer) this.shooter, new S2CMessageProjectileHitEntity(hitVec.x, hitVec.y, hitVec.z, hitType, entity instanceof Player));
//                }
//
//                /* Send blood particle to tracking clients. */
//                PacketHandler.getPlayChannel().sendToTracking(() -> entity, new S2CMessageBlood(hitVec.x, hitVec.y, hitVec.z));
//            }
//            if(result instanceof ExtendedEntityRayTraceResult entityHitResult)
//            {
//                Entity entity = entityHitResult.getEntity();
//                if(entity.getId() == this.shooterId)
//                {
//                    return;
//                }
//
//                if(this.shooter instanceof Player player)
//                {
//                    if(entity.hasIndirectPassenger(player))
//                    {
//                        return;
//                    }
//                }
//
//                int fireStarterLevel = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.FIRE_STARTER.get(), this.weapon);
//                if(fireStarterLevel > 0)
//                {
//                    entity.setSecondsOnFire(2);
//                }
//
//                this.onHitEntity(entity, result.getLocation(), startVec, endVec, entityHitResult.isHeadshot());
//
//                int collateralLevel = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.COLLATERAL.get(), weapon);
//                if(collateralLevel == 0)
//                {
//                    this.remove(RemovalReason.KILLED);
//                }
//
//                entity.invulnerableTime = 0;
//            }
//        }

    public static class ProjectileHelper {
        public static final float DEFAULT_SHIELD_DISABLE_CHANCE = 0.30f;

        public static boolean handleShieldHit(Entity target, Entity projectile, float damage, float shieldDisableChance) {
            if (!(target instanceof Player player)) {
                return false;
            }

            ItemStack mainHandItem = player.getMainHandItem();
            ItemStack offHandItem = player.getOffhandItem();

            boolean isBlockingMainHand = player.isBlocking() && mainHandItem.getItem() instanceof ShieldItem;
            boolean isBlockingOffHand = player.isBlocking() && offHandItem.getItem() instanceof ShieldItem;

            if (!isBlockingMainHand && !isBlockingOffHand) {
                return false;
            }

            ItemStack shield = isBlockingMainHand ? mainHandItem : offHandItem;
            InteractionHand hand = isBlockingMainHand ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;

            if (projectile.level().getRandom().nextFloat() < shieldDisableChance) {
                player.getCooldowns().addCooldown(shield.getItem(), 100);
                player.stopUsingItem();
                player.level().broadcastEntityEvent(player, (byte) 30);

                player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.SHIELD_BREAK, SoundSource.PLAYERS, 1.0F, 0.8F + player.level().getRandom().nextFloat() * 0.4F);
                return false;
            }

            player.hurt(player.damageSources().generic(), 0.5f);
            shield.hurtAndBreak(12, player, (p) -> p.broadcastBreakEvent(hand));

            player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.SHIELD_BLOCK, SoundSource.PLAYERS, 1.0F, 0.8F + player.level().getRandom().nextFloat() * 0.4F);

            return true;
        }

        public static boolean handleShieldHit(Entity target, Entity projectile, float damage) {
            return handleShieldHit(target, projectile, damage, DEFAULT_SHIELD_DISABLE_CHANCE);
        }
    }
}
