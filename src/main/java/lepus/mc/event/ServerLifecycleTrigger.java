package lepus.mc.event;

import lepus.mc.core.ServerInstance;
import lyra.object.ObjectManipulator;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;

@EventBusSubscriber
public enum ServerLifecycleTrigger implements EventTrigger<ServerLifecycleTrigger.Operation> {
	BEFORE_SERVER_START(EventPriority.HIGH), // 未加载世界
	AFTER_SERVER_LOAD_LEVEL(EventPriority.LOW), // 加载完世界
	AFTER_SERVER_STARTED(EventPriority.LOW), // 服务器加载全部完成
	BEFORE_SERVER_STOP(EventPriority.LOW), // 世界保存前
	AFTER_SERVER_STOP(EventPriority.LOW);// 世界保存后，服务器完全关闭

	@FunctionalInterface
	public static interface Operation {
		public void operate(MinecraftServer server);
	}

	ServerLifecycleTrigger(EventPriority defaultPriority) {
		define(defaultPriority);
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST) // 最高优先级以获取注册表
	private static void onServerAboutToStartHighest(ServerAboutToStartEvent event) {
		ServerInstance.setServer(event.getServer());
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	private static void onServerAboutToStartHigh(ServerAboutToStartEvent event) {
		ServerLifecycleTrigger.BEFORE_SERVER_START.definition().priority(EventPriority.HIGH).execute();
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	private static void onServerAboutToStartLow(ServerAboutToStartEvent event) {
		ServerLifecycleTrigger.BEFORE_SERVER_START.definition().priority(EventPriority.LOW).execute();
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	private static void onServerAboutToStartLowest(ServerAboutToStartEvent event) {
		ServerLifecycleTrigger.BEFORE_SERVER_START.definition().priority(EventPriority.LOWEST).execute();
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	private static void onServerStartingHighest(ServerStartingEvent event) {
		ServerLifecycleTrigger.AFTER_SERVER_LOAD_LEVEL.definition().priority(EventPriority.HIGHEST).execute();
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	private static void onServerStartingHigh(ServerStartingEvent event) {
		ServerLifecycleTrigger.AFTER_SERVER_LOAD_LEVEL.definition().priority(EventPriority.HIGH).execute();
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	private static void onServerStartingLow(ServerStartingEvent event) {
		ServerLifecycleTrigger.AFTER_SERVER_LOAD_LEVEL.definition().priority(EventPriority.LOW).execute();
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	private static void onServerStartingLowest(ServerStartingEvent event) {
		ServerLifecycleTrigger.AFTER_SERVER_LOAD_LEVEL.definition().priority(EventPriority.LOWEST).execute();
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	private static void onServerStartedHighest(ServerStartedEvent event) {
		ServerLifecycleTrigger.AFTER_SERVER_STARTED.definition().priority(EventPriority.HIGHEST).execute();
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	private static void onServerStartedHigh(ServerStartedEvent event) {
		ServerLifecycleTrigger.AFTER_SERVER_STARTED.definition().priority(EventPriority.HIGH).execute();
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	private static void onServerStartedLow(ServerStartedEvent event) {
		ServerLifecycleTrigger.AFTER_SERVER_STARTED.definition().priority(EventPriority.LOW).execute();
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	private static void onServerStartedLowest(ServerStartedEvent event) {
		ServerLifecycleTrigger.AFTER_SERVER_STARTED.definition().priority(EventPriority.LOWEST).execute();
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	private static void onServerStoppingHighest(ServerStoppingEvent event) {
		ServerLifecycleTrigger.BEFORE_SERVER_STOP.definition().priority(EventPriority.HIGHEST).execute();
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	private static void onServerStoppingHigh(ServerStoppingEvent event) {
		ServerLifecycleTrigger.BEFORE_SERVER_STOP.definition().priority(EventPriority.HIGH).execute();
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	private static void onServerStoppingLow(ServerStoppingEvent event) {
		ServerLifecycleTrigger.BEFORE_SERVER_STOP.definition().priority(EventPriority.LOW).execute();
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	private static void onServerStoppingLowest(ServerStoppingEvent event) {
		ServerLifecycleTrigger.BEFORE_SERVER_STOP.definition().priority(EventPriority.LOWEST).execute();
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	private static void onServerStoppedHighest(ServerStoppedEvent event) {
		ServerLifecycleTrigger.AFTER_SERVER_STOP.definition().priority(EventPriority.HIGHEST).execute();
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	private static void onServerStoppedHigh(ServerStoppedEvent event) {
		ServerLifecycleTrigger.AFTER_SERVER_STOP.definition().priority(EventPriority.HIGH).execute();
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	private static void onServerStoppedLow(ServerStoppedEvent event) {
		ServerLifecycleTrigger.AFTER_SERVER_STOP.definition().priority(EventPriority.LOW).execute();
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	private static void onServerStoppedLowest(ServerStoppedEvent event) {
		ServerLifecycleTrigger.AFTER_SERVER_STOP.definition().priority(EventPriority.LOWEST).execute();
		ObjectManipulator.setObject(ServerInstance.class, "server", null);// 服务器停止后将ServerInstance的server对象置空
	}

	@Override
	public void executeCallback(Operation op, Object... args) {
		op.operate(ServerInstance.server);
	}
}