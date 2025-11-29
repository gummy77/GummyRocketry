package org.gumrockets.client.model;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import org.gumrockets.entity.PayloadEntity;

public class FallenParachuteModel extends EntityModel<PayloadEntity> {
    private final ModelPart root;

    public FallenParachuteModel(ModelPart root) {
        this.root = root.getChild("root");
    }
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        ModelPartData bb_main = modelPartData.addChild("root", ModelPartBuilder.create().uv(0, 0).cuboid(-8.0F, -2.0F, -8.0F, 16.0F, 2.0F, 32.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        bb_main.addChild("string_1", ModelPartBuilder.create().uv(0, 36).cuboid(-11.0F, -1.0F, 0.0F, 21.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-4.0F, -1.0F, -18.0F, 3.1416F, 1.309F, 0.0F));
        bb_main.addChild("string_2", ModelPartBuilder.create().uv(0, 34).cuboid(-11.0F, -1.0F, 0.0F, 21.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(4.0F, 0.0F, -18.0F, 0.0F, 1.8326F, 0.0F));

        return TexturedModelData.of(modelData, 128, 128);
    }

    @Override
    public void setAngles(PayloadEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) { }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, int color) { }
}