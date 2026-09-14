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

package net.labymod.addons.clearwater.v1_21_8.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import net.labymod.addons.clearwater.ClearWaterAddon;
import net.labymod.addons.clearwater.ClearWaterConfiguration;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.FogRenderer;
import net.minecraft.world.level.material.FogType;
import org.joml.Vector4f;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FogRenderer.class)
public abstract class MixinFogRenderer {

  // setupFog fills a FogData and reads the fields back out of it for the buffer write, so clearing
  // the object covers that write as well.
  //
  // This sits on the last field write instead of around the buffer write because Sodium reads the
  // same object from a RETURN injector and copies the ranges into the uniform it renders chunks
  // with. Replacing only the arguments of the write left that copy untouched, which kept the fog
  // on the terrain while hand, entities and particles lost it.
  @Inject(
      method = "setupFog",
      at = @At(
          value = "FIELD",
          target = "Lnet/minecraft/client/renderer/fog/FogData;renderDistanceEnd:F",
          opcode = Opcodes.PUTFIELD,
          shift = At.Shift.AFTER
      )
  )
  private void clearwater_neutralizeFogBeforeUpdateBuffer(
      Camera camera, int renderDistanceInChunks, boolean isFoggy, DeltaTracker deltaTracker,
      float darkenWorldAmount, ClientLevel level,
      CallbackInfoReturnable<Vector4f> callback, @Local FogType fogType, @Local FogData fog
  ) {
    ClearWaterConfiguration configuration = ClearWaterAddon.get().configuration();
    if (!configuration.enabled().get()) {
      return;
    }

    ConfigProperty<Boolean> configProperty = switch (fogType) {
      case WATER -> configuration.clearWater();
      case LAVA -> configuration.clearLava();
      case POWDER_SNOW -> configuration.clearPowderedSnow();
      default -> null;
    };

    if (configProperty == null || !configProperty.get()) {
      return;
    }

    fog.environmentalEnd = Float.MAX_VALUE;
    fog.renderDistanceStart = Float.MAX_VALUE;
    fog.renderDistanceEnd = Float.MAX_VALUE;
    fog.skyEnd = Float.MAX_VALUE;
    fog.cloudEnd = Float.MAX_VALUE;
  }
}
