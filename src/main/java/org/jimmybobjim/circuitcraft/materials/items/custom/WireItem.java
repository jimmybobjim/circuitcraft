package org.jimmybobjim.circuitcraft.materials.items.custom;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.jimmybobjim.circuitcraft.materials.blocks.custom.enumProperties.WireWidth;
import org.jimmybobjim.circuitcraft.materials.blocks.custom.wireHarness.WireHarnessHoldable;
import org.jimmybobjim.circuitcraft.materials.materials.Metal;
import org.jimmybobjim.circuitcraft.util.BakedModelHelper;

import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;
import java.util.List;

import static org.jimmybobjim.circuitcraft.util.Util.v3;

public class WireItem extends Item implements WireHarnessHoldable {
    private final WireWidth wireWidth;
    private final Color wireInsulationColor;
    private final Metal wireMetal;
    public WireItem(WireWidth wireWidth, Metal wireMetal, Color wireInsulationColor, Properties pProperties) {
        super(pProperties);

        this.wireWidth = wireWidth;
        this.wireMetal = wireMetal;
        this.wireInsulationColor = wireInsulationColor;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.literal("§8§o" + wireMetal.chemicalFormula()));
    }

    @Override
    public WireWidth getWidth() {
        return wireWidth;
    }

    @Override
    public BakedModelHelper getWireTexture(int pos) {
//        initTextures();

        TextureAtlasSprite
                wire_harness_wire_insulation_side = BakedModelHelper.getTexture("block/wire_harness_block/wire/wire_insulation_side"),
                wire_harness_wire_insulation_front = BakedModelHelper.getTexture("block/wire_harness_block/wire/wire_insulation_front"),
                wire_harness_wire_metal_front = BakedModelHelper.getTexture("block/wire_harness_block/wire/wire_metal_front");

        int width = getWidth().getWidth();

        BakedModelHelper helper = new BakedModelHelper();

        Vec3
                pos1 = v3(((double) width/4)+pos, 0.5, 0),
                pos2 = v3(((double) width*0.75)+pos, ((double) width/2)+0.5, 16);

        helper.cube(pos1, pos2, wireInsulationColor, BakedModelHelper.cubeSprite(
                wire_harness_wire_insulation_side,
                wire_harness_wire_insulation_side,
                wire_harness_wire_insulation_front,
                wire_harness_wire_insulation_front,
                wire_harness_wire_insulation_side,
                wire_harness_wire_insulation_side
        ));

        double
                x1 = pos1.x, y1 = pos1.y, z1 = pos1.z,
                x2 = pos2.x, y2 = pos2.y, z2 = pos2.z;

        helper.quad(v3(x1,y2,z1), v3(x2,y2,z1), v3(x2,y1,z1), v3(x1,y1,z1), wireMetal.color(), wire_harness_wire_metal_front);
        helper.quad(v3(x1,y1,z2), v3(x2,y1,z2), v3(x2,y2,z2) ,v3(x1,y2,z2), wireMetal.color(), wire_harness_wire_metal_front);

        return helper;
    }

    public Metal getWireMetal() {
        return wireMetal;
    }

    public Color getWireInsulationColor() {
        return wireInsulationColor;
    }
}
