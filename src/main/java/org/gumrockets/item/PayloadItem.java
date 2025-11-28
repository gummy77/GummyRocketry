package org.gumrockets.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import org.gumrockets.component.PayloadTypes;
import org.gumrockets.component.RocketPart;
import org.gumrockets.registry.ComponentRegistry;

import java.util.List;

public class PayloadItem extends Item {
    PayloadTypes payloadType;
    private float payloadWeight = 1f;

    public PayloadItem(Settings settings, PayloadTypes _type) {
        super(settings);
        this.payloadType = _type;
    }
    public PayloadItem(Settings settings, PayloadTypes _type, float payloadWeight) {
        super(settings);
        this.payloadType = _type;
        this.payloadWeight = payloadWeight;
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
        tooltip.add(Text.of("§7payloadWeight: §2" + payloadWeight+ "kg"));
    }

    public PayloadTypes getPayloadType() {
        return payloadType;
    }

    public float getPayloadWeight() { return payloadWeight; }
}
