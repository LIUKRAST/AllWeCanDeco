package net.frozenblock.allwecandeco;

import net.frozenblock.allwecandeco.registry.RegisterBlocks;
import net.frozenblock.allwecandeco.registry.RegisterCreativeTabs;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.concurrent.CompletableFuture;


@Mod(AWCDSharedConstants.MOD_ID)
public class AllWeCanDeco {

    @SuppressWarnings("unused")
    public AllWeCanDeco(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.register(this);
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void registryEvent(RegisterEvent event) {
        event.register(BuiltInRegistries.BLOCK.key(), helper -> RegisterBlocks.register());
        event.register(BuiltInRegistries.CREATIVE_MODE_TAB.key(), helper -> RegisterCreativeTabs.register());
    }

    @SubscribeEvent
    public void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        generator.addProvider(
                event.includeServer(),
                new MyRecipeProvider(output)
        );
    }

    private static class MyRecipeProvider extends LanguageProvider {


        public MyRecipeProvider(PackOutput output) {
            super(output, "all_we_can_deco", "en_us");
        }

        @Override
        protected void addTranslations() {
            add("itemGroup.all_we_can_deco", "Funziona diocane");
        }
    }
}
