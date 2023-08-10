package net.labymod.addons.clearwater.v1_12_2.mixins;


import net.labymod.addons.clearwater.ClearWaterAddon;
import net.labymod.addons.clearwater.ClearWaterConfiguration;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(net.minecraft.client.renderer.EntityRenderer.class)
public abstract class MixinEntityRenderer {

  @Redirect(method = "setupFog", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/state/IBlockState;getMaterial()Lnet/minecraft/block/material/Material;"))
  private Material getMaterial(IBlockState blockState) {
    ClearWaterConfiguration config = ClearWaterAddon.get().configuration();
    Material material = blockState.getMaterial();

    if (!config.enabled().get()) {
      return material;
    }

    if (material == Material.WATER && config.clearWater().get()) {
      return Material.AIR;
    }

    if (material == Material.LAVA && config.clearLava().get()) {
      return Material.AIR;
    }

    return material;
  }
}