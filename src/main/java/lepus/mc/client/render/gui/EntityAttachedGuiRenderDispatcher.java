package lepus.mc.client.render.gui;

import java.util.ArrayList;

import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import lepus.mc.client.render.RenderDispatcher;
import lepus.mc.mixins.internal.EntityRenderDispatcherInternal;
import lepus.mc.mixins.internal.LevelRendererInternal;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EntityAttachedGuiRenderDispatcher extends RenderDispatcher<Gui> {
	public static final EntityAttachedGuiRenderDispatcher DISPATCHER;

	static {
		DISPATCHER = RenderDispatcher.register(EntityAttachedGuiRenderDispatcher.class, EntityTask.class, Object.class, EntityRenderDispatcher.class, Entity.class, double.class, double.class, double.class, float.class, float.class, PoseStack.class);
	}

	public class EntityTask extends RenderDispatcher<Gui>.Task {
		public final EntityRenderDispatcher entityRenderDispatcher;
		public final Entity entity;
		public final double x;
		public final double y;
		public final double z;
		public final float rotationYaw;
		public final float partialTicks;

		private EntityTask(
				EntityRenderDispatcher this_,
				Entity entity,
				double x,
				double y,
				double z,
				float rotationYaw,
				float partialTicks,
				PoseStack poseStack) {
			super(poseStack);
			this.entityRenderDispatcher = this_;
			this.entity = entity;
			this.x = x;
			this.y = y;
			this.z = z;
			this.rotationYaw = rotationYaw;
			this.partialTicks = partialTicks;
		}

		@Override
		public String toString() {
			return "{entity=" + entity + ", rotationYaw=" + rotationYaw + ", partialTicks=" + partialTicks + "}";
		}

		@Override
		public void render(Gui gui, PoseStack poseStack) {
			poseStack.translate(0, -gui.getOffsetY(), 0);// 移动原点
			poseStack.mulPose(entityRenderDispatcher.cameraOrientation());// 实体GUI坐标系中原点在实体头上正中心，向左向上为正
			boolean discard = false;
			if (gui instanceof TickableEntityAttachedGui entityAttachedGui)
				discard = entityAttachedGui.tick(this);
			// 将实体GUI坐标系转换为寻常GUI坐标系，寻常GUI坐标系原点在左上角，向右向下为正
			gui.render(poseStack, (float) x, (float) y);
			if (discard)
				super.remove(gui);
		}
	}

	public void addTaskRenderable(Entity key, Gui... rs) {
		super.addTaskTypeRenderables(key.getUUID(), rs);
	}

	private final ArrayList<Class<? extends Entity>> entityClasses = new ArrayList<>();

	public void addTaskRenderable(Class<? extends Entity> key, Gui... rs) {
		entityClasses.add(key);
		super.addTaskTypeRenderables(key, rs);
	}

	@Override
	public void registerDispatcher() {
		EntityRenderDispatcherInternal.Render.Callbacks.addBeforePosePopPoseCallback((
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
				CallbackInfo ci) -> {
			for (Class<? extends Entity> ec : entityClasses)
				if (ec.isInstance(entity))
					super.createTask(ec, this_, entity, x, y, z, rotationYaw, partialTicks, poseStack);
			super.createTask(entity.getUUID(), this_, entity, x, y, z, rotationYaw, partialTicks, poseStack);
		});
		LevelRendererInternal.RenderLevel.Callbacks.addBeforeRenderSystemDisableBlendCallback((LevelRenderer this_, DeltaTracker deltaTracker, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f frustumMatrix, Matrix4f projectionMatrix, CallbackInfo ci) -> {
			super.executeTasks("entity_attached_gui");
		});
	}

}
