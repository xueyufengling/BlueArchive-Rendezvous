package fw.mixins.defs;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import fw.mixins.internal.EntityRenderDispatcherInternal;
import fw.mixins.internal.Internal;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.entity.Entity;

@Mixin(targets = { "net.minecraft.client.renderer.entity.EntityRenderDispatcher" })
public abstract class EntityRenderDispatcherMixin implements ResourceManagerReloadListener {
	@Inject(method = "render", at = @At(value = "HEAD"))
	private void initParams(Entity entity,
			double x,
			double y,
			double z,
			float rotationYaw,
			float partialTicks,
			PoseStack poseStack,
			MultiBufferSource buffer,
			int packedLight,
			CallbackInfo ci) {
		EntityRenderDispatcherInternal.Render.Args.store((EntityRenderDispatcher) (Object) this, entity, x, y, z, rotationYaw, partialTicks, poseStack, buffer, packedLight, ci);
		EntityRenderDispatcherInternal.Render.LocalVars.store((EntityRenderDispatcher) (Object) this, entity, partialTicks);
	}

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;popPose()V", shift = Shift.BEFORE), cancellable = true)
	private void before_pose_popPose(Entity entity,
			double x,
			double y,
			double z,
			float rotationYaw,
			float partialTicks,
			PoseStack poseStack,
			MultiBufferSource buffer,
			int packedLight,
			CallbackInfo ci) {
		EntityRenderDispatcherInternal.Render.Args.store((EntityRenderDispatcher) (Object) this, entity, x, y, z, rotationYaw, partialTicks, poseStack, buffer, packedLight, ci);
		Internal.Callbacks.invoke(EntityRenderDispatcherInternal.Render.Callbacks.before_pose_popPose);
	}
}
