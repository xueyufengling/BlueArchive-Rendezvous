package lepus.mc.mixins.defs;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;

import lepus.mc.mixins.internal.Internal;
import lepus.mc.mixins.internal.ItemRendererInternal;
import lepus.mc.mixins.internal.TargetDescriptors;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

@Mixin(targets = { "net.minecraft.client.renderer.entity.ItemRenderer" })
public abstract class ItemRendererMixin implements ResourceManagerReloadListener {
	@Inject(method = "render", at = @At(value = "HEAD"))
	private void initParams(ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay, BakedModel model, CallbackInfo ci) {
		ItemRendererInternal.Render.Args.store((ItemRenderer) (Object) this, itemStack, displayContext, leftHand, poseStack, bufferSource, combinedLight, combinedOverlay, model, ci);
	}

	@Inject(method = "render", at = @At(value = "INVOKE", target = TargetDescriptors.LPoseStack.pushPose, shift = Shift.BEFORE), cancellable = true)
	private void before_poseStack_pushPose(ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay, BakedModel model, CallbackInfo ci) {
		ItemRendererInternal.Render.Args.store((ItemRenderer) (Object) this, itemStack, displayContext, leftHand, poseStack, bufferSource, combinedLight, combinedOverlay, model, ci);
		Internal.Callbacks.invoke(ItemRendererInternal.Render.Callbacks.before_poseStack_pushPose);
	}

	@Inject(method = "render", at = @At(value = "RETURN"), cancellable = true)
	private void before_return(ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay, BakedModel model, CallbackInfo ci) {
		ItemRendererInternal.Render.Args.store((ItemRenderer) (Object) this, itemStack, displayContext, leftHand, poseStack, bufferSource, combinedLight, combinedOverlay, model, ci);
		Internal.Callbacks.invoke(ItemRendererInternal.Render.Callbacks.before_return);
	}

	@ModifyVariable(method = "render", at = @At(value = "INVOKE", target = TargetDescriptors.LPoseStack.translate, shift = At.Shift.BY, by = -2), index = 8)
	private BakedModel before_poseStack_translate(BakedModel orig) {
		Internal.Callbacks.invoke(ItemRendererInternal.Render.Callbacks.before_poseStack_translate);
		return ItemRendererInternal.Render.Args.p_model;
	}

	@WrapOperation(method = "render", at = @At(value = "INVOKE", target = TargetDescriptors.LItemStack.hasFoil))
	private boolean itemStack_hasFoil(ItemStack itemStack, Operation<Boolean> orig) {
		return ItemRendererInternal.Render.hasFoil.hasFoil(itemStack);
	}
}
