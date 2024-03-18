package org.jimmybobjim.circuitcraft.materials.blocks;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jimmybobjim.circuitcraft.CircuitCraft;
import org.jimmybobjim.circuitcraft.materials.blocks.custom.wireHarness.facadeBlock.FacadeBlockEntity;
import org.jimmybobjim.circuitcraft.materials.blocks.custom.wireHarness.wireHarnessBlock.WireHarnessBlockEntity;

@SuppressWarnings({"ConstantConditions", "unused"})
public class CCBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, CircuitCraft.MODID);

    public static final RegistryObject<BlockEntityType<WireHarnessBlockEntity>> WIRE_HARNESS_BE =
            BLOCK_ENTITIES.register("wire_harness_be",
                    () -> BlockEntityType.Builder.of(WireHarnessBlockEntity::new,
                            CCBlocks.WIRE_HARNESS_BLOCK.get()).build(null));

    public static final RegistryObject<BlockEntityType<FacadeBlockEntity>> FACADE_BE =
            BLOCK_ENTITIES.register("facade_be",
                    () -> BlockEntityType.Builder.of(FacadeBlockEntity::new,
                            CCBlocks.FACADE_BLOCK.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
