package fw.event;

import fw.core.Core;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = Core.ModId)
public enum ClientLifecycleTrigger implements EventTrigger<ClientLifecycleTrigger.Operation> {
	CLIENT_CONNECT(EventPriority.LOWEST), // 世界更新前
	CLIENT_DISCONNECT(EventPriority.LOWEST); // 世界更新后

	@FunctionalInterface
	public static interface Operation {
		public void operate(Player player);
	}

	ClientLifecycleTrigger(EventPriority defaultPriority) {
		define(defaultPriority);
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	private static void playerLoggedInHighest(PlayerEvent.PlayerLoggedInEvent event) {
		ClientLifecycleTrigger.CLIENT_CONNECT.definition().priority(EventPriority.HIGHEST).execute(event.getEntity());
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	private static void playerLoggedInHigh(PlayerEvent.PlayerLoggedInEvent event) {
		ClientLifecycleTrigger.CLIENT_CONNECT.definition().priority(EventPriority.HIGH).execute(event.getEntity());
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	private static void playerLoggedInLow(PlayerEvent.PlayerLoggedInEvent event) {
		ClientLifecycleTrigger.CLIENT_CONNECT.definition().priority(EventPriority.LOW).execute(event.getEntity());
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	private static void playerLoggedInLowest(PlayerEvent.PlayerLoggedInEvent event) {
		ClientLifecycleTrigger.CLIENT_CONNECT.definition().priority(EventPriority.LOWEST).execute(event.getEntity());
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	private static void playerLoggedOutHighest(PlayerEvent.PlayerLoggedOutEvent event) {
		ClientLifecycleTrigger.CLIENT_DISCONNECT.definition().priority(EventPriority.HIGHEST).execute(event.getEntity());
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	private static void playerLoggedOutHigh(PlayerEvent.PlayerLoggedOutEvent event) {
		ClientLifecycleTrigger.CLIENT_DISCONNECT.definition().priority(EventPriority.HIGH).execute(event.getEntity());
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	private static void playerLoggedOutLow(PlayerEvent.PlayerLoggedOutEvent event) {
		ClientLifecycleTrigger.CLIENT_DISCONNECT.definition().priority(EventPriority.LOW).execute(event.getEntity());
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	private static void playerLoggedOutLowest(PlayerEvent.PlayerLoggedOutEvent event) {
		ClientLifecycleTrigger.CLIENT_DISCONNECT.definition().priority(EventPriority.LOWEST).execute(event.getEntity());
	}

	@Override
	public void executeCallback(Operation op, Object... args) {
		op.operate((Player) args[0]);
	}
}