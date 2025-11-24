package org.gumrockets.item;

import net.minecraft.item.Item;
import org.gumrockets.component.PayloadTypes;

public class PayloadItem extends Item {
    PayloadTypes payloadType;
    public PayloadItem(Settings settings, PayloadTypes _type) {
        super(settings);
        this.payloadType = _type;
    }

    public PayloadTypes getPayloadType() {
        return payloadType;
    }
}
