package org.gumrockets.client.model;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import org.gumrockets.entity.PayloadEntity;

public class DeployedParachuteModel extends EntityModel<PayloadEntity> {
    private final ModelPart Parachute;

	public DeployedParachuteModel(ModelPart root) {
        this.Parachute = root.getChild("Parachute");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData Parachute = modelPartData.addChild("Parachute", ModelPartBuilder.create().uv(0, 0).cuboid(8.0F, -24.0F, -8.0F, 16.0F, 1.0F, 16.0F, new Dilation(0.0F))
                .uv(0, 17).cuboid(8.0F, -24.0F, 8.0F, 16.0F, 1.0F, 16.0F, new Dilation(0.0F))
                .uv(0, 34).cuboid(-8.0F, -24.0F, 8.0F, 16.0F, 1.0F, 16.0F, new Dilation(0.0F))
                .uv(0, 51).cuboid(-24.0F, -24.0F, 8.0F, 16.0F, 1.0F, 16.0F, new Dilation(0.0F))
                .uv(64, 0).cuboid(-24.0F, -24.0F, -8.0F, 16.0F, 1.0F, 16.0F, new Dilation(0.0F))
                .uv(64, 17).cuboid(-24.0F, -24.0F, -24.0F, 16.0F, 1.0F, 16.0F, new Dilation(0.0F))
                .uv(64, 34).cuboid(-8.0F, -24.0F, -24.0F, 16.0F, 1.0F, 16.0F, new Dilation(0.0F))
                .uv(64, 51).cuboid(8.0F, -24.0F, -24.0F, 16.0F, 1.0F, 16.0F, new Dilation(0.0F))
                .uv(0, 68).cuboid(23.0F, -23.0F, -24.0F, 1.0F, 15.0F, 16.0F, new Dilation(0.0F))
                .uv(34, 68).cuboid(23.0F, -23.0F, -8.0F, 1.0F, 15.0F, 16.0F, new Dilation(0.0F))
                .uv(68, 68).cuboid(23.0F, -23.0F, 8.0F, 1.0F, 15.0F, 16.0F, new Dilation(0.0F))
                .uv(0, 99).cuboid(-24.0F, -23.0F, 8.0F, 1.0F, 15.0F, 16.0F, new Dilation(0.0F))
                .uv(34, 99).cuboid(-24.0F, -23.0F, -8.0F, 1.0F, 15.0F, 16.0F, new Dilation(0.0F))
                .uv(68, 99).cuboid(-24.0F, -23.0F, -24.0F, 1.0F, 15.0F, 16.0F, new Dilation(0.0F))
                .uv(1, 234).cuboid(-23.0F, -23.0F, 23.0F, 46.0F, 15.0F, 1.0F, new Dilation(0.0F))
                .uv(147, 232).cuboid(-23.0F, -23.0F, -24.0F, 46.0F, 15.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 16.0F, 0.0F));

        Parachute.addChild("cube_r1", ModelPartBuilder.create().uv(106, 100).cuboid(0.0F, -1.0F, -1.0F, 1.0F, 44.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(22.7F, -9.0F, 23.4F, -0.7854F, 0.7854F, 0.0F));
        Parachute.addChild("cube_r2", ModelPartBuilder.create().uv(102, 100).cuboid(0.0F, -1.0F, -1.0F, 1.0F, 44.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-22.7F, -9.0F, 23.4F, -0.6155F, 0.5236F, -0.9553F));
        Parachute.addChild("cube_r3", ModelPartBuilder.create().uv(4, 130).cuboid(0.0F, -1.0F, -1.0F, 1.0F, 43.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-22.7F, -9.0F, -22.0F, 0.7854F, 0.7854F, 0.0F));
        Parachute.addChild("cube_r4", ModelPartBuilder.create().uv(0, 130).cuboid(0.0F, -1.0F, -1.0F, 1.0F, 43.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(22.7F, -9.0F, -22.0F, 0.6155F, 0.5236F, 0.9553F));
        return TexturedModelData.of(modelData, 256, 256);
    }
    @Override
    public void setAngles(PayloadEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) { }
    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, int color) { }
}