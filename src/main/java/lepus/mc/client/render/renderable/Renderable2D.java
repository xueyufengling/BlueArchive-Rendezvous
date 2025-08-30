package lepus.mc.client.render.renderable;

import com.mojang.blaze3d.vertex.PoseStack;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface Renderable2D extends Renderable {
	public void render(PoseStack poseStack, float x1, float y1);

	@OnlyIn(Dist.CLIENT)
	public abstract class Instance implements Renderable2D {
		public float offset_x, offset_y;// 相对于渲染左上角点的位移
		public float width_scale, height_scale;

		Instance(float offset_x, float offset_y, float width_scale, float height_scale) {
			this.offset_x = offset_x;
			this.offset_y = offset_y;
			this.width_scale = width_scale;
			this.height_scale = height_scale;
		}

		Instance(float offset_x, float offset_y) {
			this(offset_x, offset_y, 1.0f, 1.0f);
		}

		Instance() {
			this(0.0f, 0.0f);
		}

		public Instance setOffset(float offset_x, float offset_y) {
			this.offset_x = offset_x;
			this.offset_y = offset_y;
			return this;
		}

		public Instance setScale(float f) {
			width_scale = f;
			height_scale = f;
			return this;
		}

		public Instance setScale(float wf, float hf) {
			width_scale = wf;
			height_scale = hf;
			return this;
		}

		public Instance scale(float f) {
			width_scale *= f;
			height_scale *= f;
			return this;
		}

		public Instance scale(float wf, float hf) {
			width_scale *= wf;
			height_scale *= hf;
			return this;
		}

		public abstract Instance setRenderingSize(float x, float y);
	}
}
