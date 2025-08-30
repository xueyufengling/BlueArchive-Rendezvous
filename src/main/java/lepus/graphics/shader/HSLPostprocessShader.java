package lepus.graphics.shader;

/**
 * HSL颜色模型后期处理
 */
public class HSLPostprocessShader extends ScreenShader {
	public static final String hsl_postprocess_shader_source = "#version 330 core\n" +
			"uniform sampler2D Texture0;\n" +
			"uniform float Hue;\n" +
			"uniform float SaturationDelta;\n" +
			"uniform float LightDelta;\n" +
			"in vec2 TexCoord;\n" +
			"out vec4 FragColor;\n\n" +
			Shader.hsl_model +
			"void main()\n" +
			"{\n" +
			"   vec4 color = texture(Texture0, TexCoord);\n" +
			"   vec3 hsl = rgb2hsl(vec3(color));\n" +
			"   hsl.x = Hue;\n" +
			"   hsl.y = clamp(hsl.y + SaturationDelta, 0.0, 1.0);\n" +
			"   hsl.z = clamp(hsl.z + LightDelta, 0.0, 1.0);\n" +
			"   FragColor = vec4(hsl2rgb(hsl), color.a);\n" +
			"}";

	private final int hueLoc;
	private final int saturationDeltaLoc;
	private final int lightDeltaLoc;

	public HSLPostprocessShader() {
		super(hsl_postprocess_shader_source, "Texture0");
		this.hueLoc = this.uniformLocation("Hue");
		this.saturationDeltaLoc = this.uniformLocation("SaturationDelta");
		this.lightDeltaLoc = this.uniformLocation("LightDelta");
	}

	/**
	 * 设置全部像素固定色相
	 * 
	 * @param hue
	 */
	public void setHue(float hue) {
		this.setUniform(hueLoc, hue);
	}

	/**
	 * 设置各个像素的饱和度变化量
	 * 
	 * @param saturationDelta
	 */
	public void setSaturationDelta(float saturationDelta) {
		this.setUniform(saturationDeltaLoc, saturationDelta);
	}

	/**
	 * 设置各个像素的亮度变化量
	 * 
	 * @param saturationDelta
	 */
	public void setLightDelta(float lightDelta) {
		this.setUniform(lightDeltaLoc, lightDelta);
	}

	public static HSLPostprocessShader fixed(float hue, float saturationDelta, float lightDelta) {
		HSLPostprocessShader shader = new HSLPostprocessShader();
		shader.setHue(hue);
		shader.setSaturationDelta(saturationDelta);
		shader.setLightDelta(lightDelta);
		return shader;
	}
}
