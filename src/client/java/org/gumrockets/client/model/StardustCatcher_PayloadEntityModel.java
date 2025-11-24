package org.gumrockets.client.model;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import org.gumrockets.entity.PayloadEntity;

public class StardustCatcher_PayloadEntityModel extends EntityModel<PayloadEntity> {
	private final ModelPart bb_main;

	public StardustCatcher_PayloadEntityModel(ModelPart root) {
		this.bb_main = root.getChild("bb_main");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData bb_main = modelPartData.addChild("bb_main", ModelPartBuilder.create().uv(0, 0).cuboid(-2.0F, -2.0F, -2.0F, 4.0F, 2.0F, 4.0F, new Dilation(0.0F))
		.uv(0, 6).cuboid(-2.0F, -9.0F, -2.0F, 4.0F, 2.0F, 4.0F, new Dilation(0.0F))
		.uv(6, 12).cuboid(-1.0F, -7.0F, -2.0F, 2.0F, 5.0F, 1.0F, new Dilation(0.0F))
		.uv(12, 12).cuboid(-1.0F, -7.0F, 1.0F, 2.0F, 5.0F, 1.0F, new Dilation(0.0F))
		.uv(18, 7).cuboid(-2.0F, -7.0F, -2.0F, 1.0F, 5.0F, 4.0F, new Dilation(0.0F))
		.uv(14, -2).cuboid(1.0F, -7.0F, -2.0F, 1.0F, 5.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		return TexturedModelData.of(modelData, 32, 32);
	}
	@Override
	public void setAngles(PayloadEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, int colour) {
		matrices.push();
		matrices.scale(1.0F, -1.0F, 1.0F);
		bb_main.render(matrices, vertexConsumer, light, overlay);
		matrices.pop();
	}
}