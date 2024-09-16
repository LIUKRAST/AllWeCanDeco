package net.frozenblock.allwecandeco.datagen;

import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.frozenblock.allwecandeco.AWCDSharedConstants;
import net.frozenblock.allwecandeco.block.CurtainBlock;
import net.frozenblock.allwecandeco.block.WindowBlock;
import net.frozenblock.allwecandeco.registry.RegisterBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.data.models.blockstates.PropertyDispatch;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.data.models.model.TexturedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.Optional;

public class AWCDModelProvider extends FabricModelProvider {

    public AWCDModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators generator) {
        RegisterBlocks.iterateColor(RegisterBlocks.FLURRY_CARPET, (block, color) -> generateSingleParentAndTexture(
                generator,
                block,
                ResourceLocation.fromNamespaceAndPath(AWCDSharedConstants.MOD_ID, "block/" + color.getName() + "_flurry_wool"),
                ResourceLocation.fromNamespaceAndPath(AWCDSharedConstants.MOD_ID, "block/flurry_carpet")
        ));
        RegisterBlocks.iterateColor(RegisterBlocks.CARPET, (block, color) -> generateSingleParentAndTexture(
                generator,
                block,
                ResourceLocation.fromNamespaceAndPath(AWCDSharedConstants.MOD_ID, "block/" + color.getName() + "_flurry_wool"),
                ResourceLocation.fromNamespaceAndPath(AWCDSharedConstants.MOD_ID, "block/carpet_block")
        ));
        RegisterBlocks.iterateWood(RegisterBlocks.WINDOWS, (block, type) -> generator.blockStateOutput.accept(
                MultiVariantGenerator
                        .multiVariant(block)
                        .with(
                                PropertyDispatch.properties(WindowBlock.SWAP, WindowBlock.CONNECTED_UP, WindowBlock.CONNECTED_DOWN, WindowBlock.CONNECTED_LEFT, WindowBlock.CONNECTED_RIGHT)
                                        .generate((dir, cUp, cDown, cLeft, cRight) -> {
                                            if(dir) {
                                                final JsonObject element = new JsonObject();
                                                final JsonObject textures = new JsonObject();
                                                element.addProperty("parent", AWCDSharedConstants.MOD_ID + ":block/window" + createModel(cUp, cDown, cLeft, cRight));
                                                textures.addProperty("all", AWCDSharedConstants.MOD_ID + ":block/" + type.name() + "_window");
                                                textures.addProperty("particle", "minecraft:block/" + type.name() + "_planks");
                                                element.add("textures", textures);
                                                generator.modelOutput.accept(
                                                        ResourceLocation.fromNamespaceAndPath(AWCDSharedConstants.MOD_ID, "block/" + type.name() + "_window" + createModel(cUp, cDown, cLeft, cRight)),
                                                        () -> element
                                                );
                                            }
                                            return new Variant()
                                                    .with(VariantProperties.MODEL, ResourceLocation.fromNamespaceAndPath(AWCDSharedConstants.MOD_ID, "block/" + type.name() + "_window" + createModel(cUp, cDown, cLeft, cRight)))
                                                    .with(VariantProperties.Y_ROT, getRotation(dir));
                                        })
                        )
        ));
        RegisterBlocks.iterateColor(RegisterBlocks.CURTAINS, (block, color) -> generator.blockStateOutput.accept(
                MultiVariantGenerator
                        .multiVariant(block)
                        .with(
                                PropertyDispatch.properties(CurtainBlock.FACING, CurtainBlock.LEFT, CurtainBlock.RIGHT, CurtainBlock.OPEN)
                                        .generate((dir, left, right, open) -> {
                                            if(dir == Direction.NORTH) {
                                                final JsonObject element = new JsonObject();
                                                final JsonObject textures = new JsonObject();
                                                element.addProperty("parent", AWCDSharedConstants.MOD_ID + ":block/curtain" + createCurtain(left, right, open));
                                                textures.addProperty("all", AWCDSharedConstants.MOD_ID + ":block/" + color.getName() + "_curtain");
                                                textures.addProperty("particle", AWCDSharedConstants.MOD_ID + ":block/" + color.getName() + "_curtain");
                                                element.add("textures", textures);
                                                generator.modelOutput.accept(
                                                        ResourceLocation.fromNamespaceAndPath(AWCDSharedConstants.MOD_ID, "block/" + color.getName() + "_curtain" + createCurtain(left, right, open)),
                                                        () -> element
                                                );
                                            }
                                            return new Variant()
                                                    .with(VariantProperties.MODEL, ResourceLocation.fromNamespaceAndPath(AWCDSharedConstants.MOD_ID, "block/" + color.getName() + "_curtain" + createCurtain(left, right, open)))
                                                    .with(VariantProperties.Y_ROT, switch (dir) {
                                                        case NORTH -> VariantProperties.Rotation.R180;
                                                        case WEST -> VariantProperties.Rotation.R90;
                                                        case SOUTH -> VariantProperties.Rotation.R0;
                                                        case EAST -> VariantProperties.Rotation.R270;
                                                        default -> throw new IllegalStateException();
                                                    });
                                        })
                        )
        ));
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {

    }

    private String createCurtain(Boolean left, Boolean right, Boolean open) {
        if(left && right) return open ? "_middle_open" : "_middle";
        if(left) return open ? "_left_open" : "_left";
        if(right) return open ? "_right_open": "_right";
        return open ? "_open" : "";
    }

    private String createModel(Boolean cUp, Boolean cDown, Boolean cLeft, Boolean cRight) {
        if(cUp || cDown || cLeft || cRight) {
            String result = cUp ? "_u" : "_";
            result = cDown ? result + "d" : result;
            result = cLeft ? result + "l" : result;
            return cRight ? result + "r" : result;
        } else {
            return "";
        }
    }

    private VariantProperties.Rotation getRotation(boolean direction) {
        return direction ? VariantProperties.Rotation.R0 : VariantProperties.Rotation.R90;
    }

    private void generateSingleParentAndTexture(BlockModelGenerators generator, Block block, final ResourceLocation texture, final ResourceLocation parent) {
        generator.createTrivialBlock(block,
                TexturedModel.createDefault(
                        b -> new TextureMapping()
                                .putForced(TextureSlot.ALL, texture)
                                .putForced(TextureSlot.PARTICLE, texture),
                        new ModelTemplate(Optional.of(parent),Optional.empty())
                )
        );
    }
}
