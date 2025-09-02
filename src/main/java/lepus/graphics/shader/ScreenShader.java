package lepus.graphics.shader;

import org.lwjgl.opengl.GL33;

/**
 * 顶点着色器固定为NDC坐标系传递顶点着色器
 */
public class ScreenShader extends Shader {
	protected ScreenShader(String fragment_shader_source, String samplerUniform) {
		super(Shader.pt_pass_vertex_shader, fragment_shader_source);
		this.setUniform(samplerUniform, 0);// 初始化着色器GL_TEXTURE0的uniform采样器
	}

	public static ScreenShader createShaderProgram(String fragment_shader_source, String samplerUniform) {
		return new ScreenShader(fragment_shader_source, samplerUniform);
	}

	public static final ScreenShader SCREEN_BLIT_SHADER = createShaderProgram(Shader.pt_texture_fragment_shader, "Texture0");

	private static int screen_quad_vao = 0;

	/**
	 * 屏幕QUAD，用于将读出的帧缓冲颜色写回去。<br>
	 * 不可直接写入目标帧缓冲的颜色附件，否则GPU渲染和CPU同时写入会造成数据竞争，渲染结果将错误。
	 */
	private static int screen_quad_vbo = 0;

	private static boolean inited_screen = false;

	public static final int GL_FLOAT_SIZE = 4;

	private static void init() {
		if (!inited_screen) {
			GL33.glDeleteVertexArrays(screen_quad_vao);
			screen_quad_vao = GL33.glGenVertexArrays();
			GL33.glDeleteBuffers(screen_quad_vbo);
			screen_quad_vbo = GL33.glGenBuffers();
			GL33.glBindVertexArray(screen_quad_vao);// 绑定VAO
			GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, screen_quad_vbo);// 为VAO绑定VBO
			/**
			 * 顶点顺序需要保证三角形顶点逆时针排列，避免反面向前被Backface-cull剔除
			 * TRIANGLE_STRP的正面为第一个三角形的正面
			 */
			GL33.glBufferData(GL33.GL_ARRAY_BUFFER, new float[] {
					-1.0f, -1.0f, 0.0f, 0.0f, 0.0f,
					1.0f, -1.0f, 0.0f, 1.0f, 0.0f,
					-1.0f, 1.0f, 0.0f, 0.0f, 1.0f,
					1.0f, 1.0f, 0.0f, 1.0f, 1.0f
			}, GL33.GL_STATIC_DRAW);
			GL33.glVertexAttribPointer(0, 3, GL33.GL_FLOAT, false, 5 * GL_FLOAT_SIZE, 0);
			GL33.glEnableVertexAttribArray(0);
			GL33.glVertexAttribPointer(1, 2, GL33.GL_FLOAT, false, 5 * GL_FLOAT_SIZE, 3 * GL_FLOAT_SIZE);
			GL33.glEnableVertexAttribArray(1);
			GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, 0);
			GL33.glBindVertexArray(0);
			inited_screen = true;
		}
	}

	/**
	 * 使用着色器
	 */
	@Override
	public void beginUse() {
		init();
		GL33.glBindVertexArray(screen_quad_vao);
		super.beginUse();
	}

	@Override
	public void endUse() {
		super.endUse();
		GL33.glBindVertexArray(0);
	}

	/**
	 * 使用本着色器渲染texture纹理QUAD到屏幕帧缓冲<br>
	 * 不关心深度测试、混合
	 * 
	 * @param texture
	 */
	public void renderScreen(int texture) {
		beginUse();
		GL33.glDisable(GL33.GL_CULL_FACE);// 渲染到屏幕时关闭面剔除
		GL33.glActiveTexture(GL33.GL_TEXTURE0);
		GL33.glBindTexture(GL33.GL_TEXTURE_2D, texture);
		GL33.glDrawArrays(GL33.GL_TRIANGLE_STRIP, 0, 4);
		GL33.glEnable(GL33.GL_CULL_FACE);
		endUse();
	}

	@Override
	public void use() {
		init();
		GL33.glBindVertexArray(screen_quad_vao);
		super.use();
	}
}
