package fw.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import fw.core.event.ServerLifecycleTrigger;
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

public class ServerInstance {

	@FunctionalInterface
	public static interface Operation {
		public void operate(MinecraftServer server);
	}

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
	public static final void setServer(MinecraftServer server) {
		ObjectManipulator.setObject(ServerInstance.class, "server", server);
		ObjectManipulator.setObject(ServerInstance.class, "levels", levels(server));
		// 如果不使用MappedRegistries.registryAccess，那么就无法修改MappedRegistries.registryAccess的值
		Placeholders.NotInlined(MappedRegistryAccess.serverRegistryAccess);
		if (!ObjectManipulator.setObject(MappedRegistryAccess.class, "serverRegistryAccess", server.registryAccess()))
			Core.logError("Get server registryAccess failed.");
		connections = server.getConnection();
		RegistryFieldsInitializer.Dynamic.initializeFields();// 初始化动态注册表字段
		ServerLifecycleTrigger.BEFORE_SERVER_START.definition().priority(EventPriority.HIGHEST).execute();
		RegistryFieldsInitializer.Dynamic.freeze();
	}
}
