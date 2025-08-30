package lepus.mc.mixins.internal;

import java.util.ArrayList;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import lepus.mc.resources.ResourceLocations;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class ItemRendererInternal {
	@FunctionalInterface
	public static interface ItemStackHasFoilResolver {
		/**
		 * 物品是否渲染动态贴图，例如附魔的流光特效
		 * 
		 * @param itemStack
		 * @return
		 */
		public boolean hasFoil(ItemStack itemStack);

		public static ItemStackHasFoilResolver Default = (ItemStack itemStack) -> itemStack.hasFoil();

		public static ItemStackHasFoilResolver AlwaysTrue = (ItemStack itemStack) -> true;

		public static ItemStackHasFoilResolver AlwaysFalse = (ItemStack itemStack) -> false;
	}

	private static BakedModel EMPTY_MODEL = null;

	/**
	 * 获取空物品模型
	 * 
	 * @return
	 */
	public static final BakedModel emptyItemModel() {
		if (EMPTY_MODEL == null)
			EMPTY_MODEL = Render.Args.this_.getItemModelShaper().getModelManager().getModel(new ModelResourceLocation(ResourceLocations.build("minecraft:air"), "inventory"));
		return EMPTY_MODEL;
	}

	public static class Render {
		public static ItemStackHasFoilResolver hasFoil = ItemStackHasFoilResolver.Default;

		/**
		 * 是否渲染物品附魔的流光贴图
		 * 
		 * @param glint
		 */
		public static void renderItemGlint(boolean glint) {
			if (glint)
				hasFoil = ItemStackHasFoilResolver.Default;
			else
				hasFoil = ItemStackHasFoilResolver.AlwaysFalse;
		}

		public static class Args {
			public static ItemRenderer this_;
			public static ItemStack itemStack;
			public static ItemDisplayContext displayContext;
			public static boolean leftHand;
			public static PoseStack poseStack;
			public static MultiBufferSource bufferSource;
			public static int combinedLight;
			public static int combinedOverlay;
			public static BakedModel p_model;
			public static CallbackInfo ci;

			public static final void store(
					ItemRenderer this_,
					ItemStack itemStack,
					ItemDisplayContext displayContext,
					boolean leftHand,
					PoseStack poseStack,
					MultiBufferSource bufferSource,
					int combinedLight,
					int combinedOverlay,
					BakedModel p_model,
					CallbackInfo ci) {
				// 方法参数
				Args.this_ = this_;
				Args.itemStack = itemStack;
				Args.displayContext = displayContext;
				Args.leftHand = leftHand;
				Args.poseStack = poseStack;
				Args.bufferSource = bufferSource;
				Args.combinedLight = combinedLight;
				Args.combinedOverlay = combinedOverlay;
				Args.p_model = p_model;
				Args.ci = ci;
			}
		}

		@FunctionalInterface
		public static interface Callback extends Internal.Callback {
			public void render(ItemRenderer this_,
					ItemStack itemStack,
					ItemDisplayContext displayContext,
					boolean leftHand,
					PoseStack poseStack,
					MultiBufferSource bufferSource,
					int combinedLight,
					int combinedOverlay,
					BakedModel p_model,
					CallbackInfo ci);

			public default void execute() {
				render(Args.this_, Args.itemStack, Args.displayContext, Args.leftHand, Args.poseStack, Args.bufferSource, Args.combinedLight, Args.combinedOverlay, Args.p_model, Args.ci);
			}
		}

		public static class Callbacks {
			public static ArrayList<Callback> before_poseStack_pushPose = new ArrayList<>();

			public static void addBeforePoseStackPushPoseCallback(Callback func) {
				before_poseStack_pushPose.add(func);
			}

			// render()中pose.translate()前
			public static ArrayList<Callback> before_poseStack_translate = new ArrayList<>();

			public static void addBeforePoseStackTranslateCallback(Callback func) {
				before_poseStack_translate.add(func);
			}

			// render()返回前
			public static ArrayList<Callback> before_return = new ArrayList<>();

			public static void addBeforeReturnCallback(Callback func) {
				before_return.add(func);
			}
		}
	}
}
