package fw.client.render.gl;

import org.lwjgl.opengl.GL30;

/**
 * 顶点着色器固定为NDC坐标系传递顶点着色器
 */
public class ScreenShader extends Shader {
	private ScreenShader(String fragment_shader_source, String samplerUniform) {
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
			GL30.glDeleteVertexArrays(screen_quad_vao);
			screen_quad_vao = GL30.glGenVertexArrays();
			GL30.glDeleteBuffers(screen_quad_vbo);
			screen_quad_vbo = GL30.glGenBuffers();
			GL30.glBindVertexArray(screen_quad_vao);// 绑定VAO
			GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, screen_quad_vbo);// 为VAO绑定VBO
			/**
			 * 顶点顺序需要保证三角形顶点逆时针排列，避免反面向前被Backface-cull剔除
			 * TRIANGLE_STRP的正面为第一个三角形的正面
			 */
			GL30.glBufferData(GL30.GL_ARRAY_BUFFER, new float[] {
					-1.0f, -1.0f, 0.0f, 0.0f, 0.0f,
					1.0f, -1.0f, 0.0f, 1.0f, 0.0f,
					-1.0f, 1.0f, 0.0f, 0.0f, 1.0f,
					1.0f, 1.0f, 0.0f, 1.0f, 1.0f
			}, GL30.GL_STATIC_DRAW);
			GL30.glVertexAttribPointer(0, 3, GL30.GL_FLOAT, false, 5 * GL_FLOAT_SIZE, 0);
			GL30.glEnableVertexAttribArray(0);
			GL30.glVertexAttribPointer(1, 2, GL30.GL_FLOAT, false, 5 * GL_FLOAT_SIZE, 3 * GL_FLOAT_SIZE);
			GL30.glEnableVertexAttribArray(1);
			GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
			GL30.glBindVertexArray(0);
			inited_screen = true;
		}
	}

	/**
	 * 使用着色器
	 */
	@Override
	public void beginUse() {
		init();
		GL30.glBindVertexArray(screen_quad_vao);
		super.beginUse();
	}

	@Override
	public void endUse() {
		super.endUse();
		GL30.glBindVertexArray(0);
	}

	/**
	 * 使用本着色器渲染texture纹理QUAD到屏幕帧缓冲
	 * 
	 * @param texture
	 */
	public void renderScreen(int texture) {
		beginUse();
		GL30.glDisable(GL30.GL_CULL_FACE);// 渲染到屏幕时关闭面剔除
		GL30.glActiveTexture(GL30.GL_TEXTURE0);
		GL30.glBindTexture(GL30.GL_TEXTURE_2D, texture);
		GL30.glDrawArrays(GL30.GL_TRIANGLE_STRIP, 0, 4);
		GL30.glEnable(GL30.GL_CULL_FACE);
		endUse();
	}

	@Override
	public void use() {
		init();
		GL30.glBindVertexArray(screen_quad_vao);
		super.use();
	}
}
