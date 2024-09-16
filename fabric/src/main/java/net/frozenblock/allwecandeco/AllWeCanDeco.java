package net.frozenblock.allwecandeco;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.frozenblock.allwecandeco.datagen.AWCDLangProvider;
import net.frozenblock.allwecandeco.datagen.AWCDLootTableProvider;
import net.frozenblock.allwecandeco.datagen.AWCDModelProvider;
import net.frozenblock.allwecandeco.datagen.AWCDTagProvider;
import net.frozenblock.allwecandeco.registry.RegisterBlocks;
import net.frozenblock.allwecandeco.registry.RegisterCreativeTabs;

public class AllWeCanDeco implements ModInitializer, DataGeneratorEntrypoint {
    @Override
    public void onInitialize() {
        RegisterBlocks.register();
        RegisterCreativeTabs.register();
    }

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        var pack = fabricDataGenerator.createPack();
        pack.addProvider(AWCDLangProvider::new);
        pack.addProvider(AWCDLootTableProvider::new);
        pack.addProvider(AWCDModelProvider::new);
        pack.addProvider(AWCDTagProvider.BlockTag::new);
    }
}
