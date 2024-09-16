package net.frozenblock.allwecandeco.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.frozenblock.allwecandeco.registry.RegisterBlocks;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class AWCDLangProvider extends FabricLanguageProvider {
    public AWCDLangProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider registryLookup, TranslationBuilder translationBuilder) {
        translationBuilder.add("itemGroup.all_we_can_deco", "All We Can Deco");
        RegisterBlocks.iterateColor(RegisterBlocks.FLURRY_CARPET, (block, color) -> {
            final String str = cap(color.getName());
            translationBuilder.add(block, str + " Flurry Carpet");
        });
        RegisterBlocks.iterateColor(RegisterBlocks.CARPET, (block, color) -> {
            final String str = cap(color.getName());
            translationBuilder.add(block, str + " Carpet Block");
        });
        RegisterBlocks.iterateColor(RegisterBlocks.CURTAINS, (block, color) -> {
            final String str = cap(color.getName());
            translationBuilder.add(block, str + " Curtain");
        });
        RegisterBlocks.iterateWood(RegisterBlocks.WINDOWS, (block, type) -> {
            final String str = cap(type.name());
            translationBuilder.add(block, str + " Window");
        });
    }
    private String cap(String str) {
        String[] split = str.split("_");
        for(int i = 0; i < split.length; i++) {
            split[i] = split[i].substring(0, 1).toUpperCase() + split[i].substring(1);
        }
        StringBuilder re = new StringBuilder();
        for(String a : split) {
            re.append(a);
        }
        return re.toString();
    }

}
