package net.frozenblock.allwecandeco.platform;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.frozenblock.allwecandeco.platform.services.IPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.item.CreativeModeTab;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public CreativeModeTab.Builder getCreativeTabBuilder() {
        return FabricItemGroup.builder();
    }
}
