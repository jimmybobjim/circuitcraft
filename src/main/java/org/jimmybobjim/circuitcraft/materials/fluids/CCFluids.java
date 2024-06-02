package org.jimmybobjim.circuitcraft.materials.fluids;

import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.jimmybobjim.circuitcraft.CircuitCraft;
import org.jimmybobjim.circuitcraft.materials.blocks.CCBlocks;
import org.jimmybobjim.circuitcraft.materials.items.CCItems;

import java.awt.*;

public class CCFluids {
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, CircuitCraft.MODID);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, CircuitCraft.MODID);

    static  {
        FluidHelper.create("test", new Color(-1), new Color(-1)).slopeFindDistance(2).levelDecreasePerBlock(1).fluidProperties(FluidType.Properties.create().viscosity(5).density(15)).build(FLUIDS, FLUID_TYPES, CCItems.ITEMS, CCBlocks.BLOCKS);

    }

    public static void register(IEventBus eventBus) {
        eventBus.register(FLUID_TYPES);
        eventBus.register(FLUIDS);
    }
}
