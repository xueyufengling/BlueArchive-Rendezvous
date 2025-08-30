package lepus.mc.event;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

@EventBusSubscriber
public enum LevelTickTrigger implements EventTrigger<LevelTickTrigger.LevelTickOperation> {
	PRE_SERVER_LEVEL_TICK(EventPriority.HIGH), // 服务器世界更新前
	POST_SERVER_LEVEL_TICK(EventPriority.LOW), // 服务器世界更新后
	PRE_CLIENT_LEVEL_TICK(EventPriority.HIGH), // 客户端世界更新前
	POST_CLIENT_LEVEL_TICK(EventPriority.LOW); // 客户端世界更新后

	@FunctionalInterface
	public static interface LevelTickOperation {
		public void operate(Level level);
	}

	LevelTickTrigger(EventPriority defaultPriority) {
		define(defaultPriority);
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	private static void preLevelTickHighest(LevelTickEvent.Pre event) {
		Level level = event.getLevel();
		if (level instanceof ClientLevel)
			LevelTickTrigger.PRE_CLIENT_LEVEL_TICK.definition().priority(EventPriority.HIGHEST).execute(event.getLevel());
		else
			LevelTickTrigger.PRE_SERVER_LEVEL_TICK.definition().priority(EventPriority.HIGHEST).execute(event.getLevel());
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	private static void preLevelTickHigh(LevelTickEvent.Pre event) {
		Level level = event.getLevel();
		if (level instanceof ClientLevel)
			LevelTickTrigger.PRE_CLIENT_LEVEL_TICK.definition().priority(EventPriority.HIGH).execute(event.getLevel());
		else
			LevelTickTrigger.PRE_SERVER_LEVEL_TICK.definition().priority(EventPriority.HIGH).execute(event.getLevel());
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	private static void preLevelTickLow(LevelTickEvent.Pre event) {
		Level level = event.getLevel();
		if (level instanceof ClientLevel)
			LevelTickTrigger.PRE_CLIENT_LEVEL_TICK.definition().priority(EventPriority.LOW).execute(event.getLevel());
		else
			LevelTickTrigger.PRE_SERVER_LEVEL_TICK.definition().priority(EventPriority.LOW).execute(event.getLevel());
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	private static void preLevelTickLowest(LevelTickEvent.Pre event) {
		Level level = event.getLevel();
		if (level instanceof ClientLevel)
			LevelTickTrigger.PRE_CLIENT_LEVEL_TICK.definition().priority(EventPriority.LOWEST).execute(event.getLevel());
		else
			LevelTickTrigger.PRE_SERVER_LEVEL_TICK.definition().priority(EventPriority.LOWEST).execute(event.getLevel());
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	private static void postLevelTickHighest(LevelTickEvent.Post event) {
		Level level = event.getLevel();
		if (level instanceof ClientLevel)
			LevelTickTrigger.POST_CLIENT_LEVEL_TICK.definition().priority(EventPriority.HIGHEST).execute(event.getLevel());
		else
			LevelTickTrigger.POST_SERVER_LEVEL_TICK.definition().priority(EventPriority.HIGHEST).execute(event.getLevel());
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	private static void postLevelTickHigh(LevelTickEvent.Post event) {
		Level level = event.getLevel();
		if (level instanceof ClientLevel)
			LevelTickTrigger.POST_CLIENT_LEVEL_TICK.definition().priority(EventPriority.HIGH).execute(event.getLevel());
		else
			LevelTickTrigger.POST_SERVER_LEVEL_TICK.definition().priority(EventPriority.HIGH).execute(event.getLevel());
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	private static void postLevelTickLow(LevelTickEvent.Post event) {
		Level level = event.getLevel();
		if (level instanceof ClientLevel)
			LevelTickTrigger.POST_CLIENT_LEVEL_TICK.definition().priority(EventPriority.LOW).execute(event.getLevel());
		else
			LevelTickTrigger.POST_SERVER_LEVEL_TICK.definition().priority(EventPriority.LOW).execute(event.getLevel());
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	private static void postLevelTickLowest(LevelTickEvent.Post event) {
		Level level = event.getLevel();
		if (level instanceof ClientLevel)
			LevelTickTrigger.POST_CLIENT_LEVEL_TICK.definition().priority(EventPriority.LOWEST).execute(event.getLevel());
		else
			LevelTickTrigger.POST_SERVER_LEVEL_TICK.definition().priority(EventPriority.LOWEST).execute(event.getLevel());
	}

	@Override
	public void executeCallback(LevelTickOperation op, Object... args) {
		op.operate((Level) args[0]);
	}
}