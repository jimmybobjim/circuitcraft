package org.jimmybobjim.circuitcraft.api.materialgen;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;

public record Registries(
        DeferredRegister<Item> itemRegister,
        DeferredRegister<Block> blockRegister,
        DeferredRegister<BlockEntityType<?>> blockEntityRegister,
        DeferredRegister<Fluid> fluidRegister,
        DeferredRegister<FluidType> fluidTypeRegister
) {
}
