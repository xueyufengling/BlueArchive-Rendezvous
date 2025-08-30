package lepus.mc.event;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber
public enum ClientTickTrigger implements EventTrigger<ClientTickTrigger.TickOperation> {
	PRE_CLIENT_TICK(EventPriority.HIGH), // 世界更新前
	POST_CLIENT_TICK(EventPriority.LOW); // 世界更新后

	@FunctionalInterface
	public static interface TickOperation {
		public void operate(LocalPlayer player, ClientLevel level);
	}

	ClientTickTrigger(EventPriority defaultPriority) {
		define(defaultPriority);
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	private static void preClientTickHighest(ClientTickEvent.Pre event) {
		ClientTickTrigger.PRE_CLIENT_TICK.definition().priority(EventPriority.HIGHEST).execute();
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	private static void preClientTickHigh(ClientTickEvent.Pre event) {
		ClientTickTrigger.PRE_CLIENT_TICK.definition().priority(EventPriority.HIGH).execute();
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	private static void preClientTickLow(ClientTickEvent.Pre event) {
		ClientTickTrigger.PRE_CLIENT_TICK.definition().priority(EventPriority.LOW).execute();
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	private static void preClientTickLowest(ClientTickEvent.Pre event) {
		ClientTickTrigger.PRE_CLIENT_TICK.definition().priority(EventPriority.LOWEST).execute();
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	private static void postClientTickHighest(ClientTickEvent.Post event) {
		ClientTickTrigger.POST_CLIENT_TICK.definition().priority(EventPriority.HIGHEST).execute();
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	private static void postClientTickHigh(ClientTickEvent.Post event) {
		ClientTickTrigger.POST_CLIENT_TICK.definition().priority(EventPriority.HIGH).execute();
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	private static void postClientTickLow(ClientTickEvent.Post event) {
		ClientTickTrigger.POST_CLIENT_TICK.definition().priority(EventPriority.LOW).execute();
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	private static void postClientTickLowest(ClientTickEvent.Post event) {
		ClientTickTrigger.POST_CLIENT_TICK.definition().priority(EventPriority.LOWEST).execute();
	}

	@Override
	public void executeCallback(TickOperation op, Object... args) {
		LocalPlayer player = ClientPlayerNetworkTrigger.player;
		op.operate(player, player == null ? null : player.clientLevel);
	}
}