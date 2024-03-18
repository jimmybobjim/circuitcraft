package org.jimmybobjim.circuitcraft.creativeTabs;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.jimmybobjim.circuitcraft.CircuitCraft;
import org.jimmybobjim.circuitcraft.materials.blocks.CCBlocks;
import org.jimmybobjim.circuitcraft.materials.items.CCItems;

public class CCCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CircuitCraft.MODID);

    @SuppressWarnings("unused")
    public static final RegistryObject<CreativeModeTab>
            MATERIALS = register("materials", Items.IRON_INGOT, Component.literal("materials"), (displayParameters, output) -> {
                output.accept(CCItems.WIRE_COPPER_RED_X1.get());
            }),

            MACHINES = register("machines", Items.FURNACE, Component.literal("machines"), (displayParameters, output) -> {
                output.accept(CCBlocks.WIRE_HARNESS_BLOCK.get());
                output.accept(CCBlocks.FACADE_BLOCK.get());
            });

    private static RegistryObject<CreativeModeTab> register(String name, ItemLike icon, Component title,
                                                            CreativeModeTab.DisplayItemsGenerator generator) {
        return CREATIVE_MODE_TABS.register(name, () -> CreativeModeTab.builder().icon(() -> new ItemStack(icon))
                .title(title).displayItems(generator).build()
        );
    }

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
