package org.gumrockets.client.model;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import org.gumrockets.entity.PayloadEntity;

public class UndeployedParachuteModel extends EntityModel<PayloadEntity> {
    private final ModelPart Parachute2;

    public UndeployedParachuteModel(ModelPart root) {
        this.Parachute2 = root.getChild("Parachute2");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        ModelPartData Parachute2 = modelPartData.addChild("Parachute2", ModelPartBuilder.create().uv(34, 68).cuboid(7.0F, -23.0F, -8.0F, 1.0F, 15.0F, 16.0F, new Dilation(0.0F))
                .uv(34, 68).cuboid(7.0F, -38.0F, -8.0F, 1.0F, 15.0F, 16.0F, new Dilation(0.0F))
                .uv(34, 99).cuboid(-8.0F, -23.0F, -8.0F, 1.0F, 15.0F, 16.0F, new Dilation(0.0F))
                .uv(34, 99).cuboid(-8.0F, -38.0F, -8.0F, 1.0F, 15.0F, 16.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 16.0F, 0.0F));

        ModelPartData cube_r5 = Parachute2.addChild("cube_r5", ModelPartBuilder.create().uv(106, 100).cuboid(0.0F, -1.0F, -1.0F, 1.0F, 44.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(6.7F, -9.0F, 7.4F, -0.1745F, 0.7854F, 0.0F));
        ModelPartData cube_r6 = Parachute2.addChild("cube_r6", ModelPartBuilder.create().uv(102, 100).cuboid(0.0F, -1.0F, -1.0F, 1.0F, 44.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-6.7F, -9.0F, 7.4F, -0.1719F, 0.7703F, -0.2444F));
        ModelPartData cube_r7 = Parachute2.addChild("cube_r7", ModelPartBuilder.create().uv(4, 130).cuboid(0.0F, -1.0F, -1.0F, 1.0F, 43.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-6.7F, -9.0F, -6.0F, 0.1745F, 0.7854F, 0.0F));
        ModelPartData cube_r8 = Parachute2.addChild("cube_r8", ModelPartBuilder.create().uv(0, 130).cuboid(0.0F, -1.0F, -1.0F, 1.0F, 43.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(6.7F, -9.0F, -6.0F, 0.1719F, 0.7703F, 0.2444F));
        ModelPartData cube_r9 = Parachute2.addChild("cube_r9", ModelPartBuilder.create().uv(36, 70).cuboid(-1.0F, -1.0F, -6.0F, 1.0F, 15.0F, 14.0F, new Dilation(0.0F))
                .uv(36, 70).cuboid(-1.0F, 14.0F, -6.0F, 1.0F, 15.0F, 14.0F, new Dilation(0.0F)), ModelTransform.of(1.0F, -37.0F, -7.0F, 0.0F, -1.5708F, 0.0F));
        ModelPartData cube_r10 = Parachute2.addChild("cube_r10", ModelPartBuilder.create().uv(36, 70).cuboid(-1.0F, -1.0F, -6.0F, 1.0F, 15.0F, 14.0F, new Dilation(0.0F))
                .uv(36, 70).cuboid(-1.0F, 14.0F, -6.0F, 1.0F, 15.0F, 14.0F, new Dilation(0.0F)), ModelTransform.of(1.0F, -37.0F, 8.0F, 0.0F, -1.5708F, 0.0F));

        return TexturedModelData.of(modelData, 256, 256);
    }
    @Override
    public void setAngles(PayloadEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }
    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, int color) { }
}