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

package net.labymod.addons.clearwater.v1_21_11.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import java.nio.ByteBuffer;
import net.labymod.addons.clearwater.ClearWaterAddon;
import net.labymod.addons.clearwater.ClearWaterConfiguration;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.minecraft.client.renderer.fog.FogRenderer;
import net.minecraft.world.level.material.FogType;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FogRenderer.class)
public abstract class MixinFogRenderer {
  @WrapOperation(
      method = "setupFog",
      at = @At(
          value = "INVOKE",
          target = "Lnet/minecraft/client/renderer/fog/FogRenderer;updateBuffer(Ljava/nio/ByteBuffer;ILorg/joml/Vector4f;FFFFFF)V"
      )
  )
  private void clearwater_neutralizeFogBeforeUpdateBuffer(
      FogRenderer instance, ByteBuffer $$0, int $$1, Vector4f $$2, float $$3, float environmentalStart, float environmentalEnd,
      float renderDistanceStart, float renderDistanceEnd, float skyEnd, Operation<Void> original, @Local LocalRef<FogType> fogType
  ) {
    ClearWaterConfiguration configuration = ClearWaterAddon.get().configuration();
    if (!configuration.enabled().get()) {
      original.call(instance, $$0, $$1, $$2, $$3, environmentalStart, environmentalEnd, renderDistanceStart, renderDistanceEnd, skyEnd);
      return;
    }

    ConfigProperty<Boolean> configProperty = switch (fogType.get()) {
      case WATER -> configuration.clearWater();
      case LAVA -> configuration.clearLava();
      case POWDER_SNOW -> configuration.clearPowderedSnow();
      default -> null;
    };

    if (configProperty != null && configProperty.get()) {
      original.call(instance, $$0, $$1, $$2, $$3, Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
    } else {
      original.call(instance, $$0, $$1, $$2, $$3, environmentalStart, environmentalEnd, renderDistanceStart, renderDistanceEnd, skyEnd);
    }
  }
}
