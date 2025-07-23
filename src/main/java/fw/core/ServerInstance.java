package fw.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import fw.core.registry.MappedRegistryAccess;
import fw.dimension.ExtDimension;
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
public class ServerInstance {
	public enum EventTrigger {
		BEFORE_SERVER_START, // 未加载世界
		AFTER_SERVER_LOAD_LEVEL, // 加载完世界
		AFTER_SERVER_STARTED, // 服务器加载全部完成
		BEFORE_SERVER_STOP, // 世界保存前
		AFTER_SERVER_STOP;// 世界保存后，服务器完全关闭

		private final ArrayList<Operation> callbacks = new ArrayList<>();
		/**
		 * 执行完成后就会移除的回调函数
		 */
		private final ArrayList<Operation> temp_callbacks = new ArrayList<>();
		private final ArrayList<Recoverable<?>> redirect_recoverables = new ArrayList<>();
		private final ArrayList<Recoverable<?>> recovery_recoverables = new ArrayList<>();

		public final EventTrigger addCallbacks(Operation... ops) {
			for (Operation op : ops)
				callbacks.add(op);
			return this;
		}

		public final EventTrigger addTempCallbacks(Operation... ops) {
			for (Operation op : ops)
				temp_callbacks.add(op);
			return this;
		}

		public final EventTrigger addRedirectRecoverables(Recoverable<?>... refs) {
			for (Recoverable<?> ref : refs)
				if (!redirect_recoverables.contains(ref))
					redirect_recoverables.add(ref);
			return this;
		}

		public final EventTrigger addRecoveryRecoverables(Recoverable<?>... refs) {
			for (Recoverable<?> ref : refs)
				if (!redirect_recoverables.contains(ref))
					recovery_recoverables.add(ref);
			return this;
		}

		public final EventTrigger addCallback(Operation op) {
			callbacks.add(op);
			return this;
		}

		public final EventTrigger addTempCallback(Operation op) {
			temp_callbacks.add(op);
			return this;
		}

		public final EventTrigger addRedirectRecoverable(Recoverable<?> ref) {
			if (!redirect_recoverables.contains(ref))
				redirect_recoverables.add(ref);
			return this;
		}

		public final EventTrigger addRecoveryRecoverable(Recoverable<?> ref) {
			if (!redirect_recoverables.contains(ref))
				recovery_recoverables.add(ref);
			return this;
		}

		public final void execute() {
			for (Operation callback : callbacks)
				callback.operate(server);
			Iterator<Operation> iter = temp_callbacks.iterator();
			while (iter.hasNext()) {
				iter.next().operate(server);
				iter.remove();
			}
			for (Recoverable<?> ref : redirect_recoverables)
				ref.redirect();
			for (Recoverable<?> ref : recovery_recoverables)
				ref.recovery();
		}
	}

	@FunctionalInterface
	public static interface Operation {
		public void operate(MinecraftServer server);
	}

	/**
	 * 不论单人还是多人都有服务器，，单人为内置服务器，多人则是外部服务器
	 */
	private static MinecraftServer server = null;
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
		EventTrigger.BEFORE_SERVER_START.addCallback(op);
	}

	public static final void addAfterServerLoadLevelCallback(Operation op) {
		EventTrigger.AFTER_SERVER_LOAD_LEVEL.addCallback(op);
	}

	public static final void addAfterServerStartedCallback(Operation op) {
		EventTrigger.AFTER_SERVER_STARTED.addCallback(op);
	}

	public static final void addBeforeServerStopCallback(Operation op) {
		EventTrigger.BEFORE_SERVER_STOP.addCallback(op);
	}

	public static final void addAfterServerStopCallback(Operation op) {
		EventTrigger.AFTER_SERVER_STOP.addCallback(op);
	}

	public static final void addTempBeforeServerStartCallback(Operation op) {
		EventTrigger.BEFORE_SERVER_START.addTempCallback(op);
	}

	public static final void addTempAfterServerLoadLevelCallback(Operation op) {
		EventTrigger.AFTER_SERVER_LOAD_LEVEL.addTempCallback(op);
	}

	public static final void addTempAfterServerStartedCallback(Operation op) {
		EventTrigger.AFTER_SERVER_STARTED.addTempCallback(op);
	}

	public static final void addTempBeforeServerStopCallback(Operation op) {
		EventTrigger.BEFORE_SERVER_STOP.addTempCallback(op);
	}

	public static final void addTempAfterServerStopCallback(Operation op) {
		EventTrigger.AFTER_SERVER_STOP.addTempCallback(op);
	}

	/**
	 * 托管临时的字段重定向，服务器启动时将字段重定向为指定值，并在服务器退出时将字段恢复。<br>
	 * 该功能用于重定向MC原版的静态ResourceKey引用，防止修改后退出重新进入世界时验证数据包失败，这是因为代码和数据包json的耦合性，进入世界时需要确保原版的数据包和引用完整如初。
	 * 
	 * @param redirectTrigger
	 * @param recoveryTrigger
	 * @param references
	 */
	public static final void delegateRecoverableRedirectors(EventTrigger redirectTrigger, EventTrigger recoveryTrigger, Recoverable<?>... references) {
		for (Recoverable<?> ref : references) {
			redirectTrigger.addRedirectRecoverable(ref);
			recoveryTrigger.addRecoveryRecoverable(ref);
		}
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
	 * 移除世界并且不记录
	 * 
	 * @param level_keys
	 */
	public static final void removeLevels(String... level_keys) {
		for (String level_key : level_keys) {
			levels.remove(ExtDimension.Stem.levelKey(level_key));
		}
	}

	/**
	 * 移除世界并且不记录
	 * 
	 * @param level_keys
	 */
	@SuppressWarnings("unchecked")
	public static final void removeLevels(ResourceKey<Level>... level_keys) {
		for (ResourceKey<Level> level_key : level_keys) {
			levels.remove(level_key);
		}
	}

	/**
	 * 暂时移除的世界
	 */
	private static final Map<ResourceKey<Level>, ServerLevel> disabled_levels = new HashMap<>();

	public static final void disableLevels(String... level_keys) {
		for (String level_key : level_keys) {
			ResourceKey<Level> res_key = ExtDimension.Stem.levelKey(level_key);
			ServerLevel level = levels.remove(res_key);
			if (level != null)
				disabled_levels.put(res_key, level);
		}
	}

	/**
	 * 禁用世界，暂时移除
	 * 
	 * @param level_keys
	 */
	@SuppressWarnings("unchecked")
	public static final void disableLevels(ResourceKey<Level>... level_keys) {
		for (ResourceKey<Level> level_key : level_keys) {
			ServerLevel level = levels.remove(level_key);
			if (level != null)
				disabled_levels.put(level_key, level);
		}
	}

	public static final void disableAllLevels() {
		Iterator<Entry<ResourceKey<Level>, ServerLevel>> iter = levels.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<ResourceKey<Level>, ServerLevel> entry = iter.next();
			disabled_levels.put(entry.getKey(), entry.getValue());
			iter.remove();
		}
	}

	public static final void enableLevels(String... level_keys) {
		for (String level_key : level_keys) {
			ResourceKey<Level> res_key = ExtDimension.Stem.levelKey(level_key);
			ServerLevel level = disabled_levels.remove(res_key);
			if (level != null)
				levels.put(res_key, level);
		}
	}

	/**
	 * 启用世界，恢复到世界列表
	 * 
	 * @param level_keys
	 */
	@SuppressWarnings("unchecked")
	public static final void enableLevels(ResourceKey<Level>... level_keys) {
		for (ResourceKey<Level> level_key : level_keys) {
			ServerLevel level = disabled_levels.remove(level_key);
			if (level != null)
				levels.put(level_key, level);
		}
	}

	public static final void enableAllLevels() {
		Iterator<Entry<ResourceKey<Level>, ServerLevel>> iter = disabled_levels.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<ResourceKey<Level>, ServerLevel> entry = iter.next();
			levels.put(entry.getKey(), entry.getValue());
			iter.remove();
		}
	}

	/**
	 * 设置当前服务器
	 * 
	 * @param server
	 */
	static final void setServer(MinecraftServer server) {
		ServerInstance.server = server;
		ObjectManipulator.setObject(ServerInstance.class, "levels", levels(server));
		// 如果不使用MappedRegistries.registryAccess，那么就无法修改MappedRegistries.registryAccess的值
		Placeholders.NotInlined(MappedRegistryAccess.serverRegistryAccess);
		if (!ObjectManipulator.setObject(MappedRegistryAccess.class, "serverRegistryAccess", server.registryAccess()))
			Core.logError("Get server registryAccess failed.");
		connections = server.getConnection();
		RegistryFieldsInitializer.Dynamic.initializeFields();// 初始化动态注册表字段
		EventTrigger.BEFORE_SERVER_START.execute();
		RegistryFieldsInitializer.Dynamic.freeze();
	}

	public static final MinecraftServer getServer() {
		return server;
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST) // 最高优先级以获取注册表
	private static void onServerAboutToStart(ServerAboutToStartEvent event) {
		setServer(event.getServer());
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	private static void onServerStarting(ServerStartingEvent event) {
		EventTrigger.AFTER_SERVER_LOAD_LEVEL.execute();
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	private static void onServerStarted(ServerStartedEvent event) {
		EventTrigger.AFTER_SERVER_STARTED.execute();
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	private static void onServerStopping(ServerStoppingEvent event) {
		EventTrigger.BEFORE_SERVER_STOP.execute();
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	private static void onServerStopped(ServerStoppedEvent event) {
		EventTrigger.AFTER_SERVER_STOP.execute();
		server = null;
	}
}
