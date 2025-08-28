package fw.ext.client.render.iris;

import java.util.HashMap;

import fw.client.render.gl.ScreenShader;

public class IrisPostprocess {
	public static final HashMap<String, ScreenShader> postProcess = new HashMap<>();

	public static void setPostprocessShader(String phase, ScreenShader shader) {
		postProcess.put(phase, shader);
	}
}
