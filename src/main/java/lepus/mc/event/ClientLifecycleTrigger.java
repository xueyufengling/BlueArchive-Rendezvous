package lepus.mc.event;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.LevelAccessor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.LevelEvent;;

/**
 * 客户端生命周期及其本地玩家、世界、数据包注册表管理<br>
 * LevelEvent.Load先于ClientPlayerNetworkEvent.LoggingIn触发，即先加载世界再创建玩家
 */
@OnlyIn(Dist.CLIENT)
@EventBusSubscriber
public enum ClientLifecycleTrigger implements EventTrigger<ClientLifecycleTrigger.Operation> {
	CLIENT_CONNECT(EventPriority.LOW), // 世界更新前
	CLIENT_DISCONNECT(EventPriority.LOW); // 世界更新后

	public static ClientLevel level;
	public static RegistryAccess.Frozen registryAccess;

	@FunctionalInterface
	public static interface Operation {
		public void operate(ClientLevel level, RegistryAccess.Frozen registryAccess);
	}

	ClientLifecycleTrigger(EventPriority defaultPriority) {
		define(defaultPriority);
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	private static void loggedInHighest(LevelEvent.Load event) {
		LevelAccessor level = event.getLevel();
		if (level instanceof ClientLevel)
			ClientLifecycleTrigger.CLIENT_CONNECT.definition().priority(EventPriority.HIGHEST).execute(level);
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	private static void loggedInHigh(LevelEvent.Load event) {
		LevelAccessor level = event.getLevel();
		if (level instanceof ClientLevel)
			ClientLifecycleTrigger.CLIENT_CONNECT.definition().priority(EventPriority.HIGH).execute(level);
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	private static void loggedInLow(LevelEvent.Load event) {
		LevelAccessor level = event.getLevel();
		if (level instanceof ClientLevel)
			ClientLifecycleTrigger.CLIENT_CONNECT.definition().priority(EventPriority.LOW).execute(level);
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	private static void loggedInLowest(LevelEvent.Load event) {
		LevelAccessor level = event.getLevel();
		if (level instanceof ClientLevel)
			ClientLifecycleTrigger.CLIENT_CONNECT.definition().priority(EventPriority.LOWEST).execute(level);
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	private static void loggedOutHighest(LevelEvent.Unload event) {
		LevelAccessor level = event.getLevel();
		if (level instanceof ClientLevel)
			ClientLifecycleTrigger.CLIENT_DISCONNECT.definition().priority(EventPriority.HIGHEST).execute(level);
		clearCache();
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	private static void loggedOutHigh(LevelEvent.Unload event) {
		LevelAccessor level = event.getLevel();
		if (level instanceof ClientLevel)
			ClientLifecycleTrigger.CLIENT_DISCONNECT.definition().priority(EventPriority.HIGH).execute(level);
		clearCache();
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	private static void loggedOutLow(LevelEvent.Unload event) {
		LevelAccessor level = event.getLevel();
		if (level instanceof ClientLevel)
			ClientLifecycleTrigger.CLIENT_DISCONNECT.definition().priority(EventPriority.LOW).execute(level);
		clearCache();
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	private static void loggedOutLowest(LevelEvent.Unload event) {
		LevelAccessor level = event.getLevel();
		if (level instanceof ClientLevel)
			ClientLifecycleTrigger.CLIENT_DISCONNECT.definition().priority(EventPriority.LOWEST).execute(level);
		clearCache();
	}

	/**
	 * 断开连接后就清楚连接和本地玩家、数据包注册表
	 */
	private static void clearCache() {
		ClientLifecycleTrigger.level = null;
		ClientLifecycleTrigger.registryAccess = null;
	}

	@Override
	public void executeCallback(Operation op, Object... args) {
		ClientLevel level = (ClientLevel) args[0];
		ClientLifecycleTrigger.level = level;
		RegistryAccess.Frozen registryAccess = (RegistryAccess.Frozen) level.registryAccess();
		ClientLifecycleTrigger.registryAccess = registryAccess;
		op.operate(level, registryAccess);
	}
}