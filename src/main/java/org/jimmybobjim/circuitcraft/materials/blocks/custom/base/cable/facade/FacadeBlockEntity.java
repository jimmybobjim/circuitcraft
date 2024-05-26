package org.jimmybobjim.circuitcraft.materials.blocks.custom.base.cable.facade;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jimmybobjim.circuitcraft.materials.blocks.custom.base.cable.cable.CableBlock;
import org.jimmybobjim.circuitcraft.materials.blocks.custom.base.cable.cable.CableBlockEntity;

import javax.annotation.Nullable;

public class FacadeBlockEntity extends CableBlockEntity {
    public static final String MIMIC_TAG = "mimic";
    public static final String WIDTH_TAG = "width";

    @Nullable
    private BlockState mimicBlock = null;
    private float width = -1f;

    public FacadeBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(pPos, pBlockState);
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
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        CompoundTag nbtTag = new CompoundTag();
        saveMimic(nbtTag);
//        saveWidth(nbtTag);
        return ClientboundBlockEntityDataPacket.create(this, (entity) -> nbtTag);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag updateTag = super.getUpdateTag();
        saveMimic(updateTag);
//        saveWidth(updateTag);
        return updateTag;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        loadMimic(tag);
//        loadWidth(tag);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        saveMimic(tag);
//        saveWidth(tag);
    }

    @Nullable
    public BlockState getMimicBlock() {
        return mimicBlock;
    }

    public void setMimicBlock(BlockState mimicBlock) {
        this.mimicBlock = mimicBlock;
        setChanged();
        getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    private void loadMimic(CompoundTag tagCompound) {
        if (tagCompound.contains(MIMIC_TAG)) {
            mimicBlock = NbtUtils.readBlockState(BuiltInRegistries.BLOCK.asLookup(), tagCompound.getCompound(MIMIC_TAG));
        } else {
            mimicBlock = null;
        }
    }

    private void saveMimic(@NotNull CompoundTag tagCompound) {
        if (mimicBlock != null) {
            CompoundTag tag = NbtUtils.writeBlockState(mimicBlock);
            tagCompound.put(MIMIC_TAG, tag);
        }
    }

    public void setWidth(float width) {
        this.width = width;
        setChanged();
        getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    private void loadWidth(CompoundTag tagCompound) {
        if (tagCompound.contains(WIDTH_TAG)) {
            width = tagCompound.getFloat(WIDTH_TAG);
        } else {
            width = -1f;
        }
    }

    private void saveWidth(@NotNull CompoundTag tagCompound) {
        CompoundTag tag = new CompoundTag();
        tag.putFloat(WIDTH_TAG, width);
        tagCompound.put(MIMIC_TAG, tag);
    }

    @Override
    public @NotNull ModelData getModelData() {
        return ModelData.builder().with(CableBlock.FACADE_ID, mimicBlock).with(CableBlock.WIDTH, width).build();
    }
}
