package lepus.mc.client.render.gui;

import org.joml.Matrix4f;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat;

import lepus.mc.client.render.TesselatorInstance;
import lepus.mc.resources.ResourceLocations;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiGraphicsContext {

	public static int width() {
		return Minecraft.getInstance().getWindow().getGuiScaledWidth();
	}

	public static int height() {
		return Minecraft.getInstance().getWindow().getGuiScaledHeight();
	}

	private static GuiGraphics graphicsContext;

	public static GuiGraphics guiGraphics() {
		if (graphicsContext == null)
			graphicsContext = GuiGraphicsContext.createGuiGraphics();
		return graphicsContext;
	}

	/**
	 * 创建GuiGraphics，当无法获取GuiGraphics时可以自行创建<br>
	 * 在底层所有实例实际上都对应同一个GuiGraphics逻辑对象
	 * 
	 * @return
	 */
	public static GuiGraphics createGuiGraphics() {
		Minecraft client = Minecraft.getInstance();
		return new GuiGraphics(client, client.renderBuffers().bufferSource());
	}

	@SuppressWarnings("resource")
	public static Font font() {
		return Minecraft.getInstance().font;
	}

	public static final int WHITE = 0xFFFFFFFF;

	public static void drawString(GuiGraphics graphics, String str, float x, float y, float scale, int packedColor) {
		PoseStack pose = graphics.pose();
		pose.pushPose();
		pose.translate(x, y, 0);
		pose.scale(scale, scale, scale);
		graphics.drawString(font(), str, 0, 0, packedColor);
		pose.popPose();
	}

	public static void drawString(GuiGraphics graphics, String str, float x, float y, float scale) {
		drawString(graphics, str, x, y, scale, WHITE);
	}

	public static void drawString(GuiGraphics graphics, String str, float x, float y) {
		drawString(graphics, str, x, y, 1.0f);
	}

	public static void drawCenteredString(GuiGraphics graphics, String str, float x, float y, float scale, int packedColor) {
		PoseStack pose = graphics.pose();
		Font font = font();
		pose.pushPose();
		pose.translate(x, y - (font.lineHeight / 2.0f) * scale, 0);// 将字符串中心移动到x, y处
		pose.scale(scale, scale, scale);
		graphics.drawCenteredString(font, str, 0, 0, packedColor);
		pose.popPose();
	}

	public static void drawCenteredString(GuiGraphics graphics, String str, float x, float y, float scale) {
		drawCenteredString(graphics, str, x, y, scale, 0xFFFFFFFF);
	}

	public static void drawCenteredString(GuiGraphics graphics, String str, float x, float y) {
		drawCenteredString(graphics, str, x, y, 1.0f);
	}

	public static final float DEFAULT_BLIT_OFFSET = 1;
	public static final float DEFAULT_BEGIN_U = 0.0f;
	public static final float DEFAULT_BEGIN_V = 0.0f;
	public static final float DEFAULT_END_U = 1.0f;
	public static final float DEFAULT_END_V = 1.0f;

	/**
	 * 绘制平面四边形图像，不操作PoseStack，需要手动操作栈
	 * 
	 * @param poseStack
	 * @param atlasLocation 要绘制的目标纹理
	 * @param x1            起始点x坐标
	 * @param x2            终止点x坐标
	 * @param y1            起始点y坐标
	 * @param y2            终止点y坐标
	 * @param blitOffset
	 * @param minU
	 * @param maxU
	 * @param minV
	 * @param maxV
	 */
	public static void blitImage(PoseStack poseStack, ResourceLocation atlasLocation, float x1, float x2, float y1, float y2, float blitOffset, float minU, float maxU, float minV, float maxV) {
		RenderSystem.setShaderTexture(0, (atlasLocation));
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.disableDepthTest();
		RenderSystem.enableBlend();
		Matrix4f matrix4f = poseStack.last().pose();
		BufferBuilder bufferBuilder = TesselatorInstance.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		TesselatorInstance.posUvVertex(bufferBuilder, matrix4f, x1, y1, blitOffset, minU, minV);
		TesselatorInstance.posUvVertex(bufferBuilder, matrix4f, x1, y2, blitOffset, minU, maxV);
		TesselatorInstance.posUvVertex(bufferBuilder, matrix4f, x2, y2, blitOffset, maxU, maxV);
		TesselatorInstance.posUvVertex(bufferBuilder, matrix4f, x2, y1, blitOffset, maxU, minV);
		TesselatorInstance.drawBufferBuilder(bufferBuilder);
	}

	public static void blitImage(MultiBufferSource bufferSource, RenderType type, PoseStack poseStack, ResourceLocation atlasLocation, float x1, float x2, float y1, float y2, float blitOffset, float minU, float maxU, float minV, float maxV) {
		RenderSystem.setShaderTexture(0, (atlasLocation));
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		Matrix4f matrix4f = poseStack.last().pose();
		BufferBuilder bufferBuilder = (BufferBuilder) bufferSource.getBuffer(type);
		TesselatorInstance.posUvColorVertex(bufferBuilder, matrix4f, x1, y1, blitOffset, minU, minV);
		TesselatorInstance.posUvColorVertex(bufferBuilder, matrix4f, x1, y2, blitOffset, minU, maxV);
		TesselatorInstance.posUvColorVertex(bufferBuilder, matrix4f, x2, y2, blitOffset, maxU, maxV);
		TesselatorInstance.posUvColorVertex(bufferBuilder, matrix4f, x2, y1, blitOffset, maxU, minV);
	}

	public static void blitImage(PoseStack poseStack, ResourceLocation atlasLocation, float x1, float x2, float y1, float y2, float minU, float maxU, float minV, float maxV) {
		blitImage(poseStack, atlasLocation, x1, x2, y1, y2, DEFAULT_BLIT_OFFSET, minU, maxU, minV, maxV);
	}

	public static void blitImage(PoseStack poseStack, ResourceLocation atlasLocation, int x1, int x2, int y1, int y2, int blitOffset) {
		blitImage(poseStack, atlasLocation, x1, x2, y1, y2, blitOffset, DEFAULT_BEGIN_U, DEFAULT_END_U, DEFAULT_BEGIN_V, DEFAULT_END_V);
	}

	public static void blitImage(PoseStack poseStack, ResourceLocation atlasLocation, int x1, int x2, int y1, int y2) {
		blitImage(poseStack, atlasLocation, x1, x2, y1, y2, DEFAULT_BLIT_OFFSET, DEFAULT_BEGIN_U, DEFAULT_END_U, DEFAULT_BEGIN_V, DEFAULT_END_V);
	}

	public static void blitImage(PoseStack poseStack, String atlasLocation, int x1, int x2, int y1, int y2, int blitOffset, float minU, float maxU, float minV, float maxV) {
		blitImage(poseStack, ResourceLocations.build(atlasLocation), x1, x2, y1, y2, blitOffset, minU, maxU, minV, maxV);
	}

	public static void blitImage(PoseStack poseStack, String atlasLocation, int x1, int x2, int y1, int y2, float minU, float maxU, float minV, float maxV) {
		blitImage(poseStack, ResourceLocations.build(atlasLocation), x1, x2, y1, y2, DEFAULT_BLIT_OFFSET, minU, maxU, minV, maxV);
	}

	public static void blitImage(PoseStack poseStack, String atlasLocation, int x1, int x2, int y1, int y2, int blitOffset) {
		blitImage(poseStack, ResourceLocations.build(atlasLocation), x1, x2, y1, y2, blitOffset, DEFAULT_BEGIN_U, DEFAULT_END_U, DEFAULT_BEGIN_V, DEFAULT_END_V);
	}

	public static void blitImage(PoseStack poseStack, String atlasLocation, int x1, int x2, int y1, int y2) {
		blitImage(poseStack, ResourceLocations.build(atlasLocation), x1, x2, y1, y2, DEFAULT_BLIT_OFFSET, DEFAULT_BEGIN_U, DEFAULT_END_U, DEFAULT_BEGIN_V, DEFAULT_END_V);
	}

	/**
	 * 绘制平面图像，自动推入弹出栈，不改变原有的PoseStack
	 * 
	 * @param poseStack
	 * @param atlasLocation
	 * @param x1
	 * @param x2
	 * @param y1
	 * @param y2
	 * @param blitOffset
	 * @param minU
	 * @param maxU
	 * @param minV
	 * @param maxV
	 */
	public static void drawImage(PoseStack poseStack, ResourceLocation atlasLocation, int x1, int x2, int y1, int y2, int blitOffset, float minU, float maxU, float minV, float maxV) {
		poseStack.pushPose();
		blitImage(poseStack, atlasLocation, x1, x2, y1, y2, blitOffset, minU, maxU, minV, maxV);
		poseStack.popPose();
	}

	public static void drawImage(PoseStack poseStack, ResourceLocation atlasLocation, int x1, int x2, int y1, int y2, float minU, float maxU, float minV, float maxV) {
		poseStack.pushPose();
		blitImage(poseStack, atlasLocation, x1, x2, y1, y2, minU, maxU, minV, maxV);
		poseStack.popPose();
	}

	public static void drawImage(PoseStack poseStack, ResourceLocation atlasLocation, int x1, int x2, int y1, int y2, int blitOffset) {
		poseStack.pushPose();
		blitImage(poseStack, atlasLocation, x1, x2, y1, y2, blitOffset);
		poseStack.popPose();
	}

	public static void drawImage(PoseStack poseStack, ResourceLocation atlasLocation, int x1, int x2, int y1, int y2) {
		poseStack.pushPose();
		blitImage(poseStack, atlasLocation, x1, x2, y1, y2);
		poseStack.popPose();
	}

	public static void drawImage(PoseStack poseStack, String atlasLocation, int x1, int x2, int y1, int y2, int blitOffset, float minU, float maxU, float minV, float maxV) {
		poseStack.pushPose();
		blitImage(poseStack, atlasLocation, x1, x2, y1, y2, blitOffset, minU, maxU, minV, maxV);
		poseStack.popPose();
	}

	public static void drawImage(PoseStack poseStack, String atlasLocation, int x1, int x2, int y1, int y2, int blitOffset) {
		poseStack.pushPose();
		blitImage(poseStack, atlasLocation, x1, x2, y1, y2, blitOffset);
		poseStack.popPose();
	}

	public static void drawImage(PoseStack poseStack, String atlasLocation, int x1, int x2, int y1, int y2, float minU, float maxU, float minV, float maxV) {
		poseStack.pushPose();
		blitImage(poseStack, atlasLocation, x1, x2, y1, y2, minU, maxU, minV, maxV);
		poseStack.popPose();
	}

	public static void drawImage(PoseStack poseStack, String atlasLocation, int x1, int x2, int y1, int y2) {
		poseStack.pushPose();
		blitImage(poseStack, atlasLocation, x1, x2, y1, y2);
		poseStack.popPose();
	}

	public static final int SLOT_SIZE = 16;

	/**
	 * 在GUI的物品槽中渲染图像，需要在ItemRenderer.render()中调用以获取正确的物品槽x、y坐标
	 * 
	 * @param poseStack
	 * @param atlasLocation
	 * @param x1
	 * @param x2
	 * @param y1
	 * @param y2
	 * @param blitOffset
	 * @param minU
	 * @param maxU
	 * @param minV
	 * @param maxV
	 */
	public static void drawSlot(PoseStack poseStack, ResourceLocation atlasLocation, int x, int y, int blitOffset, float minU, float maxU, float minV, float maxV) {
		drawImage(poseStack, atlasLocation, x, x + SLOT_SIZE, y, y + SLOT_SIZE, blitOffset, minU, maxU, minV, maxV);
	}

	public static void drawSlot(PoseStack poseStack, ResourceLocation atlasLocation, int x, int y, float minU, float maxU, float minV, float maxV) {
		drawImage(poseStack, atlasLocation, x, x + SLOT_SIZE, y, y + SLOT_SIZE, minU, maxU, minV, maxV);
	}

	public static void drawSlot(PoseStack poseStack, ResourceLocation atlasLocation, int x, int y, int blitOffset) {
		drawImage(poseStack, atlasLocation, x, x + SLOT_SIZE, y, y + SLOT_SIZE, blitOffset);
	}

	public static void drawSlot(PoseStack poseStack, ResourceLocation atlasLocation, int x, int y) {
		drawImage(poseStack, atlasLocation, x, x + SLOT_SIZE, y, y + SLOT_SIZE);
	}

	public static void drawSlot(PoseStack poseStack, String atlasLocation, int x, int y, int blitOffset, float minU, float maxU, float minV, float maxV) {
		drawImage(poseStack, atlasLocation, x, x + SLOT_SIZE, y, y + SLOT_SIZE, blitOffset, minU, maxU, minV, maxV);
	}

	public static void drawSlot(PoseStack poseStack, String atlasLocation, int x, int y, float minU, float maxU, float minV, float maxV) {
		drawImage(poseStack, atlasLocation, x, x + SLOT_SIZE, y, y + SLOT_SIZE, minU, maxU, minV, maxV);
	}

	public static void drawSlot(PoseStack poseStack, String atlasLocation, int x, int y, int blitOffset) {
		drawImage(poseStack, atlasLocation, x, x + SLOT_SIZE, y, y + SLOT_SIZE, blitOffset);
	}

	public static void drawSlot(PoseStack poseStack, String atlasLocation, int x, int y) {
		drawImage(poseStack, atlasLocation, x, x + SLOT_SIZE, y, y + SLOT_SIZE);
	}
}
