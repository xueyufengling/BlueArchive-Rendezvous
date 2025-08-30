package lepus.mc.client.render.renderable;

import com.mojang.blaze3d.vertex.PoseStack;

import lepus.mc.core.Tickable;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * 一个可渲染的复合2D对象，由多个纹理图片组成，所有纹理按照列表顺序依次渲染，且各个层级之间没有偏移，绘制起点相同
 */
@OnlyIn(Dist.CLIENT)
public class CompositeRenderable2D implements Cloneable, Renderable2D, Tickable {
	// 浅拷贝
	@Override
	public CompositeRenderable2D clone() {
		try {
			return (CompositeRenderable2D) super.clone();
		} catch (CloneNotSupportedException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public Renderable2D[] layers;// 该图像的图层，图层具有深度

	public CompositeRenderable2D() {
		this.layers = null;
	}

	public CompositeRenderable2D(Texture... textures) {
		this.layers = (Renderable2D[]) Texture.Area.of(textures);
	}

	public CompositeRenderable2D(ResourceLocation... textureLocs) {
		this(Texture.of(textureLocs));
	}

	public CompositeRenderable2D(String... textureLocs) {
		this(Texture.of(textureLocs));
	}

	/**
	 * 渲染前的操作
	 * 
	 * @param graphicsContext
	 * @param x1
	 * @param y1
	 */
	public void preRender(PoseStack poseStack, float x1, float y1) {

	}

	/**
	 * 渲染后的操作
	 * 
	 * @param graphicsContext
	 * @param x1
	 * @param x2
	 * @param y1
	 * @param y2
	 */
	public void postRender(PoseStack poseStack, float x1, float y1) {

	}

	/**
	 * 渲染的实际执行逻辑
	 * 
	 * @param poseStack
	 * @param x1
	 * @param y1
	 */
	public void doRender(PoseStack poseStack, float x1, float y1) {
		if (layers != null)
			for (int i = 0; i < layers.length; ++i)
				layers[i].render(poseStack, x1, y1);
	}

	public final void render(PoseStack poseStack, float x1, float y1) {
		this.preRender(poseStack, x1, y1);
		this.doRender(poseStack, x1, y1);
		this.postRender(poseStack, x1, y1);
	}

	public Renderable2D getLayer(int idx) {
		return layers[idx];
	}

	@Override
	public String toString() {
		String descrip = "{layers=[";
		for (int i = 0; i < layers.length; ++i) {
			descrip += layers[i].toString();
			if (i != layers.length - 1)
				descrip += ", ";
		}
		descrip += "]}";
		return descrip;
	}
}