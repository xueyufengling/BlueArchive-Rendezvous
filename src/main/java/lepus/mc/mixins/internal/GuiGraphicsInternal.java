package lepus.mc.mixins.internal;

import java.util.ArrayList;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class GuiGraphicsInternal {

	public static class RenderItem {

		public static class Args {
			public static GuiGraphics this_;
			public static LivingEntity entity;
			public static Level level;
			public static ItemStack stack;
			public static int x, y, seed, guiOffset;
			public static CallbackInfo ci;

			public static final void store(GuiGraphics this_, LivingEntity entity, Level level, ItemStack stack, int x, int y, int seed, int guiOffset, CallbackInfo ci) {
				// 方法参数
				Args.this_ = this_;
				Args.entity = entity;
				Args.level = level;
				Args.stack = stack;
				Args.x = x;
				Args.y = y;
				Args.seed = seed;
				Args.guiOffset = guiOffset;
				Args.ci = ci;
			}
		}

		public static class LocalVars {
			/**
			 * 全局矩阵变换
			 */
			public static PoseStack pose;

			public static final void store(GuiGraphics this_) {
				// 局部变量
				LocalVars.pose = this_.pose();
			}
		}

		@FunctionalInterface
		public static interface Callback extends Internal.Callback {
			/**
			 * 在渲染物品前执行的代码，需要被覆写。 其中x、y为待渲染物品的左上角顶点坐标
			 * 
			 * @param this_
			 * @param entity
			 * @param level
			 * @param stack     要渲染的物品stack
			 * @param x         渲染的x坐标，以GUI左上角为原点，向右为正
			 * @param y         渲染的y坐标，以GUI左上角为原点，向下为正
			 * @param seed
			 * @param guiOffset 渲染的深度
			 * @param ci
			 */
			public void renderItem(GuiGraphics this_, LivingEntity entity, Level level, ItemStack stack, int x, int y, int seed, int guiOffset, CallbackInfo ci);

			public default void execute() {
				renderItem(Args.this_, Args.entity, Args.level, Args.stack, Args.x, Args.y, Args.seed, Args.guiOffset, Args.ci);
			}
		}

		public static class Callbacks {

			public static ArrayList<Callback> before_pose_pushPose = new ArrayList<>();

			public static void addBeforePosePushPoseCallback(Callback func) {
				before_pose_pushPose.add(func);
			}
		}
	}
}
