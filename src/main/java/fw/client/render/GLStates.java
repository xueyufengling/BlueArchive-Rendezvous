package fw.client.render;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.platform.GlStateManager;

import lyra.object.ObjectManipulator;

public class GLStates {
	public static final boolean ON_LINUX;
	public static final int TEXTURE_COUNT;
	public static final Object BLEND;
	public static final Object DEPTH;
	public static final Object CULL;
	public static final Object POLY_OFFSET;
	public static final Object COLOR_LOGIC;
	public static final Object STENCIL;
	public static final Object SCISSOR;
	public static final Object[] TEXTURES;
	public static final Object COLOR_MASK;

	static {
		ON_LINUX = (boolean) ObjectManipulator.access(GlStateManager.class, "ON_LINUX");
		TEXTURE_COUNT = (int) ObjectManipulator.access(GlStateManager.class, "TEXTURE_COUNT");
		BLEND = ObjectManipulator.access(GlStateManager.class, "BLEND");
		DEPTH = ObjectManipulator.access(GlStateManager.class, "DEPTH");
		CULL = ObjectManipulator.access(GlStateManager.class, "CULL");
		POLY_OFFSET = ObjectManipulator.access(GlStateManager.class, "POLY_OFFSET");
		COLOR_LOGIC = ObjectManipulator.access(GlStateManager.class, "COLOR_LOGIC");
		STENCIL = ObjectManipulator.access(GlStateManager.class, "STENCIL");
		SCISSOR = ObjectManipulator.access(GlStateManager.class, "SCISSOR");
		TEXTURES = (Object[]) ObjectManipulator.access(GlStateManager.class, "TEXTURES");
		COLOR_MASK = ObjectManipulator.access(GlStateManager.class, "COLOR_MASK");
	}

	public static boolean depthEnabled() {
		return GL11.glIsEnabled(GL11.GL_DEPTH_TEST);
	}

	public static void setDepthEnabled(boolean enabled) {
		if (enabled)
			GlStateManager._enableDepthTest();
		else
			GlStateManager._disableDepthTest();
	}

	public static boolean blendEnabled() {
		return GL11.glIsEnabled(GL11.GL_BLEND);
	}

	public static void setBlendEnabled(boolean enabled) {
		if (enabled)
			GlStateManager._enableBlend();
		else
			GlStateManager._disableBlend();
	}
}
