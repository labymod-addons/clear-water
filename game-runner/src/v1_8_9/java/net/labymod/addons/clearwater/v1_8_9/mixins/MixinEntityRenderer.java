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