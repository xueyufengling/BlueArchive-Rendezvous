package lepus.mc.common;

import lepus.mc.event.ServerTickTrigger;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.EventPriority;

public class GameDate {
	private static long worldTime;
	private static long dayTime;

	static {
		ServerTickTrigger.PRE_SERVER_TICK.addCallback(EventPriority.HIGHEST, (MinecraftServer server, ServerLevel level) -> {
			GameDate.worldTime = level.getGameTime();
			GameDate.dayTime = level.getDayTime();
		});
	}

	/**
	 * 获取存档时间
	 * 
	 * @return
	 */
	public static long worldTime() {
		return GameDate.worldTime;
	}

	/**
	 * 获取一天内的时间
	 * 
	 * @return
	 */
	public static long dayTime() {
		return GameDate.dayTime;
	}
}
