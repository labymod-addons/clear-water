package net.labymod.addons.clearwater.v1_18_2.mixins;

import net.labymod.addons.clearwater.ClearWaterAddon;
import net.labymod.addons.clearwater.ClearWaterConfiguration;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.level.material.FogType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FogRenderer.class)
public abstract class MixinFogRenderer {

  @Inject(method = "setupFog", at = @At("HEAD"), cancellable = true)
  private static void clearwater_setupFog(Camera camera, FogRenderer.FogMode fogMode, float farPlaneDistance, boolean bl, CallbackInfo ci) {
    ClearWaterConfiguration config = ClearWaterAddon.get().configuration();

    if (!config.enabled().get()) {
      return;
    }

    FogType fluid = camera.getFluidInCamera();

    if (fluid == FogType.WATER && config.clearWater().get()) {
      ci.cancel();
    }

    if (fluid == FogType.LAVA && config.clearLava().get()) {
      ci.cancel();
    }
  }
}
