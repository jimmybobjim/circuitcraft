package org.jimmybobjim.circuitcraft.materials.blocks.custom.wireHarness.facadeBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jimmybobjim.circuitcraft.materials.blocks.CCBlockEntities;
import org.jimmybobjim.circuitcraft.materials.blocks.custom.wireHarness.wireHarnessBlock.WireHarnessBlock;
import org.jimmybobjim.circuitcraft.materials.blocks.custom.wireHarness.wireHarnessBlock.WireHarnessBlockEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FacadeBlockEntity extends WireHarnessBlockEntity {
    public static final String MIMIC_TAG = "mimic";

    @Nullable private BlockState mimicBlock;
    public FacadeBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(CCBlockEntities.FACADE_BE.get(), pPos, pBlockState);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);

        if (level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
            requestModelDataUpdate();
        }
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        CompoundTag nbtTag = new CompoundTag();
        saveMimic(nbtTag);
        return ClientboundBlockEntityDataPacket.create(this, (BlockEntity entity) -> nbtTag);
    }

    @Nonnull
    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag updateTag = super.getUpdateTag();
        saveMimic(updateTag);
        return updateTag;
    }

    public @Nullable BlockState getMimicBlock() {
        return mimicBlock;
    }

    private boolean isFacadeBlockSolid() {
        if (getLevel() != null && mimicBlock != null) {
            return mimicBlock.getShape(getLevel(), getBlockPos()) == Block.box(0,0,0,16,16,16)
                    && mimicBlock.isSolidRender(getLevel(), getBlockPos());
        }
        return false;
    }

    @Nonnull
    @Override
    public ModelData getModelData() {
        return ModelData.builder()
                .with(WireHarnessBlock.WIRE_DATA, WIRE_DATA)
                .with(WireHarnessBlock.FACADE_BLOCK_ID, mimicBlock)
                .with(WireHarnessBlock.IS_FACADE_BLOCK_SOLID, isFacadeBlockSolid())
                .build();
    }

    public void setMimicBlock(BlockState mimicBlock) {
        this.mimicBlock = mimicBlock.hasProperty(BlockStateProperties.WATERLOGGED)
                ? mimicBlock.setValue(BlockStateProperties.WATERLOGGED, false)
                : mimicBlock;

        setChanged();
        getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    @Override
    public void load(@NotNull CompoundTag tagCompound) {
        super.load(tagCompound);
        loadMimic(tagCompound);
    }

    private void loadMimic(CompoundTag tagCompound) {
        if (tagCompound.contains(MIMIC_TAG)) {
            mimicBlock = NbtUtils.readBlockState(BuiltInRegistries.BLOCK.asLookup(), tagCompound.getCompound(MIMIC_TAG));
        } else {
            mimicBlock = null;
        }
    }

    @Override
    public void saveAdditional(@Nonnull CompoundTag tagCompound) {
        super.saveAdditional(tagCompound);
        saveMimic(tagCompound);
    }

    private void saveMimic(@NotNull CompoundTag tagCompound) {
        if (mimicBlock != null) {
            CompoundTag tag = NbtUtils.writeBlockState(mimicBlock);
            tagCompound.put(MIMIC_TAG, tag);
        }
    }
}
