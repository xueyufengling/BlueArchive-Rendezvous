package fw.core.event;

import fw.core.Core;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber(modid = Core.ModId)
public enum ServerTickTrigger implements EventTrigger {
	PRE_SERVER_TICK(EventPriority.HIGHEST), // 世界更新前
	POST_SERVER_TICK(EventPriority.LOWEST); // 世界更新后

	ServerTickTrigger(EventPriority defaultPriority) {
		define(defaultPriority);
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	private static void preServerTickHighest(ServerTickEvent.Pre event) {
		ServerTickTrigger.PRE_SERVER_TICK.definition().priority(EventPriority.HIGHEST).execute();
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	private static void preServerTickHigh(ServerTickEvent.Pre event) {
		ServerTickTrigger.PRE_SERVER_TICK.definition().priority(EventPriority.HIGH).execute();
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	private static void preServerTickLow(ServerTickEvent.Pre event) {
		ServerTickTrigger.PRE_SERVER_TICK.definition().priority(EventPriority.LOW).execute();
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	private static void preServerTickLowest(ServerTickEvent.Pre event) {
		ServerTickTrigger.PRE_SERVER_TICK.definition().priority(EventPriority.LOWEST).execute();
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	private static void postServerTickHighest(ServerTickEvent.Post event) {
		ServerTickTrigger.POST_SERVER_TICK.definition().priority(EventPriority.HIGHEST).execute();
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	private static void postServerTickHigh(ServerTickEvent.Post event) {
		ServerTickTrigger.POST_SERVER_TICK.definition().priority(EventPriority.HIGH).execute();
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	private static void postServerTickLow(ServerTickEvent.Post event) {
		ServerTickTrigger.POST_SERVER_TICK.definition().priority(EventPriority.LOW).execute();
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	private static void postServerTickLowest(ServerTickEvent.Post event) {
		ServerTickTrigger.POST_SERVER_TICK.definition().priority(EventPriority.LOWEST).execute();
	}
}