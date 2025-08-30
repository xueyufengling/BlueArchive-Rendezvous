package lepus.mc.dimension;

import lepus.mc.core.ServerInstance;
import lepus.mc.core.ServerLevels;

public abstract class LevelSwitch {
	private static String[] current_levels = new String[] {};

	private LevelSwitch() {

	}

	public static void switchTo(String... level_names) {
		if (ServerInstance.available()) {
			ServerLevels.disableLevels(current_levels);
			ServerLevels.enableLevels(level_names);
		}
		current_levels = level_names;
	}
}