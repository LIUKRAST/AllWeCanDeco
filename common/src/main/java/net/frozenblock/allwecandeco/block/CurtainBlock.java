package net.frozenblock.allwecandeco.block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

public class CurtainBlock extends Block {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty LEFT = BooleanProperty.create("connected_left");
    public static final BooleanProperty RIGHT = BooleanProperty.create("connected_right");
    public static final BooleanProperty OPEN = BooleanProperty.create("open");

    public CurtainBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, LEFT, RIGHT, OPEN);
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult blockHitResult) {
        final boolean get = state.getValue(OPEN);
        forEachRightLeft(state, world, pos, (p, s) -> world.setBlock(p, s.setValue(OPEN, !get), 2));
        world.setBlock(pos, state.setValue(OPEN, !get), 2);
        this.playSound(player, world, pos);
        return InteractionResult.sidedSuccess(world.isClientSide);
    }

    private void playSound(@Nullable Entity entity, Level level, BlockPos blockPos) {
        level.playSound(entity, blockPos, SoundType.WOOL.getStepSound(), SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.1F + 0.9F);
    }

    @Override
    protected @NotNull VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        if (blockState.getValue(OPEN)) return Shapes.empty();
        return super.getCollisionShape(blockState, blockGetter, blockPos, collisionContext);
    }

    @Override
    public @NotNull BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        final var dir = state.getValue(FACING);
        BlockState right = world.getBlockState(pos.relative(dir.getClockWise()));
        if (isValid(state, right))
            world.setBlock(pos.relative(dir.getClockWise()), right.setValue(LEFT, false), 2);
        BlockState left = world.getBlockState(pos.relative(dir.getCounterClockWise()));
        if (isValid(state, left))
            world.setBlock(pos.relative(dir.getCounterClockWise()), left.setValue(RIGHT, false), 2);
        return super.playerWillDestroy(world, pos, state, player);
    }

    private void forEachRightLeft(BlockState state, Level world, BlockPos pos, BiConsumer<BlockPos, BlockState> triConsumer) {
        if (state.getValue(LEFT)) {
            BlockPos left = pos.relative(state.getValue(FACING).getCounterClockWise());
            while (true) {
                final BlockState a = world.getBlockState(left);
                if (isValid(state, a)) {
                    triConsumer.accept(left, a);
                    if (!a.getValue(LEFT)) break;
                } else break;
                left = left.relative(state.getValue(FACING).getCounterClockWise());
            }
        }
        if (state.getValue(RIGHT)) {
            BlockPos left = pos.relative(state.getValue(FACING).getClockWise());
            while (true) {
                final BlockState a = world.getBlockState(left);
                if (isValid(state, a)) {
                    triConsumer.accept(left, a);
                    if (!a.getValue(RIGHT)) break;
                } else break;
                left = left.relative(state.getValue(FACING).getClockWise());
            }
        }
    }

    private boolean isValid(BlockState instance, BlockState connection) {
        if (instance.is(this) && connection.is(this)) {
            return instance.getValue(FACING) == connection.getValue(FACING);
        }
        return false;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
        final var dir = ctx.getHorizontalDirection();
        final var world = ctx.getLevel();
        final var pos = ctx.getClickedPos();

        final var state = defaultBlockState()
                .setValue(FACING, dir)
                .setValue(OPEN, false);
        boolean connectRight = false;
        boolean connectLeft = false;

        forEachRightLeft(state, world, pos, (p, s) -> world.setBlock(p, s.setValue(OPEN, false), 2));
        BlockState right = world.getBlockState(pos.relative(dir.getClockWise()));
        if (isValid(state, right)) {
            world.setBlock(pos.relative(dir.getClockWise()), right.setValue(LEFT, true), 2);
            connectRight = true;
        }
        BlockState left = world.getBlockState(pos.relative(dir.getCounterClockWise()));
        if (isValid(state, left)) {
            connectLeft = true;
            world.setBlock(pos.relative(dir.getCounterClockWise()), left.setValue(RIGHT, true), 2);
        }

        return state
                .setValue(LEFT, connectLeft)
                .setValue(RIGHT, connectRight);
    }

    final VoxelShape NORTH = Shapes.create(0, -1, 0, 2 / 16f, 1, 1);
    final VoxelShape SOUTH = Shapes.create(14 / 16f, -1, 0, 1, 1, 1);
    final VoxelShape WEST = Shapes.create(0, -1, 0, 1, 1, 2 / 16f);
    final VoxelShape EAST = Shapes.create(0, -1, 14 / 16f, 1, 1, 1);

    @Override
    protected @NotNull VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return switch (blockState.getValue(FACING)) {
            case NORTH -> WEST;
            case SOUTH -> EAST;
            case WEST -> NORTH;
            case EAST -> SOUTH;
            default -> throw new IllegalStateException();
        };
    }
}
