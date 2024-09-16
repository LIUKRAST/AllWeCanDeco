package net.frozenblock.allwecandeco.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.frozenblock.allwecandeco.registry.RegisterBlocks;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class AWCDLootTableProvider extends FabricBlockLootTableProvider {
    public AWCDLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        RegisterBlocks.FLURRY_CARPET.forEach(this::dropSelf);
        RegisterBlocks.CARPET.forEach(this::dropSelf);
        RegisterBlocks.WINDOWS.forEach(this::dropSelf);
        RegisterBlocks.CURTAINS.forEach(this::dropSelf);
    }
}
