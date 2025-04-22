package iDiamondhunter.bows;

import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentThorns;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet70GameEvent;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityFrostArrow extends Entity implements IProjectile {
   Random random;
   private int xTile = -1;
   private int yTile = -1;
   private int zTile = -1;
   private int inTile = 0;
   private int inData = 0;
   private boolean inGround = false;
   public int canBePickedUp = 0;
   public int arrowShake = 0;
   public Entity shootingEntity;
   private int ticksInGround;
   private int ticksIn;
   private int ticksInAir = 0;
   private double damage = 2.0D;
   private int knockbackStrength;

   public EntityFrostArrow(World var1) {
      super(var1);
      this.setSize(0.5F, 0.5F);
   }

   public EntityFrostArrow(World var1, double var2, double var4, double var6) {
      super(var1);
      this.setSize(0.5F, 0.5F);
      this.setPosition(var2, var4, var6);
      super.yOffset = 0.0F;
   }

   public EntityFrostArrow(World var1, EntityLivingBase var2, EntityLiving var3, float var4, float var5) {
      super(var1);
      this.shootingEntity = var2;
      if (var2 instanceof EntityPlayer) {
         this.canBePickedUp = 1;
      }

      super.posY = var2.posY + (double)var2.getEyeHeight() - 0.10000000149011612D;
      double var6 = var3.posX - var2.posX;
      double var8 = var3.posY + (double)var3.getEyeHeight() - 0.699999988079071D - super.posY;
      double var10 = var3.posZ - var2.posZ;
      double var12 = (double)MathHelper.sqrt_double(var6 * var6 + var10 * var10);
      if (var12 >= 1.0E-7D) {
         float var14 = (float)(Math.atan2(var10, var6) * 180.0D / 3.141592653589793D) - 90.0F;
         float var15 = (float)(-(Math.atan2(var8, var12) * 180.0D / 3.141592653589793D));
         double var16 = var6 / var12;
         double var18 = var10 / var12;
         this.setLocationAndAngles(var2.posX + var16, super.posY, var2.posZ + var18, var14, var15);
         super.yOffset = 0.0F;
         float var20 = (float)var12 * 0.2F;
         this.setThrowableHeading(var6, var8 + (double)var20, var10, var4, var5);
      }

   }

   public EntityFrostArrow(World var1, EntityLivingBase var2, float var3) {
      super(var1);
      this.shootingEntity = var2;
      if (var2 instanceof EntityPlayer) {
         this.canBePickedUp = 1;
      }

      this.setSize(0.5F, 0.5F);
      this.setLocationAndAngles(var2.posX, var2.posY + (double)var2.getEyeHeight(), var2.posZ, var2.rotationYaw, var2.rotationPitch);
      super.posX -= (double)(MathHelper.cos(super.rotationYaw / 180.0F * 3.1415927F) * 0.16F);
      super.posY -= 0.10000000149011612D;
      super.posZ -= (double)(MathHelper.sin(super.rotationYaw / 180.0F * 3.1415927F) * 0.16F);
      this.setPosition(super.posX, super.posY, super.posZ);
      super.yOffset = 0.0F;
      super.motionX = (double)(-MathHelper.sin(super.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(super.rotationPitch / 180.0F * 3.1415927F));
      super.motionZ = (double)(MathHelper.cos(super.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(super.rotationPitch / 180.0F * 3.1415927F));
      super.motionY = (double)(-MathHelper.sin(super.rotationPitch / 180.0F * 3.1415927F));
      this.setThrowableHeading(super.motionX, super.motionY, super.motionZ, var3 * 1.5F, 1.0F);
   }

   protected void entityInit() {
      super.dataWatcher.addObject(16, Byte.valueOf((byte)0));
   }

   public void setThrowableHeading(double var1, double var3, double var5, float var7, float var8) {
      float var9 = MathHelper.sqrt_double(var1 * var1 + var3 * var3 + var5 * var5);
      var1 = var1 / (double)var9;
      var3 = var3 / (double)var9;
      var5 = var5 / (double)var9;
      var1 = var1 + super.rand.nextGaussian() * 0.007499999832361937D * (double)var8;
      var3 = var3 + super.rand.nextGaussian() * 0.007499999832361937D * (double)var8;
      var5 = var5 + super.rand.nextGaussian() * 0.007499999832361937D * (double)var8;
      var1 = var1 * (double)var7;
      var3 = var3 * (double)var7;
      var5 = var5 * (double)var7;
      super.motionX = var1;
      super.motionY = var3;
      super.motionZ = var5;
      float var10 = MathHelper.sqrt_double(var1 * var1 + var5 * var5);
      super.prevRotationYaw = super.rotationYaw = (float)(Math.atan2(var1, var5) * 180.0D / 3.141592653589793D);
      super.prevRotationPitch = super.rotationPitch = (float)(Math.atan2(var3, (double)var10) * 180.0D / 3.141592653589793D);
      this.ticksInGround = 0;
   }

   public static DamageSource causeFrostArrowDamage(EntityFrostArrow var0, Entity var1) {
      return (new EntityDamageSourceIndirect("arrow", var0, var1)).setProjectile(); //.b()
   }

   public void setPositionAndRotation2(double var1, double var3, double var5, float var7, float var8, int var9) {
      this.setPosition(var1, var3, var5);
      this.setRotation(var7, var8);
   }

   public void setVelocity(double var1, double var3, double var5) {
      super.motionX = var1;
      super.motionY = var3;
      super.motionZ = var5;
      if (super.prevRotationPitch == 0.0F && super.prevRotationYaw == 0.0F) {
         float var7 = MathHelper.sqrt_double(var1 * var1 + var5 * var5);
         super.prevRotationYaw = super.rotationYaw = (float)(Math.atan2(var1, var5) * 180.0D / 3.141592653589793D);
         super.prevRotationPitch = super.rotationPitch = (float)(Math.atan2(var3, (double)var7) * 180.0D / 3.141592653589793D);
         super.prevRotationPitch = super.rotationPitch;
         super.prevRotationYaw = super.rotationYaw;
         this.setLocationAndAngles(super.posX, super.posY, super.posZ, super.rotationYaw, super.rotationPitch);
         this.ticksInGround = 4;
      }

   }

   public void onUpdate() {
      super.onUpdate();
      if (super.prevRotationPitch == 0.0F && super.prevRotationYaw == 0.0F) {
         float var1 = MathHelper.sqrt_double(super.motionX * super.motionX + super.motionZ * super.motionZ);
         super.prevRotationYaw = super.rotationYaw = (float)(Math.atan2(super.motionX, super.motionZ) * 180.0D / 3.141592653589793D);
         super.prevRotationPitch = super.rotationPitch = (float)(Math.atan2(super.motionY, (double)var1) * 180.0D / 3.141592653589793D);
      }

      int var16 = super.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);
      if (var16 > 0) {
         Block.blocksList[var16].setBlockBoundsBasedOnState(super.worldObj, this.xTile, this.yTile, this.zTile);
         AxisAlignedBB var2 = Block.blocksList[var16].getCollisionBoundingBoxFromPool(super.worldObj, this.xTile, this.yTile, this.zTile);
         if (var2 != null && var2.isVecInside(super.worldObj.getWorldVec3Pool().getVecFromPool(super.posX, super.posY, super.posZ))) {
            this.inGround = true;
         }
      }

      if (this.arrowShake > 0) {
         --this.arrowShake;
      }

      if (this.inGround) {
         int var3 = super.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);
         int var4 = super.worldObj.getBlockMetadata(this.xTile, this.yTile, this.zTile);
         boolean var5 = false;
         int var6 = super.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);
         int var7 = super.worldObj.getBlockMetadata(this.xTile, this.yTile, this.zTile);
         int var8 = MathHelper.floor_double(super.posX);
         int var9 = MathHelper.floor_double(super.posY);
         int var17 = MathHelper.floor_double(super.posZ);
         boolean var10 = false;
         if (super.worldObj.getBlockMaterial(var8, var9, var17) == Material.water && super.worldObj.getBlockMetadata(var8, var9, var17) == 0) {
            super.worldObj.setBlock(var8, var9, var17, Block.ice.blockID, 0, 3);
            var10 = true;
         }

         if (this.ticksInGround >= 64 && this.ticksInGround <= 65) {
            if (super.worldObj.getBlockId(var8, var9, var17) == 0) {
               super.worldObj.setBlock(var8, var9, var17, Block.snow.blockID, 0, 3);
            }

            if (super.worldObj.getBlockId(var8, var9, var17) == Block.waterStill.blockID) {
               super.worldObj.setBlock(var8, var9, var17, Block.ice.blockID, 0, 3);
            }
         }

         if (var3 == this.inTile && var4 == this.inData) {
            boolean var11 = true;
            ++this.ticksInGround;
            --this.ticksIn;
            if (this.ticksInGround <= 2) {
               super.worldObj.spawnParticle("snowballpoof", super.posX, super.posY, super.posZ, 0.0D, 0.0D, 0.0D);
            }

            this.setSize(0.1F, 0.1F);
            if (this.ticksInGround <= 30) {
               this.setSize(0.1F, 0.1F);
               super.worldObj.spawnParticle("dripWater", super.posX, super.posY - 0.3D, super.posZ, 0.0D, 0.0D, 0.0D);
            }

            if (this.ticksInGround > 80) {
               this.setDead();
            }
         } else {
            this.inGround = false;
            super.motionX *= (double)(super.rand.nextFloat() * 0.2F);
            super.motionY *= (double)(super.rand.nextFloat() * 0.2F);
            super.motionZ *= (double)(super.rand.nextFloat() * 0.2F);
            this.ticksInGround = 0;
            this.ticksInAir = 0;
         }
      } else {
         ++this.ticksInAir;
         Vec3 var18 = super.worldObj.getWorldVec3Pool().getVecFromPool(super.posX, super.posY, super.posZ);
         Vec3 var19 = super.worldObj.getWorldVec3Pool().getVecFromPool(super.posX + super.motionX, super.posY + super.motionY, super.posZ + super.motionZ);
         MovingObjectPosition var20 = super.worldObj.rayTraceBlocks_do_do(var18, var19, false, true);
         var18 = super.worldObj.getWorldVec3Pool().getVecFromPool(super.posX, super.posY, super.posZ);
         var19 = super.worldObj.getWorldVec3Pool().getVecFromPool(super.posX + super.motionX, super.posY + super.motionY, super.posZ + super.motionZ);
         if (var20 != null) {
            var19 = super.worldObj.getWorldVec3Pool().getVecFromPool(var20.hitVec.xCoord, var20.hitVec.yCoord, var20.hitVec.zCoord);
         }

         Entity var21 = null;
         List var22 = super.worldObj.getEntitiesWithinAABBExcludingEntity(this, super.boundingBox.addCoord(super.motionX, super.motionY, super.motionZ).expand(1.0D, 1.0D, 1.0D));
         double var23 = 0.0D;

         for(int var17 = 0; var17 < var22.size(); ++var17) {
            Entity var12 = (Entity)var22.get(var17);
            if (var12.canBeCollidedWith() && (var12 != this.shootingEntity || this.ticksInAir >= 5)) {
               float var24 = 0.3F;
               AxisAlignedBB var13 = var12.boundingBox.expand((double)var24, (double)var24, (double)var24);
               MovingObjectPosition var25 = var13.calculateIntercept(var18, var19);
               if (var25 != null) {
                  double var14 = var18.distanceTo(var25.hitVec);
                  if (var14 < var23 || var23 == 0.0D) {
                     var21 = var12;
                     var23 = var14;
                  }
               }
            }
         }

         if (var21 != null) {
            var20 = new MovingObjectPosition(var21);
         }

         if (var20 != null) {
            if (var20.entityHit != null) {
               float var26 = MathHelper.sqrt_double(super.motionX * super.motionX + super.motionY * super.motionY + super.motionZ * super.motionZ);
               int var27 = MathHelper.ceiling_double_int((double)var26 * this.damage);
               if (this.getIsCritical()) {
                  var27 += super.rand.nextInt(var27 / 2 + 2);
               }

               MovingObjectPosition var25 = null;
               DamageSource var30;
               if (this.shootingEntity == null) {
                  var30 = DamageSource.causeThrownDamage(this, this);
               } else {
                  var30 = DamageSource.causeThrownDamage(this, this.shootingEntity);
               }

               if (var20.entityHit.attackEntityFrom(var30, (float)var27)) {
                  if (var20.entityHit instanceof EntityLiving) {
                     byte var15 = 10;
                     ((EntityLiving)var21).addPotionEffect(new PotionEffect(Potion.moveSlowdown.getId(), 30 * var15, 2));
                  }
               } else {
                  this.setDead();
               }

               this.setDead();
               if (this.isBurning()) {
                  var20.entityHit.setFire(5);
               }

               if (var20.entityHit.attackEntityFrom(var30, (float)var27)) {
                  if (var20.entityHit instanceof EntityLiving) {
                     EntityLiving var28 = (EntityLiving)var20.entityHit;
                     if (!super.worldObj.isRemote) {
                        var28.setArrowCountInEntity(var28.getArrowCountInEntity() + 1);
                     }

                     if (this.knockbackStrength > 0) {
                        var26 = MathHelper.sqrt_double(super.motionX * super.motionX + super.motionZ * super.motionZ);
                        if ((float)var27 > 0.0F) {
                           var20.entityHit.addVelocity(super.motionX * (double)this.knockbackStrength * 0.6000000238418579D / (double)var27, 0.1D, super.motionZ * (double)this.knockbackStrength * 0.6000000238418579D / (double)var27);
                        }
                     }

                     EnchantmentThorns.func_92096_a(this.shootingEntity, var28, super.rand);
                     if (this.shootingEntity != null && var20.entityHit != this.shootingEntity && var20.entityHit instanceof EntityPlayer && this.shootingEntity instanceof EntityPlayerMP) {
                        ((EntityPlayerMP)this.shootingEntity).playerNetServerHandler.sendPacketToPlayer(new Packet70GameEvent(6, 0));
                     }
                  }

                  this.playSound("random.bowhit", 1.0F, 1.2F / (super.rand.nextFloat() * 0.2F + 0.9F));
                  this.setDead();
               } else {
                  super.motionX *= -0.10000000149011612D;
                  super.motionY *= -0.10000000149011612D;
                  super.motionZ *= -0.10000000149011612D;
                  super.rotationYaw += 180.0F;
                  super.prevRotationYaw += 180.0F;
                  this.ticksInAir = 0;
               }
            } else {
               this.xTile = var20.blockX;
               this.yTile = var20.blockY;
               this.zTile = var20.blockZ;
               this.inTile = super.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);
               this.inData = super.worldObj.getBlockMetadata(this.xTile, this.yTile, this.zTile);
               super.motionX = (double)((float)(var20.hitVec.xCoord - super.posX));
               super.motionY = (double)((float)(var20.hitVec.yCoord - super.posY));
               super.motionZ = (double)((float)(var20.hitVec.zCoord - super.posZ));
               float var26 = MathHelper.sqrt_double(super.motionX * super.motionX + super.motionY * super.motionY + super.motionZ * super.motionZ);
               super.posX -= super.motionX / (double)var26 * 0.05000000074505806D;
               super.posY -= super.motionY / (double)var26 * 0.05000000074505806D;
               super.posZ -= super.motionZ / (double)var26 * 0.05000000074505806D;
               this.playSound("random.bowhit", 1.0F, 1.2F / (super.rand.nextFloat() * 0.2F + 0.9F));
               this.inGround = true;
               this.arrowShake = 7;
               this.setIsCritical(false);
               if (this.inTile != 0) {
                  Block.blocksList[this.inTile].onEntityCollidedWithBlock(super.worldObj, this.xTile, this.yTile, this.zTile, this);
               }
            }
         }

         if (this.getIsCritical()) {
            for(int var191 = 0; var191 < 4; ++var191) {
               super.worldObj.spawnParticle("splash", super.posX + super.motionX * (double)var191 / 4.0D, super.posY + super.motionY * (double)var191 / 4.0D, super.posZ + super.motionZ * (double)var191 / 4.0D, -super.motionX, -super.motionY + 0.2D, -super.motionZ);
            }
         }

         super.posX += super.motionX;
         super.posY += super.motionY;
         super.posZ += super.motionZ;
         float var26 = MathHelper.sqrt_double(super.motionX * super.motionX + super.motionZ * super.motionZ);
         super.rotationYaw = (float)(Math.atan2(super.motionX, super.motionZ) * 180.0D / 3.141592653589793D);

         for(super.rotationPitch = (float)(Math.atan2(super.motionY, (double)var26) * 180.0D / 3.141592653589793D); super.rotationPitch - super.prevRotationPitch < -180.0F; super.prevRotationPitch -= 360.0F) {
            ;
         }

         while(super.rotationPitch - super.prevRotationPitch >= 180.0F) {
            super.prevRotationPitch += 360.0F;
         }

         while(super.rotationYaw - super.prevRotationYaw < -180.0F) {
            super.prevRotationYaw -= 360.0F;
         }

         while(super.rotationYaw - super.prevRotationYaw >= 180.0F) {
            super.prevRotationYaw += 360.0F;
         }

         super.rotationPitch = super.prevRotationPitch + (super.rotationPitch - super.prevRotationPitch) * 0.2F;
         super.rotationYaw = super.prevRotationYaw + (super.rotationYaw - super.prevRotationYaw) * 0.2F;
         float var29 = 0.99F;
         float var24 = 0.05F;
         if (this.isInWater()) {
            this.setDead();
         }

         super.motionX *= (double)var29;
         super.motionY *= (double)var29;
         super.motionZ *= (double)var29;
         super.motionY -= (double)var24;
         this.setPosition(super.posX, super.posY, super.posZ);
         this.doBlockCollisions();
      }

   }

   public void writeEntityToNBT(NBTTagCompound var1) {
      var1.setShort("xTile", (short)this.xTile);
      var1.setShort("yTile", (short)this.yTile);
      var1.setShort("zTile", (short)this.zTile);
      var1.setByte("inTile", (byte)this.inTile);
      var1.setByte("inData", (byte)this.inData);
      var1.setByte("shake", (byte)this.arrowShake);
      var1.setByte("inGround", (byte)(this.inGround ? 1 : 0));
      var1.setByte("pickup", (byte)this.canBePickedUp);
      var1.setDouble("damage", this.damage);
   }

   public void readEntityFromNBT(NBTTagCompound var1) {
      this.xTile = var1.getShort("xTile");
      this.yTile = var1.getShort("yTile");
      this.zTile = var1.getShort("zTile");
      this.inTile = var1.getByte("inTile") & 255;
      this.inData = var1.getByte("inData") & 255;
      this.arrowShake = var1.getByte("shake") & 255;
      this.inGround = var1.getByte("inGround") == 1;
      if (var1.hasKey("damage")) {
         this.damage = var1.getDouble("damage");
      }

      if (var1.hasKey("pickup")) {
         this.canBePickedUp = var1.getByte("pickup");
      } else if (var1.hasKey("player")) {
         this.canBePickedUp = var1.getBoolean("player") ? 1 : 0;
      }

   }

   public void onCollideWithPlayer(EntityPlayer var1) {
      if (!super.worldObj.isRemote && this.inGround && this.arrowShake <= 0) {
         boolean var2 = this.canBePickedUp == 1 || this.canBePickedUp == 2 && var1.capabilities.isCreativeMode;
         if (this.canBePickedUp == 1 && !var1.inventory.addItemStackToInventory(new ItemStack(Item.arrow, 1))) {
            var2 = false;
         }

         if (var2) {
            this.playSound("random.pop", 0.2F, ((super.rand.nextFloat() - super.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            var1.onItemPickup(this, 1);
            this.setDead();
         }
      }

   }

   protected boolean canTriggerWalking() {
      return false;
   }

   public float getShadowSize() {
      return 0.0F;
   }

   public void setDamage(double var1) {
      this.damage = var1;
   }

   public double getDamage() {
      return this.damage;
   }

   public void setKnockbackStrength(int var1) {
      this.knockbackStrength = 0;
   }

   public boolean canAttackWithItem() {
      return false;
   }

   public void hitGround(int var1, int var2, int var3, int var4, int var5) {
      super.worldObj.createExplosion(this.shootingEntity, super.posX, super.posY, super.posZ, 3.0F, false);
      this.setDead();
   }

   public void setIsCritical(boolean var1) {
      byte var2 = super.dataWatcher.getWatchableObjectByte(16);
      if (var1) {
         super.dataWatcher.updateObject(16, Byte.valueOf((byte)(var2 | 1)));
      } else {
         super.dataWatcher.updateObject(16, Byte.valueOf((byte)(var2 & -2)));
      }

   }

   public boolean getIsCritical() {
      byte var1 = super.dataWatcher.getWatchableObjectByte(16);
      return (var1 & 1) != 0;
   }
}
