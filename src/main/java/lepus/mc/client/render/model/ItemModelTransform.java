package lepus.mc.client.render.model;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface ItemModelTransform {
	/**
	 * 判断目标物品模型是否变换
	 * 
	 * @param itemStack
	 * @param itemId    itemStack对应的Id
	 * @return
	 */
	public default boolean condition(ItemStack itemStack, String itemId, ItemDisplayContext displayContext, boolean leftHand) {
		return true;
	}

	/**
	 * 满足condition()后具体的变换操作
	 * 
	 * @param itemStack
	 * @param displayContext
	 * @param leftHand
	 * @param poseStack
	 * @param model
	 */
	public default void transform(ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, BakedModel model) {

	}

	public default void scale(PoseStack poseStack, float x_scale, float y_scale, float z_scale) {
		poseStack.scale(x_scale, y_scale, z_scale);
	}

	public default void scale(PoseStack poseStack, float scale) {
		poseStack.scale(scale, scale, scale);
	}

	public default void shift(PoseStack poseStack, float x, float y, float z) {
		poseStack.translate(x, y, z);
	}
}
