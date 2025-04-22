package iDiamondhunter.bows;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class bowEnder extends ItemBow {
   private int second = 3;
   public static Icon X1;
   public static Icon X2;
   public static Icon X3;
   public static Icon X0;

   public bowEnder(int var1, int var2) {
      super(var1);
      super.maxStackSize = 1;
      this.setMaxDamage(215);
      super.bFull3D = true;
      this.setCreativeTab(CreativeTabs.tabCombat);
   }

   public EnumRarity getRarity(ItemStack itemstack) {
      return EnumRarity.epic;
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IconRegister var1) {
      super.itemIcon = var1.registerIcon("mod/EnderBow1");
      X0 = var1.registerIcon("mod/EnderBow");
      X1 = var1.registerIcon("mod/EnderBow2");
      X2 = var1.registerIcon("mod/EnderBow3");
      X3 = var1.registerIcon("mod/EnderBow4");
   }

   public Icon getIcon(ItemStack par1ItemStack, int par2, EntityPlayer player, ItemStack var4, int var5) {
      if (player.isUsingItem() && var4.itemID == iDiamondhunter.bowEnder.itemID && var4 != null && var4.getItem().itemID == iDiamondhunter.bowEnder.itemID) {
         int k = var4.getMaxItemUseDuration() - var5;
         if (k >= 22) {
            return X3;
         } else if (k >= 11) {
            return X2;
         } else {
            return k >= 0 ? X1 : super.itemIcon;
         }
      } else {
         return super.itemIcon;
      }
   }

   public void onPlayerStoppedUsing(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, int par4) {
      (new Thread(new EnderBowFire(par1ItemStack, par2World, par3EntityPlayer, par4))).start();
   }

   public ItemStack onEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
      return par1ItemStack;
   }

   public int getMaxItemUseDuration(ItemStack par1ItemStack) {
      return 72000;
   }

   public EnumAction getItemUseAction(ItemStack par1ItemStack) {
      return EnumAction.bow;
   }

   public int getItemEnchantability() {
      return 2;
   }
}
