package net.frozenblock.allwecandeco.registry;

import net.frozenblock.allwecandeco.AWCDSharedConstants;
import net.frozenblock.allwecandeco.block.CurtainBlock;
import net.frozenblock.allwecandeco.block.FancyCarpetBlock;
import net.frozenblock.allwecandeco.block.WindowBlock;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CarpetBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.WoodType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class RegisterBlocks {
    public static final List<Block> FLURRY_CARPET = createColored(color -> new CarpetBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BLACK_WOOL)));
    public static final List<Block> CARPET = createColored(color -> new FancyCarpetBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BLACK_WOOL)));
    public static final List<Block> WINDOWS = createWooden(type -> new WindowBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)));
    public static final List<Block> CURTAINS = createColored(color -> new CurtainBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BLACK_WOOL).noOcclusion()));

    public static void register() {
        iterateColor(FLURRY_CARPET, (block, color) -> {
            Registry.register(BuiltInRegistries.BLOCK, AWCDSharedConstants.id(color.getName() + "_flurry_carpet"), block);
            Registry.register(BuiltInRegistries.ITEM, AWCDSharedConstants.id(color.getName() + "_flurry_carpet"), new BlockItem(block, new Item.Properties()));
        });
        iterateColor(CARPET, (block, color) -> {
            Registry.register(BuiltInRegistries.BLOCK, AWCDSharedConstants.id(color.getName() + "_carpet_block"), block);
            Registry.register(BuiltInRegistries.ITEM, AWCDSharedConstants.id(color.getName() + "_carpet_block"), new BlockItem(block, new Item.Properties()));
        });
        iterateWood(WINDOWS, (block, type) -> {
            Registry.register(BuiltInRegistries.BLOCK, AWCDSharedConstants.id(type.name() + "_window"), block);
            Registry.register(BuiltInRegistries.ITEM, AWCDSharedConstants.id(type.name() + "_window"), new BlockItem(block, new Item.Properties()));
        });
        iterateColor(CURTAINS, (block, color) -> {
            Registry.register(BuiltInRegistries.BLOCK, AWCDSharedConstants.id(color.getName() + "_curtain"), block);
            Registry.register(BuiltInRegistries.ITEM, AWCDSharedConstants.id(color.getName() + "_curtain"), new BlockItem(block, new Item.Properties()));
        });
    }

    public static <T> List<T> createColored(Function<DyeColor, T> colorFunction) {
        final ArrayList<T> list = new ArrayList<>();
        for(DyeColor color : DyeColor.values()) {
            list.add(colorFunction.apply(color));
        }
        return list;
    }

    public static <T> void iterateColor(List<T> blocks, BiConsumer<T, DyeColor> consumer) {
        for(int i = 0; i < DyeColor.values().length; i++) {
            consumer.accept(blocks.get(i), DyeColor.values()[i]);
        }
    }

    public static <T> List<T> createWooden(Function<WoodType, T> woodFunction) {
        final ArrayList<T> list = new ArrayList<>();
        for(WoodType type : WoodType.values().toList()) {
            list.add(woodFunction.apply(type));
        }
        return list;
    }

    public static <T> void iterateWood(List<T> blocks, BiConsumer<T, WoodType> consumer) {
        for(int i = 0; i < WoodType.values().toList().size(); i++) {
            consumer.accept(blocks.get(i), WoodType.values().toList().get(i));
        }
    }
}
