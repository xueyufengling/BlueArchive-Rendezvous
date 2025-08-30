package lepus.mc.core;

import lepus.mc.core.registry.MappedRegistryAccess;
import lepus.mc.event.ServerLifecycleTrigger;
import lepus.mc.event.ServerTickTrigger;
import lepus.mc.event.ServerLifecycleTrigger.Operation;
import lepus.mc.event.ServerTickTrigger.TickOperation;
import lyra.alpha.reference.Recoverable;
import lyra.object.ObjectManipulator;
import lyra.object.Placeholders;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerConnectionListener;
import net.neoforged.bus.api.EventPriority;

public class ServerInstance {

	/**
	 * 不论单人还是多人都有服务器，，单人为内置服务器，多人则是外部服务器
	 */
	public static final MinecraftServer server = null;
	static ServerConnectionListener connections;

	/**
	 * 本类是否当前可用
	 * 
	 * @return
	 */
	public static final boolean available() {
		return server != null;
	}

	/**
	 * 设置服务器构建好启动前的回调，此时数据包注册表全部加载完成，但还未创建和初始化Level
	 * 
	 * @param op
	 */
	public static final void addBeforeServerStartCallback(Operation op) {
		ServerLifecycleTrigger.BEFORE_SERVER_START.addCallback(op);
	}

	public static final void addAfterServerLoadLevelCallback(Operation op) {
		ServerLifecycleTrigger.AFTER_SERVER_LOAD_LEVEL.addCallback(op);
	}

	public static final void addAfterServerStartedCallback(Operation op) {
		ServerLifecycleTrigger.AFTER_SERVER_STARTED.addCallback(op);
	}

	public static final void addBeforeServerStopCallback(Operation op) {
		ServerLifecycleTrigger.BEFORE_SERVER_STOP.addCallback(op);
	}

	public static final void addAfterServerStopCallback(Operation op) {
		ServerLifecycleTrigger.AFTER_SERVER_STOP.addCallback(op);
	}

	public static final void addTempBeforeServerStartCallback(Operation op) {
		ServerLifecycleTrigger.BEFORE_SERVER_START.addTempCallback(op);
	}

	public static final void addTempAfterServerLoadLevelCallback(Operation op) {
		ServerLifecycleTrigger.AFTER_SERVER_LOAD_LEVEL.addTempCallback(op);
	}

	public static final void addTempAfterServerStartedCallback(Operation op) {
		ServerLifecycleTrigger.AFTER_SERVER_STARTED.addTempCallback(op);
	}

	public static final void addTempBeforeServerStopCallback(Operation op) {
		ServerLifecycleTrigger.BEFORE_SERVER_STOP.addTempCallback(op);
	}

	public static final void addTempAfterServerStopCallback(Operation op) {
		ServerLifecycleTrigger.AFTER_SERVER_STOP.addTempCallback(op);
	}

	public static final void addTempPreServerTickCallback(TickOperation op) {
		ServerTickTrigger.PRE_SERVER_TICK.addTempCallback(op);
	}

	public static final void addTempPostServerTickCallback(TickOperation op) {
		ServerTickTrigger.POST_SERVER_TICK.addTempCallback(op);
	}

	// 服务器运行中tick回调
	public static final void addPreServerTickCallback(TickOperation op) {
		ServerTickTrigger.PRE_SERVER_TICK.addCallback(op);
	}

	public static final void addPostServerTickCallback(TickOperation op) {
		ServerTickTrigger.POST_SERVER_TICK.addCallback(op);
	}

	/**
	 * 托管临时的字段重定向，服务器启动时将字段重定向为指定值，并在服务器退出时将字段恢复。<br>
	 * 该功能用于重定向MC原版的静态ResourceKey引用，防止修改后退出重新进入世界时验证数据包失败，这是因为代码和数据包json的耦合性，进入世界时需要确保原版的数据包和引用完整如初。
	 * 
	 * @param redirectTrigger
	 * @param recoveryTrigger
	 * @param references
	 */
	public static final void delegateRecoverableRedirectors(ServerLifecycleTrigger redirectTrigger, ServerLifecycleTrigger recoveryTrigger, Recoverable<?>... references) {
		for (Recoverable<?> ref : references) {
			redirectTrigger.addRedirectRecoverable(ref);
			recoveryTrigger.addRecoveryRecoverable(ref);
		}
	}

	public static final void delegateRecoverableRedirectors(ServerLifecycleTrigger redirectTrigger, EventPriority redirectPriiority, ServerLifecycleTrigger recoveryTrigger, EventPriority recoveryPriiority, Recoverable<?>... references) {
		for (Recoverable<?> ref : references) {
			redirectTrigger.addRedirectRecoverable(redirectPriiority, ref);
			recoveryTrigger.addRecoveryRecoverable(recoveryPriiority, ref);
		}
	}

	/**
	 * 设置当前服务器
	 * 
	 * @param server
	 */
	public static final void setServer(MinecraftServer server) {
		ObjectManipulator.setStaticObject(ServerInstance.class, "server", server);
		ServerLevels.initLevels(server);
		// 如果不使用MappedRegistries.registryAccess，那么就无法修改MappedRegistries.registryAccess的值
		Placeholders.NotInlined(MappedRegistryAccess.serverRegistryAccess);
		ObjectManipulator.setStaticObject(MappedRegistryAccess.class, "serverRegistryAccess", server.registryAccess());
		connections = server.getConnection();
		RegistryFieldsInitializer.DynamicServer.initializeFields();// 初始化动态注册表字段
		ServerLifecycleTrigger.BEFORE_SERVER_START.definition().priority(EventPriority.HIGHEST).execute();
		RegistryFieldsInitializer.DynamicServer.freeze();
	}
}
