package org.jimmybobjim.circuitcraft.util;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;

public class ReplaceBlockItemUseContext extends BlockPlaceContext {
    public ReplaceBlockItemUseContext(UseOnContext pContext) {
        super(pContext);
        replaceClicked = true;
    }
}
