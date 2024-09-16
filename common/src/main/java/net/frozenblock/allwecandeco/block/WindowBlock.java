package net.frozenblock.allwecandeco.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class WindowBlock extends Block {
    public static final BooleanProperty SWAP = BooleanProperty.create("rotated");
    public static final BooleanProperty CONNECTED_UP = BooleanProperty.create("connected_up");
    public static final BooleanProperty CONNECTED_DOWN = BooleanProperty.create("connected_down");
    public static final BooleanProperty CONNECTED_LEFT = BooleanProperty.create("connected_left");
    public static final BooleanProperty CONNECTED_RIGHT = BooleanProperty.create("connected_right");

    public WindowBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SWAP, CONNECTED_UP, CONNECTED_DOWN, CONNECTED_LEFT, CONNECTED_RIGHT);
    }

    @Override
    protected void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos blockPos2, boolean bl) {
        super.neighborChanged(state, world, pos, block, blockPos2, bl);

        final boolean direction = state.getValue(SWAP);
        final boolean connectedUp = state.getValue(CONNECTED_UP);
        final boolean connectedDown = state.getValue(CONNECTED_DOWN);
        final boolean connectedLeft = state.getValue(CONNECTED_LEFT);
        final boolean connectedRight = state.getValue(CONNECTED_RIGHT);
        Boolean connectUp = null;
        Boolean connectDown = null;
        Boolean connectRight = null;
        Boolean connectLeft = null;

        final int network = getNetworkHeight(world, pos);
        final BlockPos source = getNetworkSource(world, pos);
        final var tu = world.getBlockState(pos.above());
        final var td = world.getBlockState(pos.below());

        if(isValidConnector(state, tu))
            connectUp = true;
        else if(connectedUp) connectUp = false;
        if(isValidConnector(state, td))
            connectDown = true;
        else if(connectedDown) connectDown = false;

        final int networkLeft = getNetworkHeight(world, pos.relative(getLeft(direction)));
        if(isValidConnector(state, world.getBlockState(pos.relative(getLeft(direction)))) && networkLeft != 0 && networkLeft == network) {
            final BlockPos networkSource = getNetworkSource(world, pos.relative(getLeft(direction)));
            if(source.getY() == networkSource.getY()) {
                if(!connectedLeft) connectLeft = true;
            } else if(connectedLeft) connectLeft = false;
        } else if(connectedLeft) connectLeft = false;
        final int networkRight = getNetworkHeight(world, pos.relative(getRight(direction)));
        if(isValidConnector(state, world.getBlockState(pos.relative(getRight(direction)))) && networkRight != 0 && networkRight == network) {
            final BlockPos networkSource = getNetworkSource(world, pos.relative(getRight(direction)));
            if(source.getY() == networkSource.getY()) {
                if(!connectedRight) connectRight = true;
            } else if(connectedRight) connectRight = false;
        } else if(connectedRight) connectRight = false;

        if(connectUp != null || connectDown != null || connectLeft != null || connectRight != null) {
            BlockState toPlace = state;
            if(connectUp != null) toPlace = toPlace.setValue(CONNECTED_UP, connectUp);
            if(connectDown != null) toPlace = toPlace.setValue(CONNECTED_DOWN, connectDown);
            if(connectLeft != null) toPlace = toPlace.setValue(CONNECTED_LEFT, connectLeft);
            if(connectRight != null) toPlace = toPlace.setValue(CONNECTED_RIGHT, connectRight);
            world.setBlock(pos, toPlace, 3);
        }
    }


    protected boolean isValidConnector(BlockState instance, BlockState connectWith) {
        if(connectWith == null && instance == null) throw new IllegalArgumentException("BlockStates cannot be both null");
        if(connectWith == null) return instance.is(this);
        if(instance == null) return connectWith.is(this);

        return instance.is(this) && connectWith.is(this) && instance.getValue(SWAP) == connectWith.getValue(SWAP);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        boolean other = state.getValue(SWAP);
        return Shapes.create(
                other ? 0 : 6/16d,
                0,
                other ? 6/16d : 0,
                other ? 1 : 10/16d,
                1,
                other ? 10/16d : 1
        );
    }

    private Direction getLeft(boolean dir) {
        if(dir) return Direction.EAST;
        else return Direction.SOUTH;
    }

    private Direction getRight(boolean dir) {
        if(dir) return Direction.WEST;
        else return Direction.NORTH;
    }


    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
        final var direction = switch (ctx.getHorizontalDirection()) {
            case NORTH,SOUTH -> true;
            case WEST,EAST -> false;
            default -> throw new IllegalArgumentException();
        };
        final var pos = ctx.getClickedPos();
        final var world = ctx.getLevel();

        boolean connectUp = false;
        boolean connectDown = false;
        boolean connectRight = false;
        boolean connectLeft = false;

        int networkUp = getNetworkHeight(world, pos.above());
        int networkDown = getNetworkHeight(world, pos.below());
        BlockState stateUp = world.getBlockState(pos.above());
        BlockState stateDown = world.getBlockState(pos.below());

        BlockState defaultState = defaultBlockState().setValue(SWAP, direction);

        if(isValidConnector(defaultState, stateUp)) {
            connectUp = true;
        }
        if(isValidConnector(defaultState, stateDown))
            connectDown = true;

        int resultNetwork = networkDown + networkUp + 1;

        final BlockPos source = !connectDown ? pos : getNetworkSource(world, pos.below());
        final var networkLeft = getNetworkHeight(world, pos.relative(getLeft(direction)));
        final BlockPos networkLeftSource = getNetworkSource(world, pos.relative(getLeft(direction)));
        final var networkRight = getNetworkHeight(world, pos.relative(getRight(direction)));
        final BlockPos networkRightSource = getNetworkSource(world, pos.relative(getRight(direction)));
        if(isValidConnector(defaultState, world.getBlockState(pos.relative(getLeft(direction)))) && networkLeftSource != null && source.getY() == networkLeftSource.getY() && networkLeft == resultNetwork) connectLeft = true;
        if(isValidConnector(defaultState, world.getBlockState(pos.relative(getRight(direction)))) && networkRightSource != null && source.getY() == networkRightSource.getY() && networkRight == resultNetwork) connectRight = true;

        return defaultState
                .setValue(CONNECTED_UP, connectUp)
                .setValue(CONNECTED_DOWN, connectDown)
                .setValue(CONNECTED_RIGHT, connectRight)
                .setValue(CONNECTED_LEFT, connectLeft);
    }

    private boolean isSameFacing(BlockState defaultState, BlockState blockState) {
        return defaultState.getValue(SWAP) == blockState.getValue(SWAP);
    }

    public BlockPos getNetworkSource(Level world, BlockPos pos) {
        final var instance = world.getBlockState(pos);
        if(instance.getBlock() instanceof WindowBlock) {
            BlockPos copy = pos.below();
            BlockState oldState = null;
            while (true) {
                copy = copy.above();
                var state = world.getBlockState(copy);
                if (isValidConnector(instance, state)) {
                    if (oldState == null || isSameFacing(oldState, state)) {
                        if (!state.getValue(CONNECTED_DOWN)) return copy;
                    } else return copy;
                } else return copy;
                oldState = state;
            }
        } else {
            return pos;
        }
    }

    public int getNetworkHeight(Level world, BlockPos pos) {
        return getNetworkHeight(world, pos, false);
    }

    public int getNetworkHeight(Level world, BlockPos pos, boolean alsoBugged) {
        BlockState a = world.getBlockState(pos);
        if(isValidConnector(null, a)) {
            int network = 1;
            if(a.getValue(CONNECTED_UP)) {
                BlockPos pos1 = pos;
                while(true) {
                    pos1 = pos1.above();
                    var state = world.getBlockState(pos1);
                    if(isValidConnector(a, state)) {
                        if(alsoBugged && !state.getValue(CONNECTED_DOWN)) break;
                        network++;
                        if(!state.getValue(CONNECTED_UP)) break;
                    } else {
                        break;
                    }
                }
            }
            if(a.getValue(CONNECTED_DOWN)) {
                BlockPos pos1 = pos;
                while(true) {
                    pos1 = pos1.below();
                    var state = world.getBlockState(pos1);
                    if(isValidConnector(a, state)) {
                        if(alsoBugged && !state.getValue(CONNECTED_UP)) break;
                        network++;
                        if(!state.getValue(CONNECTED_DOWN)) break;
                    } else {
                        break;
                    }
                }
            }
            return network;
        } else {
            return 0;
        }
    }
}

