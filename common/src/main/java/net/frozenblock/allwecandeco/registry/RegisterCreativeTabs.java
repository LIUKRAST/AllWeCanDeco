package net.frozenblock.allwecandeco.registry;

import net.frozenblock.allwecandeco.AWCDSharedConstants;
import net.frozenblock.allwecandeco.platform.Services;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class RegisterCreativeTabs {
    public static final ResourceKey<CreativeModeTab> CUSTOM_ITEM_GROUP_KEY = ResourceKey.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), ResourceLocation.fromNamespaceAndPath(AWCDSharedConstants.MOD_ID, "item_group"));
    public static final CreativeModeTab CUSTOM_ITEM_GROUP = Services.PLATFORM.getCreativeTabBuilder()
            .icon(() -> new ItemStack(RegisterBlocks.WINDOWS.get(4)))
            .title(Component.translatable("itemGroup.all_we_can_deco"))
            .displayItems((itemDisplayParameters, output) -> {
                RegisterBlocks.FLURRY_CARPET.forEach(output::accept);
                RegisterBlocks.CARPET.forEach(output::accept);
                RegisterBlocks.WINDOWS.forEach(output::accept);
                RegisterBlocks.CURTAINS.forEach(output::accept);
            })
            .build();

    public static void register() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, CUSTOM_ITEM_GROUP_KEY, CUSTOM_ITEM_GROUP);
    }
}
