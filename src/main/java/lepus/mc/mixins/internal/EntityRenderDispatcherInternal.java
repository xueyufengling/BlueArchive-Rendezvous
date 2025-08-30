package lepus.mc.mixins.internal;

import java.util.ArrayList;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class EntityRenderDispatcherInternal {

	public static class Render {

		public static class Args {
			public static EntityRenderDispatcher this_;
			public static Entity entity;
			public static double x, y, z;
			public static float rotationYaw, partialTicks;
			public static PoseStack poseStack;
			public static MultiBufferSource buffer;
			public static int packedLight;
			public static CallbackInfo ci;

			public static final void store(
					EntityRenderDispatcher this_,
					Entity entity,
					double x,
					double y,
					double z,
					float rotationYaw,
					float partialTicks,
					PoseStack poseStack,
					MultiBufferSource buffer,
					int packedLight,
					CallbackInfo ci) {
				// 方法参数
				Args.this_ = this_;
				Args.entity = entity;
				Args.x = x;
				Args.y = y;
				Args.z = z;
				Args.rotationYaw = rotationYaw;
				Args.partialTicks = partialTicks;
				Args.poseStack = poseStack;
				Args.buffer = buffer;
				Args.packedLight = packedLight;
				Args.ci = ci;
			}
		}

		public static class LocalVars {
			public static EntityRenderer<? super Entity> entityrenderer;
			public static Vec3 renderOffset;

			public static final void store(EntityRenderDispatcher this_, Entity entity, float partialTicks) {
				// 局部变量
				LocalVars.entityrenderer = this_.getRenderer(entity);
				LocalVars.renderOffset = entityrenderer.getRenderOffset(entity, partialTicks);
			}
		}

		@FunctionalInterface
		public static interface Callback extends Internal.Callback {
			public void render(
					EntityRenderDispatcher this_,
					Entity entity,
					double x,
					double y,
					double z,
					float rotationYaw,
					float partialTicks,
					PoseStack poseStack,
					MultiBufferSource buffer,
					int packedLight,
					CallbackInfo ci);

			public default void execute() {
				render(Args.this_, Args.entity, Args.x, Args.y, Args.z, Args.rotationYaw, Args.partialTicks, Args.poseStack, Args.buffer, Args.packedLight, Args.ci);
			}
		}

		public static class Callbacks {
			public static ArrayList<Callback> before_pose_popPose = new ArrayList<>();

			public static void addBeforePosePopPoseCallback(Callback func) {
				before_pose_popPose.add(func);
			}
		}
	}
}
