package org.jimmybobjim.circuitcraft.materials.items;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jimmybobjim.circuitcraft.CircuitCraft;
import org.jimmybobjim.circuitcraft.materials.blocks.CCBlocks;
import org.jimmybobjim.circuitcraft.materials.blocks.custom.enumProperties.WireWidth;
import org.jimmybobjim.circuitcraft.materials.blocks.custom.wireHarness.facadeBlock.FacadeBlockItem;
import org.jimmybobjim.circuitcraft.materials.items.custom.WireItem;
import org.jimmybobjim.circuitcraft.materials.materials.Metal;

import java.awt.*;

public class CCItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CircuitCraft.MODID);

    //items
    public static final RegistryObject<Item>
            WIRE_COPPER_RED_X1 = ITEMS.register("wire_copper_red_x1",
                    () -> new WireItem(WireWidth.x1, Metal.COPPER, Color.RED, new Item.Properties()));

    //block items
    @SuppressWarnings("unused")
    public static final RegistryObject<Item>
            FACADE_BI = ITEMS.register("facade", () -> new FacadeBlockItem(CCBlocks.FACADE_BLOCK.get(),
            new Item.Properties()));

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}
