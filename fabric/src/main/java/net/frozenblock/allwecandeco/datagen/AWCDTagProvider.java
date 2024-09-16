package net.frozenblock.allwecandeco.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.frozenblock.allwecandeco.registry.RegisterBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;

import java.util.concurrent.CompletableFuture;

public class AWCDTagProvider {

    public static class BlockTag extends FabricTagProvider.BlockTagProvider {
        public BlockTag(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected void addTags(HolderLookup.Provider wrapperLookup) {
            var ovg = getOrCreateTagBuilder(BlockTags.OCCLUDES_VIBRATION_SIGNALS);
            var w = getOrCreateTagBuilder(BlockTags.WOOL);
            var dv = getOrCreateTagBuilder(BlockTags.DAMPENS_VIBRATIONS);
            var dyed = getOrCreateTagBuilder(ConventionalBlockTags.DYED);
            RegisterBlocks.iterateColor(RegisterBlocks.FLURRY_CARPET, (block, color) -> {
                ovg.add(block);
                getOrCreateTagBuilder(TagKey.create(BuiltInRegistries.BLOCK.key(), ResourceLocation.fromNamespaceAndPath("c", "dyed/" + color.getName())));
                w.add(block);
                dyed.add(block);
                dv.add(block);
            });
            RegisterBlocks.iterateColor(RegisterBlocks.CARPET, (block, color) -> {
                ovg.add(block);
                getOrCreateTagBuilder(TagKey.create(BuiltInRegistries.BLOCK.key(), ResourceLocation.fromNamespaceAndPath("c", "dyed/" + color.getName())));
                w.add(block);
                dyed.add(block);
                dv.add(block);
            });
        }
    }
}
