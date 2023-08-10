package net.labymod.addons.clearwater;

import net.labymod.api.addon.LabyAddon;
import net.labymod.api.models.addon.annotation.AddonMain;

@AddonMain
public class ClearWaterAddon extends LabyAddon<ClearWaterConfiguration> {

  private static ClearWaterAddon instance;

  public ClearWaterAddon() {
    instance = this;
  }

  public static ClearWaterAddon get() {
    return instance;
  }

  @Override
  protected void enable() {
    this.registerSettingCategory();
  }

  @Override
  protected Class<ClearWaterConfiguration> configurationClass() {
    return ClearWaterConfiguration.class;
  }
}
