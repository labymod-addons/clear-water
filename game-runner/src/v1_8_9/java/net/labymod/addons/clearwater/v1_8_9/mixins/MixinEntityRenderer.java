package net.labymod.addons.clearwater.v1_8_9.mixins;


import net.labymod.addons.clearwater.ClearWaterAddon;
import net.labymod.addons.clearwater.ClearWaterConfiguration;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(net.minecraft.client.renderer.EntityRenderer.class)
public abstract class MixinEntityRenderer {

  @Redirect(method = "setupFog", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getMaterial()Lnet/minecraft/block/material/Material;"))
  private Material getMaterial(Block block) {
    ClearWaterConfiguration config = ClearWaterAddon.get().configuration();
    Material material = block.getMaterial();

    if (!config.enabled().get()) {
      return material;
    }

    if (material == Material.water && config.clearWater().get()) {
      return Material.air;
    }

    if (material == Material.lava && config.clearLava().get()) {
      return Material.air;
    }

    return material;
  }
}