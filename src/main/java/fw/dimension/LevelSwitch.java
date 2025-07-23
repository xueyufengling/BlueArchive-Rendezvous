package fw.dimension;

import fw.core.ServerInstance;

public abstract class LevelSwitch {
	private static String[] current_levels = new String[] {};

	private LevelSwitch() {

	}

	public static void switchTo(String... level_names) {
		if (ServerInstance.available()) {
			ServerInstance.disableLevels(current_levels);
			ServerInstance.enableLevels(level_names);
		}
		current_levels = level_names;
	}
}