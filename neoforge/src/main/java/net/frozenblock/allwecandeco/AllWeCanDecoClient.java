package net.frozenblock.allwecandeco;

import net.frozenblock.allwecandeco.registry.RegisterBlocks;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = AWCDSharedConstants.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class AllWeCanDecoClient {

    @SuppressWarnings("deprecation")
    @SubscribeEvent
    public static void initializeClient(final FMLClientSetupEvent event) {
        RegisterBlocks.FLURRY_CARPET.forEach(e -> ItemBlockRenderTypes.setRenderLayer(e, RenderType.cutout()));
        RegisterBlocks.WINDOWS.forEach(e -> ItemBlockRenderTypes.setRenderLayer(e, RenderType.cutout()));
        RegisterBlocks.CURTAINS.forEach(e -> ItemBlockRenderTypes.setRenderLayer(e, RenderType.cutout()));
    }
}
