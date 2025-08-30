package lepus.mc.event;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.Connection;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;;

/**
 * 客户端生命周期的网络事件管理
 */
@OnlyIn(Dist.CLIENT)
@EventBusSubscriber
public enum ClientPlayerNetworkTrigger implements EventTrigger<ClientPlayerNetworkTrigger.Operation> {
	CLIENT_LOGGING_IN(EventPriority.LOW), // 世界更新前
	CLIENT_LOGGING_OUT(EventPriority.LOW); // 世界更新后

	public static Connection connection;
	public static LocalPlayer player;

	@FunctionalInterface
	public static interface Operation {
		public void operate(MultiPlayerGameMode multiPlayerGameMode, Connection connection, LocalPlayer player, ClientLevel level);
	}

	ClientPlayerNetworkTrigger(EventPriority defaultPriority) {
		define(defaultPriority);
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	private static void loggingInHighest(ClientPlayerNetworkEvent.LoggingIn event) {
		ClientPlayerNetworkTrigger.CLIENT_LOGGING_IN.definition().priority(EventPriority.HIGHEST).execute(event.getMultiPlayerGameMode(), event.getConnection(), event.getPlayer());
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	private static void loggingInHigh(ClientPlayerNetworkEvent.LoggingIn event) {
		ClientPlayerNetworkTrigger.CLIENT_LOGGING_IN.definition().priority(EventPriority.HIGH).execute(event.getMultiPlayerGameMode(), event.getConnection(), event.getPlayer());
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	private static void loggingInLow(ClientPlayerNetworkEvent.LoggingIn event) {
		ClientPlayerNetworkTrigger.CLIENT_LOGGING_IN.definition().priority(EventPriority.LOW).execute(event.getMultiPlayerGameMode(), event.getConnection(), event.getPlayer());
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	private static void loggingInLowest(ClientPlayerNetworkEvent.LoggingIn event) {
		ClientPlayerNetworkTrigger.CLIENT_LOGGING_IN.definition().priority(EventPriority.LOWEST).execute(event.getMultiPlayerGameMode(), event.getConnection(), event.getPlayer());
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	private static void loggingOutHighest(ClientPlayerNetworkEvent.LoggingOut event) {
		ClientPlayerNetworkTrigger.CLIENT_LOGGING_OUT.definition().priority(EventPriority.HIGHEST).execute(event.getMultiPlayerGameMode(), event.getConnection(), event.getPlayer());
		clearCache();
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	private static void loggingOutHigh(ClientPlayerNetworkEvent.LoggingOut event) {
		ClientPlayerNetworkTrigger.CLIENT_LOGGING_OUT.definition().priority(EventPriority.HIGH).execute(event.getMultiPlayerGameMode(), event.getConnection(), event.getPlayer());
		clearCache();
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	private static void loggingOutLow(ClientPlayerNetworkEvent.LoggingOut event) {
		ClientPlayerNetworkTrigger.CLIENT_LOGGING_OUT.definition().priority(EventPriority.LOW).execute(event.getMultiPlayerGameMode(), event.getConnection(), event.getPlayer());
		clearCache();
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	private static void loggingOutLowest(ClientPlayerNetworkEvent.LoggingOut event) {
		ClientPlayerNetworkTrigger.CLIENT_LOGGING_OUT.definition().priority(EventPriority.LOWEST).execute(event.getMultiPlayerGameMode(), event.getConnection(), event.getPlayer());
		clearCache();
	}

	/**
	 * 断开连接后就清楚连接和本地玩家、数据包注册表
	 */
	private static void clearCache() {
		ClientPlayerNetworkTrigger.connection = null;
		ClientPlayerNetworkTrigger.player = null;
	}

	@Override
	public void executeCallback(Operation op, Object... args) {
		MultiPlayerGameMode multiPlayerGameMode = (MultiPlayerGameMode) args[0];
		Connection connection = (Connection) args[1];
		ClientPlayerNetworkTrigger.connection = connection;
		LocalPlayer player = (LocalPlayer) args[2];
		ClientPlayerNetworkTrigger.player = player;
		op.operate(multiPlayerGameMode, connection, player, player.clientLevel);
	}
}