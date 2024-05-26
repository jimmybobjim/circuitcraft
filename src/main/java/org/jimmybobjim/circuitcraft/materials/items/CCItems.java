package org.jimmybobjim.circuitcraft.materials.items;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jimmybobjim.circuitcraft.CircuitCraft;
import org.jimmybobjim.circuitcraft.materials.blocks.CCBlocks;
import org.jimmybobjim.circuitcraft.materials.blocks.custom.base.cable.facade.FacadeBlock;
import org.jimmybobjim.circuitcraft.materials.blocks.custom.base.cable.facade.FacadeBlockItem;

public class CCItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CircuitCraft.MODID);

    //items
//    public static final RegistryObject<Item>


    //block items
    public static final RegistryObject<BlockItem>
            FACADE_BI = ITEMS.register("facade_block", () -> new FacadeBlockItem((FacadeBlock) CCBlocks.FACADE_BLOCK.get(), new Item.Properties()));

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}
