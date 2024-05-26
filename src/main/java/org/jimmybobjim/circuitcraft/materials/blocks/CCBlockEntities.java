package org.jimmybobjim.circuitcraft.materials.blocks;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jimmybobjim.circuitcraft.CircuitCraft;
import org.jimmybobjim.circuitcraft.materials.blocks.custom.base.cable.cable.CableBlockEntity;
import org.jimmybobjim.circuitcraft.materials.blocks.custom.base.cable.facade.FacadeBlockEntity;

@SuppressWarnings({"ConstantConditions", "unused"})
public class CCBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, CircuitCraft.MODID);

    public static final RegistryObject<BlockEntityType<FacadeBlockEntity>> FACADE_BE =
            BLOCK_ENTITIES.register("facade_be", () -> BlockEntityType.Builder.of(FacadeBlockEntity::new, CCBlocks.FACADE_BLOCK.get()).build(null));

    public static final RegistryObject<BlockEntityType<CableBlockEntity>> CABLE_BE =
            BLOCK_ENTITIES.register("cable_be", () -> BlockEntityType.Builder.of(CableBlockEntity::new, CCBlocks.CABLE_BLOCK.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
