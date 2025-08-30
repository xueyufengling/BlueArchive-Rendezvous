package lepus.mc.event;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.LevelAccessor;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.LevelEvent;

@EventBusSubscriber
public enum LevelTrigger implements EventTrigger<LevelTrigger.LevelTickOperation> {
	SERVER_LEVEL_LOAD(EventPriority.HIGH), // 服务器世界加载
	SERVER_LEVEL_UNLOAD(EventPriority.LOW), // 服务器世界卸载
	CLIENT_LEVEL_LOAD(EventPriority.HIGH), // 客户端世界加载
	CLIENT_LEVEL_UNLOAD(EventPriority.LOW); // 客户端世界卸载

	@FunctionalInterface
	public static interface LevelTickOperation {
		public void operate(LevelAccessor level);
	}

	LevelTrigger(EventPriority defaultPriority) {
		define(defaultPriority);
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	private static void levelLoadHighest(LevelEvent.Load event) {
		LevelAccessor level = event.getLevel();
		if (level instanceof ClientLevel)
			LevelTrigger.CLIENT_LEVEL_LOAD.definition().priority(EventPriority.HIGHEST).execute(level);
		else
			LevelTrigger.SERVER_LEVEL_LOAD.definition().priority(EventPriority.HIGHEST).execute(level);
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	private static void levelLoadHigh(LevelEvent.Load event) {
		LevelAccessor level = event.getLevel();
		if (level instanceof ClientLevel)
			LevelTrigger.CLIENT_LEVEL_LOAD.definition().priority(EventPriority.HIGH).execute(level);
		else
			LevelTrigger.SERVER_LEVEL_LOAD.definition().priority(EventPriority.HIGH).execute(level);
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	private static void levelLoadLow(LevelEvent.Load event) {
		LevelAccessor level = event.getLevel();
		if (level instanceof ClientLevel)
			LevelTrigger.CLIENT_LEVEL_LOAD.definition().priority(EventPriority.LOW).execute(level);
		else
			LevelTrigger.SERVER_LEVEL_LOAD.definition().priority(EventPriority.LOW).execute(level);
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	private static void levelLoadLowest(LevelEvent.Load event) {
		LevelAccessor level = event.getLevel();
		if (level instanceof ClientLevel)
			LevelTrigger.CLIENT_LEVEL_LOAD.definition().priority(EventPriority.LOWEST).execute(level);
		else
			LevelTrigger.SERVER_LEVEL_LOAD.definition().priority(EventPriority.LOWEST).execute(level);
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	private static void levelUnloadHighest(LevelEvent.Unload event) {
		LevelAccessor level = event.getLevel();
		if (level instanceof ClientLevel)
			LevelTrigger.CLIENT_LEVEL_UNLOAD.definition().priority(EventPriority.HIGHEST).execute(level);
		else
			LevelTrigger.SERVER_LEVEL_UNLOAD.definition().priority(EventPriority.HIGHEST).execute(level);
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	private static void levelUnloadHigh(LevelEvent.Unload event) {
		LevelAccessor level = event.getLevel();
		if (level instanceof ClientLevel)
			LevelTrigger.CLIENT_LEVEL_UNLOAD.definition().priority(EventPriority.HIGH).execute(level);
		else
			LevelTrigger.SERVER_LEVEL_UNLOAD.definition().priority(EventPriority.HIGH).execute(level);
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	private static void levelUnloadLow(LevelEvent.Unload event) {
		LevelAccessor level = event.getLevel();
		if (level instanceof ClientLevel)
			LevelTrigger.CLIENT_LEVEL_UNLOAD.definition().priority(EventPriority.LOW).execute(level);
		else
			LevelTrigger.SERVER_LEVEL_UNLOAD.definition().priority(EventPriority.LOW).execute(level);
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	private static void levelUnloadLowest(LevelEvent.Unload event) {
		LevelAccessor level = event.getLevel();
		if (level instanceof ClientLevel)
			LevelTrigger.CLIENT_LEVEL_UNLOAD.definition().priority(EventPriority.LOWEST).execute(level);
		else
			LevelTrigger.SERVER_LEVEL_UNLOAD.definition().priority(EventPriority.LOWEST).execute(level);
	}

	@Override
	public void executeCallback(LevelTickOperation op, Object... args) {
		op.operate((LevelAccessor) args[0]);
	}
}