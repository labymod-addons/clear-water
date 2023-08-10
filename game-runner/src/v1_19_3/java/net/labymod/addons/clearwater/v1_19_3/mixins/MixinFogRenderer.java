/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package net.labymod.addons.clearwater.v1_19_3.mixins;

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
  private static void clearwater_setupFog(Camera camera, FogRenderer.FogMode fogMode, float farPlaneDistance, boolean bl, float f, CallbackInfo ci) {
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
