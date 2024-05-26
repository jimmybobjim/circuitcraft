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

public class CCCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CircuitCraft.MODID);

    @SuppressWarnings("unused")
    public static final RegistryObject<CreativeModeTab>
            MATERIALS = register("materials", Items.IRON_INGOT, Component.literal("materials"), (displayParameters, output) -> {

            }),

            MACHINES = register("machines", Items.FURNACE, Component.literal("machines"), (displayParameters, output) -> {

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
