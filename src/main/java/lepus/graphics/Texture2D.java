package lepus.graphics;

import org.lwjgl.opengl.GL30;

public class Texture2D {
	public static int currentActiveTexture() {
		return GL30.glGetInteger(GL30.GL_ACTIVE_TEXTURE);
	}

	public static int currentBindTexture() {
		return GL30.glGetInteger(GL30.GL_TEXTURE_BINDING_2D);
	}

	/**
	 * 查询指定的纹理宽度，不改变当前绑定纹理
	 * 
	 * @param texture
	 * @param mipmapLevel
	 * @return
	 */
	public static int textureWidth(int texture, int mipmapLevel) {
		int prev_texture = currentBindTexture();
		GL30.glBindTexture(GL30.GL_TEXTURE_2D, texture);
		int width = GL30.glGetTexLevelParameteri(GL30.GL_TEXTURE_2D, mipmapLevel, GL30.GL_TEXTURE_WIDTH);
		GL30.glBindTexture(GL30.GL_TEXTURE_2D, prev_texture);
		return width;
	}

	public static int textureHeight(int texture, int mipmapLevel) {
		int prev_texture = currentBindTexture();
		GL30.glBindTexture(GL30.GL_TEXTURE_2D, texture);
		int height = GL30.glGetTexLevelParameteri(GL30.GL_TEXTURE_2D, mipmapLevel, GL30.GL_TEXTURE_HEIGHT);
		GL30.glBindTexture(GL30.GL_TEXTURE_2D, prev_texture);
		return height;
	}

}
