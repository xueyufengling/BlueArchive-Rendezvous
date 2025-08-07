package fw.mixins.defs;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import fw.mixins.internal.GuiGraphicsInternal;
import fw.mixins.internal.Internal;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

@Mixin(targets = { "net.minecraft.client.gui.GuiGraphics" })
public abstract class GuiGraphicsMixin {
	@Inject(method = "Lnet/minecraft/client/gui/GuiGraphics;renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;IIII)V", at = @At(value = "HEAD"))
	private void initParams(LivingEntity entity, Level level, ItemStack stack, int x, int y, int seed, int guiOffset, CallbackInfo ci) {
		GuiGraphicsInternal.RenderItem.Args.store((GuiGraphics) (Object) this, entity, level, stack, x, y, seed, guiOffset, ci);
		GuiGraphicsInternal.RenderItem.LocalVars.store((GuiGraphics) (Object) this);
	}

	@Inject(method = "Lnet/minecraft/client/gui/GuiGraphics;renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;IIII)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;pushPose()V", shift = At.Shift.BEFORE), cancellable = true)
	private void before_pose_pushPose(LivingEntity entity, Level level, ItemStack stack, int x, int y, int seed, int guiOffset, CallbackInfo ci) {
		GuiGraphicsInternal.RenderItem.Args.store((GuiGraphics) (Object) this, entity, level, stack, x, y, seed, guiOffset, ci);
		Internal.Callbacks.invoke(GuiGraphicsInternal.RenderItem.Callbacks.before_pose_pushPose);
	}
}