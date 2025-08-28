package fw.client.render.gl;

import org.lwjgl.opengl.GL30;

import fw.core.Core;

/**
 * 着色器
 */
public class Shader {
	protected int program_id;

	protected Shader(String vertex_shader_source, String fragment_shader_source) {
		this.program_id = link(compileVertexShader(vertex_shader_source), compileFragmentShader(fragment_shader_source));
	}

	private int prev_program;

	public void beginUse() {
		prev_program = currentProgram();
		GL30.glUseProgram(program_id);
	}

	public void endUse() {
		GL30.glUseProgram(prev_program);
	}

	public void use() {
		GL30.glUseProgram(program_id);
	}

	public void setUniform(String name, int value) {
		this.beginUse();
		GL30.glUniform1i(GL30.glGetUniformLocation(program_id, name), value);
		this.endUse();
	}

	public static int uniformLocation(int program_id, String name) {
		return GL30.glGetUniformLocation(program_id, name);
	}

	public void invalidate() {
		GL30.glDeleteProgram(program_id);
		program_id = 0;
	}

	/**
	 * 链接着色器程序
	 * 
	 * @param vertex_shader
	 * @param fragment_shader
	 * @param delShaderIfSuccess 链接成功后是否删除着色器
	 * @return 链接失败返回0
	 */
	public static int link(int vertex_shader, int fragment_shader, boolean delShaderIfSuccess) {
		int shader_program = GL30.glCreateProgram();
		GL30.glAttachShader(shader_program, vertex_shader);
		GL30.glAttachShader(shader_program, fragment_shader);
		GL30.glLinkProgram(shader_program);
		int success = GL30.glGetProgrami(shader_program, GL30.GL_LINK_STATUS);
		if (success != 0) {// 链接成功则删除着色器
			if (delShaderIfSuccess) {
				GL30.glDeleteShader(vertex_shader);
				GL30.glDeleteShader(fragment_shader);
			}
			return shader_program;
		} else {// 链接失败
			Core.logError("Link shader program error:\n" + GL30.glGetProgramInfoLog(shader_program));
			GL30.glDeleteProgram(shader_program);
			return 0;
		}
	}

	public static int link(int vertex_shader, int fragment_shader) {
		return link(vertex_shader, fragment_shader, true);
	}

	/**
	 * 编译着色器
	 * 
	 * @param source
	 * @param shaderType
	 * @return 编译失败返回0
	 */
	public static int compile(String source, int shaderType) {
		int vertex_shader = GL30.glCreateShader(shaderType);
		GL30.glShaderSource(vertex_shader, source);
		GL30.glCompileShader(vertex_shader);
		int success = GL30.glGetShaderi(vertex_shader, GL30.GL_COMPILE_STATUS);
		if (success != 0) {// 编译成功
			return vertex_shader;
		} else {// 编译失败
			Core.logError("Compile shader error:\n" + GL30.glGetShaderInfoLog(vertex_shader));
			GL30.glDeleteShader(vertex_shader);
			return 0;
		}
	}

	public static int compileVertexShader(String source) {
		return compile(source, GL30.GL_VERTEX_SHADER);
	}

	public static int compileFragmentShader(String source) {
		return compile(source, GL30.GL_FRAGMENT_SHADER);
	}

	/**
	 * 当前使用的着色器程序
	 * 
	 * @return
	 */
	public static int currentProgram() {
		return GL30.glGetInteger(GL30.GL_CURRENT_PROGRAM);
	}

	public static final String pt_pass_vertex_shader = "#version 330 core\n" +
			"layout (location=0) in vec3 position;\n" +
			"layout (location=1) in vec2 texcoord;\n" +
			"out vec2 TexCoord;\n" +
			"void main()\n" +
			"{\n" +
			"	gl_Position = vec4(position, 1.0);\n" +
			"	TexCoord = texcoord;\n" +
			"}";

	public static final String pt_texture_fragment_shader = "#version 330 core\n" +
			"in vec2 TexCoord;\n" +
			"uniform sampler2D Texture0;\n" +
			"out vec4 FragColor;\n" +
			"void main()\n" +
			"{\n" +
			"	FragColor = texture(Texture0, TexCoord);\n" +
			"}";

	public static final Shader PASS_PT_SHADER = Shader.build(pt_pass_vertex_shader, pt_texture_fragment_shader);

	public static Shader build(String vertex_shader_source, String fragment_shader_source) {
		return new Shader(vertex_shader_source, fragment_shader_source);
	}

	/**
	 * RGB颜色空间转HSL颜色空间
	 */
	public static final String rgb2hsl = "vec3 rgb2hsl(vec3 rgb) {\n"
			+ "    float r = rgb.r;\n"
			+ "    float g = rgb.g;\n"
			+ "    float b = rgb.b;\n"
			+ "    float max = max(max(r, g), b);\n"
			+ "    float min = min(min(r, g), b);\n"
			+ "    float h, s, l = (max + min) / 2.0;\n"
			+ "    if (max == min) {\n"
			+ "        h = s = 0.0;\n"
			+ "    } else {\n"
			+ "        float d = max - min;\n"
			+ "        s = l > 0.5 ? d / (2.0 - max - min) : d / (max + min);\n"
			+ "        if (max == r) {\n"
			+ "            h = 60 * (g - b) / d + (g < b ? 360.0 : 0.0);\n"
			+ "        } else if (max == g) {\n"
			+ "            h = 60 * (b - r) / d + 120.0;\n"
			+ "        } else {\n"
			+ "            h = 60 * (r - g) / d + 240.0;\n"
			+ "        }\n"
			+ "        h /= 6.0;\n"
			+ "    }\n"
			+ "    return vec3(h, s, l);\n"
			+ "}\n";

	/**
	 * 色相转RGB工具函数
	 */
	public static final String hue2rgb = "float hue2rgb(float p, float q, float t) {\n"
			+ "    if (t < 0.0) t += 1.0;\n"
			+ "    if (t > 1.0) t -= 1.0;\n"
			+ "    if (t < 1.0/6.0) return p + (q - p) * 6.0 * t;\n"
			+ "    if (t < 1.0/2.0) return q;\n"
			+ "    if (t < 2.0/3.0) return p + (q - p) * (2.0/3.0 - t) * 6.0;\n"
			+ "    return p;\n"
			+ "}\n";

	/**
	 * HSL颜色空间转RGB颜色空间
	 * H取值0-360，S、L取值0-1
	 */
	public static final String hsl2rgb = hue2rgb + "vec3 hsl2rgb(vec3 hsl) {\n"
			+ "    float h = hsl.x / 360.0;\n"
			+ "    float s = hsl.y;\n"
			+ "    float l = hsl.z;\n"
			+ "    if (s == 0.0) {\n"
			+ "        return vec3(l, l, l);\n"
			+ "    }\n"
			+ "    float q = l < 0.5 ? l * (1.0 + s) : l + s - l * s;\n"
			+ "    float p = 2.0 * l - q;\n"
			+ "    return vec3(hue2rgb(p, q, h + 1.0/3.0), hue2rgb(p, q, h), hue2rgb(p, q, h - 1.0/3.0));\n"
			+ "}\n";

	public static final String hsl_model = rgb2hsl + hsl2rgb;
}
