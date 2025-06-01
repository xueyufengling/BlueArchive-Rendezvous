package fw.core;

import java.util.ArrayList;

import fw.core.registry.MappedRegistries;
import lyra.klass.FieldReference;
import lyra.klass.ObjectManipulator;
import lyra.klass.Placeholders;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerConnectionListener;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;

@EventBusSubscriber(modid = Core.ModId)
public class ServerEntry {
	@FunctionalInterface
	public static interface Operation {
		public void operate(MinecraftServer server);
	}

	private static MinecraftServer server;
	static ServerConnectionListener connections;

	private static Operation serverStartCallback;
	private static Operation serverStopCallback;

	public static final void setServerStartCallback(Operation op) {
		serverStartCallback = op;
	}

	public static final void setServerStopCallback(Operation op) {
		serverStopCallback = op;
	}

	public static final void setServer(MinecraftServer server) {
		ServerEntry.server = server;
		// 如果不使用MappedRegistries.registryAccess，那么就无法修改MappedRegistries.registryAccess的值
		Placeholders.NotInlined(MappedRegistries.serverRegistryAccess);
		if (!ObjectManipulator.setObject(MappedRegistries.class, "serverRegistryAccess", server.registryAccess()))
			System.err.println("Get server registryAccess failed.");
		connections = server.getConnection();
		MappedRegistries.fetchRegistries();
		if (serverStartCallback != null)
			serverStartCallback.operate(server);
		for (FieldReference ref : tempFieldRedirectors)
			ref.redirect();
		MappedRegistries.freezeRegistries();
	}

	public static final MinecraftServer getServer() {
		return server;
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST) // 最高优先级以获取注册表
	public static void onServerStartup(ServerStartingEvent event) {
		setServer(event.getServer());
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onServerStartup(ServerStoppingEvent event) {
		if (serverStopCallback != null)
			serverStopCallback.operate(server);
		for (FieldReference ref : tempFieldRedirectors)
			ref.recovery();
	}

	private static final ArrayList<FieldReference> tempFieldRedirectors = new ArrayList<>();

	/**
	 * 托管临时的字段重定向，服务器启动时将字段重定向为指定值，并在服务器退出时将字段恢复。<br>
	 * 该功能用于重定向MC原版的静态ResourceKey引用，防止修改后重新进入世界时验证数据包失败，这是因为代码和数据包json的耦合性，进入世界时需要确保原版的数据包和引用完整如初。
	 * 
	 * @param references
	 */
	public static final void delegateTempFieldRedirectors(FieldReference... references) {
		for (FieldReference ref : references)
			if (!tempFieldRedirectors.contains(ref))
				tempFieldRedirectors.add(ref);
	}
}
