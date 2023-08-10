package net.labymod.addons.clearwater.v1_16_5.mixins;

import net.labymod.addons.clearwater.ClearWaterAddon;
import net.labymod.addons.clearwater.ClearWaterConfiguration;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.material.FluidState;
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

    FluidState fluid = camera.getFluidInCamera();

    if (fluid.is(FluidTags.WATER) && config.clearWater().get()) {
      ci.cancel();
    }

    if (fluid.is(FluidTags.LAVA) && config.clearLava().get()) {
      ci.cancel();
    }
  }
}
