package org.jimmybobjim.circuitcraft.materials.blocks;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;
import org.jimmybobjim.circuitcraft.CircuitCraft;
import org.jimmybobjim.circuitcraft.materials.blocks.custom.base.cable.cable.CableBlock;
import org.jimmybobjim.circuitcraft.materials.blocks.custom.base.cable.facade.FacadeBlock;
import org.jimmybobjim.circuitcraft.materials.items.CCItems;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.Supplier;

public class CCBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, CircuitCraft.MODID);

    public static final RegistryObject<Block>
            FACADE_BLOCK = registerBlock("facade_block", () -> new FacadeBlock(BlockBehaviour.Properties.copy(Blocks.STONE))),
            CABLE_BLOCK = registerBlockWithItem("cable_block", () -> new CableBlock(BlockBehaviour.Properties.copy(Blocks.STONE), 4));

    private static <T extends Block> RegistryObject<T> registerBlockWithItem(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);                                //generate block
        CCItems.ITEMS.register(name, () -> new BlockItem(toReturn.get(), new Item.Properties())); //generate block-item
        return toReturn;
    }

    private static <T extends Block> RegistryObject<T> registerBlockWithItemHoverText(String name, Component hoverText, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);

        CCItems.ITEMS.register(name, () -> new BlockItem(toReturn.get(), new Item.Properties()) {
            @Override
            @ParametersAreNonnullByDefault
            public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
                super.appendHoverText(pStack, pLevel, pTooltip, pFlag);

                pTooltip.add(hoverText);
            }
        });

        return toReturn;
    }

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }

    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
    }
}
