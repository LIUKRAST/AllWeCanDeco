package net.frozenblock.allwecandeco;

import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AWCDSharedConstants {
    public static final String MOD_ID = "all_we_can_deco";
    public static final String MOD_NAME = "All We Can Deco";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    public static ResourceLocation id(String var) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, var);
    }
}
