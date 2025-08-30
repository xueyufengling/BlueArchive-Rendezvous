package lepus.mc.ext.client.render.iris;

import java.util.HashMap;

import lepus.graphics.shader.ScreenShader;

public class IrisPostprocess {
	public static final HashMap<String, ScreenShader> phasePostProcess = new HashMap<>();

	public static void setPhasePostprocessShader(String phase, ScreenShader shader) {
		phasePostProcess.put(phase, shader);
	}

	public static ScreenShader finalPostProcess;

	public static void setFinalPostProcess(ScreenShader shader) {
		finalPostProcess = shader;
	}
}
