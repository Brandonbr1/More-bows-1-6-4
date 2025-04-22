package iDiamondhunter.bows;

import java.util.Random;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EnderBowFire implements Runnable {
   private int second = 3;
   protected static Random itemRand = new Random();

   public EnderBowFire(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, int par4) {
      boolean flag = par3EntityPlayer.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, par1ItemStack) > 0;
      if (flag || par3EntityPlayer.inventory.hasItem(Item.arrow.itemID) || this.second == 3) {
         int i = this.getMaxItemUseDuration(par1ItemStack) - par4;
         float f = (float)i / 22.0F;
         f = (f * f + f * 2.0F) / 3.0F;
         if ((double)f < 0.1D) {
            return;
         }

         if (f > 1.0F) {
            f = 1.0F;
         }

         EntityArrow entityarrow = new EntityArrow(par2World, par3EntityPlayer, f * 2.0F);
         EntityArrow entityarrow2 = new EntityArrow(par2World, par3EntityPlayer, 1.0F);
         EntityArrow entityarrow3 = new EntityArrow(par2World, par3EntityPlayer, 1.2F);
         EntityArrow entityarrow4 = new EntityArrow(par2World, par3EntityPlayer, 1.5F);
         EntityArrow entityarrow5 = new EntityArrow(par2World, par3EntityPlayer, 1.75F);
         EntityArrow entityarrow6 = new EntityArrow(par2World, par3EntityPlayer, 1.825F);
         if (f == 1.0F) {
            entityarrow.setIsCritical(true);
            entityarrow2.setIsCritical(true);
            entityarrow3.setIsCritical(true);
            entityarrow4.setIsCritical(true);
            entityarrow5.setIsCritical(true);
            entityarrow6.setIsCritical(true);
         }

         int j = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, par1ItemStack);
         if (j > 0) {
            entityarrow.setDamage(entityarrow.getDamage() + (double)j * 0.5D + 0.5D);
            entityarrow2.setDamage(entityarrow.getDamage() + (double)j * 0.5D + 0.5D);
            entityarrow3.setDamage(entityarrow.getDamage() + (double)j * 0.5D + 0.5D);
            entityarrow4.setDamage(entityarrow.getDamage() + (double)j * 0.5D + 0.5D);
            entityarrow5.setDamage(entityarrow.getDamage() + (double)j * 0.5D + 0.5D);
            entityarrow6.setDamage(entityarrow.getDamage() + (double)j * 0.5D + 0.5D);
         }

         int k = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, par1ItemStack);
         if (k > 0) {
            entityarrow.setKnockbackStrength(k);
            entityarrow2.setKnockbackStrength(k);
            entityarrow3.setKnockbackStrength(k);
            entityarrow4.setKnockbackStrength(k);
            entityarrow5.setKnockbackStrength(k);
            entityarrow6.setKnockbackStrength(k);
         }

         if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, par1ItemStack) > 0) {
            entityarrow.setFire(100);
            entityarrow2.setFire(100);
            entityarrow3.setFire(100);
            entityarrow4.setFire(100);
            entityarrow5.setFire(100);
            entityarrow6.setFire(100);
         }

         par1ItemStack.damageItem(1, par3EntityPlayer);
         par2World.playSoundAtEntity(par3EntityPlayer, "random.bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
         if (!flag) {
            par3EntityPlayer.inventory.consumeInventoryItem(Item.arrow.itemID);
         } else {
            entityarrow.canBePickedUp = 2;
            entityarrow2.canBePickedUp = 0;
            entityarrow3.canBePickedUp = 0;
            entityarrow4.canBePickedUp = 0;
            entityarrow5.canBePickedUp = 0;
            entityarrow6.canBePickedUp = 0;
         }

         if (!par2World.isRemote) {
            entityarrow.canBePickedUp = 2;
            entityarrow2.canBePickedUp = 0;
            entityarrow3.canBePickedUp = 0;
            entityarrow4.canBePickedUp = 0;
            entityarrow5.canBePickedUp = 0;
            entityarrow6.canBePickedUp = 0;
            par2World.spawnEntityInWorld(entityarrow);
            entityarrow.setDamage(entityarrow.getDamage() * 1.25D);

            while(this.second > -1) {
               if (this.second == 3) {
                  this.second = 2;
                  System.out.println(this.second);
               }

               try {
                  Thread.sleep(1000L);
               } catch (Exception var17) {
                  ;
               }

               --this.second;
               System.out.println(this.second);
            }

            if (this.second == -1) {
               par2World.spawnEntityInWorld(entityarrow2);
               par2World.spawnEntityInWorld(entityarrow3);
               ++entityarrow3.posY;
               --entityarrow3.posX;
               ++entityarrow3.posZ;
               par2World.spawnEntityInWorld(entityarrow4);
               ++entityarrow4.posY;
               entityarrow4.posX -= 2.25D;
               entityarrow4.posZ -= 0.75D;
               par2World.spawnEntityInWorld(entityarrow5);
               entityarrow5.posY += 2.0D;
               entityarrow5.posX += 0.25D;
               entityarrow5.posZ += 2.5D;
               par2World.spawnEntityInWorld(entityarrow6);
               ++entityarrow6.posY;
               ++entityarrow6.posX;
               ++entityarrow6.posZ;
               this.second = 3;
            }
         }
      }

   }

   public int getMaxItemUseDuration(ItemStack par1ItemStack) {
      return 72000;
   }

   public void run() {
   }
}