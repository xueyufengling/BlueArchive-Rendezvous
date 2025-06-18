package fw.core;

import java.util.ArrayList;
import java.util.Map;

import fw.core.registry.MappedRegistries;
import lyra.alpha.reference.Recoverable;
import lyra.object.ObjectManipulator;
import lyra.object.Placeholders;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.network.ServerConnectionListener;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;

@EventBusSubscriber(modid = Core.ModId)
public class ServerEntry {
	@FunctionalInterface
	public static interface Operation {
		public void operate(MinecraftServer server);
	}

	/**
	 * 不论单人还是多人都有服务器，，单人为内置服务器，多人则是外部服务器
	 */
	private static MinecraftServer server;
	static ServerConnectionListener connections;

	private static ArrayList<Operation> beforeServerStartCallbacks = new ArrayList<>();
	private static ArrayList<Operation> afterServerLoadLevelCallbacks = new ArrayList<>();
	private static ArrayList<Operation> afterServerStartedCallbacks = new ArrayList<>();

	private static ArrayList<Operation> beforServerStopCallbacks = new ArrayList<>();
	private static ArrayList<Operation> afterServerStopCallbacks = new ArrayList<>();

	/**
	 * 设置服务器构建好启动前的回调，此时数据包注册表全部加载完成，但还未创建和初始化Level
	 * 
	 * @param op
	 */
	public static final void addBeforeServerStartCallback(Operation op) {
		beforeServerStartCallbacks.add(op);
	}

	public static final void addAfterServerLoadLevelCallback(Operation op) {
		afterServerLoadLevelCallbacks.add(op);
	}

	public static final void addAfterServerStartedCallbacks(Operation op) {
		afterServerStartedCallbacks.add(op);
	}

	public static final void addBeforeServerStopCallback(Operation op) {
		beforServerStopCallbacks.add(op);
	}

	public static final void addAfterServerStopCallbacks(Operation op) {
		afterServerStopCallbacks.add(op);
	}

	/**
	 * 获取服务器储存所有维度的Map
	 * 
	 * @param server
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static final Map<ResourceKey<Level>, ServerLevel> levels(MinecraftServer server) {
		return (Map<ResourceKey<Level>, ServerLevel>) ObjectManipulator.access(server, "levels");
	}

	/**
	 * 设置目标服务器的维度
	 * 
	 * @param server
	 * @param levels
	 */
	public static final void setLevels(MinecraftServer server, Map<ResourceKey<Level>, ServerLevel> levels) {
		ObjectManipulator.setObject(MinecraftServer.class, "levels", levels);
	}

	public static final Map<ResourceKey<Level>, ServerLevel> levels = null;

	/**
	 * 设置当前服务器
	 * 
	 * @param server
	 */
	private static final void setServer(MinecraftServer server) {
		ServerEntry.server = server;
		ObjectManipulator.setObject(ServerEntry.class, "levels", levels(server));
		// 如果不使用MappedRegistries.registryAccess，那么就无法修改MappedRegistries.registryAccess的值
		Placeholders.NotInlined(MappedRegistries.serverRegistryAccess);
		if (!ObjectManipulator.setObject(MappedRegistries.class, "serverRegistryAccess", server.registryAccess()))
			System.err.println("Get server registryAccess failed.");
		connections = server.getConnection();
		MappedRegistries.fetchRegistries();
		if (!beforeServerStartCallbacks.isEmpty())
			for (Operation beforeServerStartCallback : beforeServerStartCallbacks)
				beforeServerStartCallback.operate(server);
		for (Recoverable<?> ref : recoverableRedirectors)
			ref.redirect();
		MappedRegistries.freezeRegistries();
	}

	public static final MinecraftServer getServer() {
		return server;
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST) // 最高优先级以获取注册表
	public static void onServerAboutToStart(ServerAboutToStartEvent event) {
		setServer(event.getServer());
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onServerStarting(ServerStartingEvent event) {
		if (!afterServerLoadLevelCallbacks.isEmpty())
			for (Operation afterServerLoadLevelCallback : afterServerLoadLevelCallbacks)
				afterServerLoadLevelCallback.operate(server);
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onServerStarted(ServerStartedEvent event) {
		if (!afterServerStartedCallbacks.isEmpty())
			for (Operation afterServerStartedCallback : afterServerStartedCallbacks)
				afterServerStartedCallback.operate(server);
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onServerStopping(ServerStoppingEvent event) {
		if (!beforServerStopCallbacks.isEmpty())
			for (Operation beforServerStopCallback : beforServerStopCallbacks)
				beforServerStopCallback.operate(server);
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onServerStopped(ServerStoppedEvent event) {
		if (!afterServerStopCallbacks.isEmpty())
			for (Operation afterServerStopCallback : afterServerStopCallbacks)
				afterServerStopCallback.operate(server);
		for (Recoverable<?> ref : recoverableRedirectors)
			ref.recovery();
	}

	private static final ArrayList<Recoverable<?>> recoverableRedirectors = new ArrayList<>();

	/**
	 * 托管临时的字段重定向，服务器启动时将字段重定向为指定值，并在服务器退出时将字段恢复。<br>
	 * 该功能用于重定向MC原版的静态ResourceKey引用，防止修改后退出重新进入世界时验证数据包失败，这是因为代码和数据包json的耦合性，进入世界时需要确保原版的数据包和引用完整如初。
	 * 
	 * @param references
	 */
	public static final void delegateRecoverableRedirectors(Recoverable<?>... references) {
		for (Recoverable<?> ref : references)
			if (!recoverableRedirectors.contains(ref))
				recoverableRedirectors.add(ref);
	}
}
