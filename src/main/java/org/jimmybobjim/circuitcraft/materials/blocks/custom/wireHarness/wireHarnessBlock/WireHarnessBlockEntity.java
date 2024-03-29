package org.jimmybobjim.circuitcraft.materials.blocks.custom.wireHarness.wireHarnessBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jimmybobjim.circuitcraft.CircuitCraft;
import org.jimmybobjim.circuitcraft.materials.blocks.CCBlockEntities;
import org.jimmybobjim.circuitcraft.materials.blocks.custom.wireHarness.WireHarnessHoldable;
import org.jimmybobjim.circuitcraft.util.Util;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

import static org.jimmybobjim.circuitcraft.materials.blocks.custom.wireHarness.wireHarnessBlock.WireHarnessBlock.*;
import static org.jimmybobjim.circuitcraft.util.Util.v2;

public class WireHarnessBlockEntity extends BlockEntity {
    public static final String WIRE_DATA_TAG = "wire_data";
    public List<WireHarnessHoldable.WireData> getWIRE_DATA() {
        return WIRE_DATA;
    }

    protected final List<WireHarnessHoldable.WireData> WIRE_DATA = new ArrayList<>(16);

    {
        for (int i=0; i<16; i++) {
            WIRE_DATA.add(WireHarnessHoldable.WireData.EMPTY);
        }
    }

    private final class WireItemHandler extends ItemStackHandler {
        private final List<Boolean> filledSlots = new ArrayList<>(16);

        {
            for (int i = 0; i < 16; i++) {
                filledSlots.add(false);
            }
        }

        public WireItemHandler() {
            super(16);
        }

        @Override
        protected void onContentsChanged(int slot) {
            update();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            if (wireItemHandler.getStackInSlot(slot) != ItemStack.EMPTY) return false;

            if (Util.implementsInterface(stack.getItem().getClass(), WireHarnessHoldable.class)) {
                int width = ((WireHarnessHoldable) stack.getItem()).getWidth().getWidth();

                if (slot + width > 16) return false;

                for (int i = 0; i < width; i++) {
                    if (filledSlots.get(i + slot)) return false;
                }

                for (int i = 0; i < width; i++) {
                    filledSlots.set(i + slot, true);
                }

                return true;
            }
            return false;
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }

        public ItemStack removeItem(int slot) {
            WIRE_DATA.set(slot, WireHarnessHoldable.WireData.EMPTY);
            update();

            ItemStack stack = extractItem(slot, 1, true);
            setStackInSlot(slot, ItemStack.EMPTY);
            filledSlots.set(slot, false);
            return stack;
        }
    }

    private static final class FilterItemHandler extends ItemStackHandler {
        public final Direction direction;
        public FilterItemHandler(Direction direction) {
            super(2);

            this.direction = direction;
        }
    }

    private final WireItemHandler wireItemHandler = new WireItemHandler();

    private LazyOptional<WireItemHandler> lazyWireItemHandler = LazyOptional.empty();

    private final FilterItemHandler
            northFilterItemHelper = new FilterItemHandler(Direction.NORTH),
            southFilterItemHelper = new FilterItemHandler(Direction.SOUTH),
            eastFilterItemHelper = new FilterItemHandler(Direction.EAST),
            westFilterItemHelper = new FilterItemHandler(Direction.WEST),
            upFilterItemHelper = new FilterItemHandler(Direction.UP),
            downFilterItemHelper = new FilterItemHandler(Direction.DOWN);

    public ItemStackHandler getFilterItemHandler(Direction direction) {
        return switch (direction) {
            case DOWN -> downFilterItemHelper;
            case UP -> upFilterItemHelper;
            case NORTH -> northFilterItemHelper;
            case SOUTH -> southFilterItemHelper;
            case WEST -> westFilterItemHelper;
            case EAST -> eastFilterItemHelper;
        };
    }

    protected WireHarnessBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pState) {
        super(pType, pPos, pState);
    }

    public WireHarnessBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(CCBlockEntities.WIRE_HARNESS_BE.get(), pPos, pBlockState);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", wireItemHandler.serializeNBT());
        super.saveAdditional(pTag);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void load(CompoundTag pTag) {
        super.load(pTag);
        wireItemHandler.deserializeNBT(pTag.getCompound("inventory"));

        for (int i = 0; i<16; i++) {
            if (wireItemHandler.getStackInSlot(i).getItem() instanceof WireHarnessHoldable data) {
                WIRE_DATA.set(i, new WireHarnessHoldable.WireData(data.getWidth(), data.getWireTexture(i)));
            }
        }
    }

    private void update() {
        markDirty();
        setChanged();
        requestModelDataUpdate();

        if (getLevel() != null) {
            getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyWireItemHandler = LazyOptional.of(() -> wireItemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyWireItemHandler.invalidate();
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyWireItemHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    public void tick(Level level, BlockPos pPos, BlockState pState) {

    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(wireItemHandler.getSlots());
        for (int i = 0; i < wireItemHandler.getSlots(); i++) {
            inventory.setItem(i, wireItemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        //we don't !pLevel.isClientSide() because update() requires it is ran client side
        if (pHand == InteractionHand.MAIN_HAND) {
            ItemStack playerItem = pPlayer.getItemInHand(InteractionHand.MAIN_HAND);

            if (!Util.implementsInterface(playerItem.getItem().getClass(), WireHarnessHoldable.class)) return InteractionResult.FAIL;

            float
                    xRot = pState.getValue(X_ROT) * 90,
                    yRot = pState.getValue(Y_ROT) * 90,
                    zRot = pState.getValue(Z_ROT) * 90;

            // gets the exact position the player clicked on in an un-rotated wireHarness (un-rotated makes the math easier)
            Vec3 clickPos3 = Util.inverseRotate(Util.globalToBlockCoords(pHit.getLocation()), xRot, yRot, zRot);

            // gets rid of y value
            Vec2 clickPos2 = v2(clickPos3.x, clickPos3.z);

            for (int i = 0; i < 16; i++) {

                if (Util.isPointInsideRectangle(clickPos2, v2(i, 3.25f), v2(i + 1, 12.75f))) {

                    if (wireItemHandler.getStackInSlot(i) == ItemStack.EMPTY) {
                        WireHarnessHoldable wire = (WireHarnessHoldable) playerItem.getItem();

                        if (wireItemHandler.isItemValid(i, playerItem)) {

                            wireItemHandler.setStackInSlot(i, playerItem.copyWithCount(1));

                            WIRE_DATA.set(i, wire.getWireData(i));

                            if (!pPlayer.isCreative()) {
                                playerItem.setCount(playerItem.getCount() - 1);
                            }
                        } else {
                            return InteractionResult.FAIL;
                        }
                    } else if (pPlayer.isCrouching()) {
                        if (!pPlayer.isCreative()) {
                            pPlayer.addItem(wireItemHandler.removeItem(i));
                        } else {
                            wireItemHandler.removeItem(i);
                        }
                    } else {
                        return InteractionResult.FAIL;
                    }

                    break;
                }
            }
            update();

            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        update();
    }

    public void markDirty() {
        CircuitCraft.LOGGER.info("marked dirty at" + worldPosition);
    }

    @Override
    public @NotNull ModelData getModelData() {
        return ModelData.builder().with(WireHarnessBlock.WIRE_DATA, WIRE_DATA).build();
    }
}
