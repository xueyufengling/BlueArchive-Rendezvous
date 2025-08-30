package lepus.mc.client.render.gui;

import com.mojang.blaze3d.vertex.PoseStack;

import lepus.mc.client.render.renderable.CompositeRenderable2D;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class Gui extends CompositeRenderable2D {
	@Override
	public Gui clone() {
		return (Gui) super.clone();
	}

	private boolean visible = true;
	private boolean showCondition = true;

	public float offset_x, offset_y;// 相对于GUI渲染起始点的位移

	public Gui setOffset(float x1_offset, float y1_offset) {
		this.offset_x = x1_offset;
		this.offset_y = y1_offset;
		return this;
	}

	public float getOffsetX() {
		return offset_x;
	}

	public float getOffsetY() {
		return offset_y;
	}

	/**
	 * GUI渲染
	 * 
	 * @param poseStack
	 * @param x1
	 * @param x2
	 * @param y1
	 * @param y2
	 */
	@Override
	public final void doRender(PoseStack poseStack, float x1, float y1) {
		if (visible && showCondition) {
			super.doRender(poseStack, x1 + offset_x, y1 + offset_y);
		}
	}

	public Gui setVisible(boolean v) {
		this.visible = v;
		return this;
	}

	public boolean visible() {
		return visible;
	}

	@Override
	public Object tick(Object... args) {
		this.showCondition = showCondition(args);
		return null;
	}

	public boolean showCondition(Object... args) {
		return true;
	}
}
