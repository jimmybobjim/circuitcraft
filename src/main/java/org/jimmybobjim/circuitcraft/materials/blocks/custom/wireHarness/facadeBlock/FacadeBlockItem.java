package org.jimmybobjim.circuitcraft.materials.blocks.custom.wireHarness.facadeBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import org.jimmybobjim.circuitcraft.materials.blocks.CCBlocks;
import org.jimmybobjim.circuitcraft.util.ReplaceBlockItemUseContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static org.jimmybobjim.circuitcraft.materials.blocks.custom.base.XYZRotatableBlockEntity.*;

public class FacadeBlockItem extends BlockItem {

    public FacadeBlockItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    private static String getMimickingString(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null) {
            CompoundTag mimic = tag.getCompound("mimic");
            Block value = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(mimic.getString("Name")));
            if (value != null) {
                ItemStack s = new ItemStack(value, 1);
                s.getItem();
                return s.getHoverName().getString();
            }
        }
        return "<unset>";
    }

    private static void userSetMimicBlock(@Nonnull ItemStack item, BlockState mimicBlock, UseOnContext context) {
        Level world = context.getLevel();
        Player player = context.getPlayer();
        setMimicBlock(item, mimicBlock);
        if (world.isClientSide) {
            player.displayClientMessage(Component.literal("facade is mimicking " + mimicBlock.getBlock().getDescriptionId()), false);
        }
    }

    public static void setMimicBlock(@Nonnull ItemStack item, BlockState mimicBlock) {
        CompoundTag tagCompound = new CompoundTag();
        CompoundTag nbt = NbtUtils.writeBlockState(mimicBlock);
        tagCompound.put("mimic", nbt);
        item.setTag(tagCompound);
    }

    public static BlockState getMimicBlock(Level level, @Nonnull ItemStack stack) {
        CompoundTag tagCompound = stack.getTag();
        if (tagCompound == null || !tagCompound.contains("mimic")) {
            return Blocks.COBBLESTONE.defaultBlockState();
        } else {
            return NbtUtils.readBlockState(BuiltInRegistries.BLOCK.asLookup(), tagCompound.getCompound("mimic"));
        }
    }

    @Override
    protected boolean canPlace(@Nonnull BlockPlaceContext context, @Nonnull BlockState state) {
        return true;
    }

    // This function is called when our block item is right clicked on something. When this happens
    // we want to either set the minic block or place the facade block
    @Nonnull
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        ItemStack itemInHand = context.getItemInHand();

        if (!itemInHand.isEmpty()) {

            if (block == CCBlocks.WIRE_HARNESS_BLOCK.get()) {
                // We are hitting a cable block. We want to replace it with a facade block
                FacadeBlock facadeBlock = (FacadeBlock) this.getBlock();
                BlockPlaceContext blockContext = new ReplaceBlockItemUseContext(context);
                BlockState placementState = facadeBlock.getStateForPlacement(blockContext)
                        .setValue(X_ROT, state.getValue(X_ROT))
                        .setValue(Y_ROT, state.getValue(Y_ROT))
                        .setValue(Z_ROT, state.getValue(Z_ROT))
                        .setValue(ATTACHED_FACE, state.getValue(ATTACHED_FACE));

                if (placeBlock(blockContext, placementState)) {
                    SoundType soundtype = world.getBlockState(pos).getBlock().getSoundType(world.getBlockState(pos), world, pos, player);
                    world.playSound(player, pos, soundtype.getPlaceSound(), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                    BlockEntity te = world.getBlockEntity(pos);
                    if (te instanceof FacadeBlockEntity) {
                        ((FacadeBlockEntity) te).setMimicBlock(getMimicBlock(world, itemInHand));
                    }
                    int amount = -1;
                    itemInHand.grow(amount);
                }
            } else if (block == CCBlocks.FACADE_BLOCK.get()) {
                // We are hitting a facade block. We want to copy the block it is mimicing
                BlockEntity te = world.getBlockEntity(pos);
                if (!(te instanceof FacadeBlockEntity facade)) {
                    return InteractionResult.FAIL;
                }
                if (facade.getMimicBlock() == null) {
                    return InteractionResult.FAIL;
                }
                userSetMimicBlock(itemInHand, facade.getMimicBlock(), context);
            } else {
                // We are hitting something else. We want to set that block as what we are going to mimic
                userSetMimicBlock(itemInHand, state, context);
            }
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.FAIL;
        }
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level level, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        if (stack.hasTag()) {
            tooltip.add(Component.literal("Facade is mimicking " + getMimickingString(stack)));
        }
    }
}
