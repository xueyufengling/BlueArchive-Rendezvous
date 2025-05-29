package fw.core;

import fw.core.registry.MappedRegistryManipulator;
import lyra.klass.ObjectManipulator;
import lyra.klass.Placeholders;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerConnectionListener;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@EventBusSubscriber(modid = Core.ModId)
public class ServerEntry {
	@SuppressWarnings("unused")
	private static MinecraftServer server;
	static ServerConnectionListener connections;

	private static ServerOperation serverCallback;

	public static final void setServerCallback(ServerOperation op) {
		serverCallback = op;
	}

	public static final void setServer(MinecraftServer server) {
		ServerEntry.server = server;
		// 如果不使用MappedRegistryManipulator.registryAccess，那么就无法修改MappedRegistryManipulator.registryAccess的值
		Placeholders.NotInlined(MappedRegistryManipulator.serverRegistryAccess);
		if (!ObjectManipulator.setObject(MappedRegistryManipulator.class, "serverRegistryAccess", server.registryAccess()))
			System.err.println("Get server registryAccess failed.");
		connections = server.getConnection();
		serverCallback.operate(server);
	}

	public static final MinecraftServer getServer(MinecraftServer server) {
		return server;
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST) // 最高优先级以获取注册表
	public static void onServerStartup(ServerStartingEvent event) {
		setServer(event.getServer());
	}
}
