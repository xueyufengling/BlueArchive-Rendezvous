package fw.client.render.sky;

import fw.client.render.VertexBufferManipulator;

public class WeatherEffect {

	public static VertexBufferManipulator.ColorResolver rainColorResolver;

	public static VertexBufferManipulator.ColorResolver snowColorResolver;

	public static void setRainColorResolver(VertexBufferManipulator.ColorResolver resolver) {
		rainColorResolver = resolver;
	}

	/**
	 * 设置下雨颜色为固定颜色
	 * 
	 * @param r
	 * @param g
	 * @param b
	 * @param a
	 */
	public static void setRainColor(int r, int g, int b, int a) {
		setRainColorResolver(VertexBufferManipulator.ColorResolver.fixed(r, g, b, a));
	}

	public static void setRainColor(int r, int g, int b) {
		setRainColorResolver(VertexBufferManipulator.ColorResolver.fixedRGB(r, g, b));
	}

	public static void setRainAlpha(int a) {
		setRainColorResolver(VertexBufferManipulator.ColorResolver.fixedA(a));
	}

	public static void setSnowColorResolver(VertexBufferManipulator.ColorResolver resolver) {
		snowColorResolver = resolver;
	}

	public static void setSnowColor(int r, int g, int b, int a) {
		setSnowColorResolver(VertexBufferManipulator.ColorResolver.fixed(r, g, b, a));
	}

	public static void setSnowColor(int r, int g, int b) {
		setSnowColorResolver(VertexBufferManipulator.ColorResolver.fixedRGB(r, g, b));
	}

	public static void setSnowAlpha(int a) {
		setSnowColorResolver(VertexBufferManipulator.ColorResolver.fixedA(a));
	}
}
