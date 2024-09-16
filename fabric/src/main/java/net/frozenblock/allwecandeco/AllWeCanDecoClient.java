package net.frozenblock.allwecandeco;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.frozenblock.allwecandeco.registry.RegisterBlocks;
import net.minecraft.client.renderer.RenderType;

public class AllWeCanDecoClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        RegisterBlocks.FLURRY_CARPET.forEach(e -> BlockRenderLayerMap.INSTANCE.putBlock(e, RenderType.cutout()));
        RegisterBlocks.WINDOWS.forEach(e -> BlockRenderLayerMap.INSTANCE.putBlock(e, RenderType.cutout()));
        RegisterBlocks.CURTAINS.forEach(e -> BlockRenderLayerMap.INSTANCE.putBlock(e, RenderType.cutout()));
    }
}
