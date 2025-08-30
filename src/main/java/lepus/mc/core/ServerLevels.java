package lepus.mc.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import lepus.mc.dimension.ExtDimension;
import lyra.object.ObjectManipulator;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.PrimaryLevelData;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraft.world.level.storage.WritableLevelData;

public class ServerLevels {
	/**
	 * 每个Level都有一个WorldData/ServerLevelData对象<br>
	 * PrimaryLevelData隶属于主世界，仅一份实例，可修改和访问世界属性<br>
	 * DerivedLevelData每个非主世界各自维护一份实例，只能读取世界属性，修改无效<br>
	 */
	public static final PrimaryLevelData mainData = null;

	static final void initLevels(MinecraftServer server) {
		ObjectManipulator.setStaticObject(ServerLevels.class, "levels", ServerLevels.levels(server));
		ObjectManipulator.setStaticObject(ServerLevels.class, "mainData", server.getWorldData().overworldData());
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

	@SuppressWarnings("deprecation")
	public static final void updateTickingLevelsList(MinecraftServer server) {
		server.markWorldsDirty();
	}

	public static final WritableLevelData getLevelData(Level level) {
		return (WritableLevelData) level.getLevelData();
	}

	public static final WritableLevelData setLevelData(Level level, WritableLevelData data) {
		WritableLevelData orig = getLevelData(level);
		ObjectManipulator.setObject(level, "levelData", data);
		return orig;
	}

	/**
	 * 获取ServerLevelData对象，结果为PrimaryLevelData（主世界）或DerivedLevelData（非主世界）
	 * 
	 * @param level
	 * @return
	 */
	public static final ServerLevelData getServerLevelData(ServerLevel level) {
		return (ServerLevelData) getLevelData(level);
	}

	public static final ServerLevelData setServerLevelData(ServerLevel level, ServerLevelData data) {
		ServerLevelData orig = (ServerLevelData) setLevelData(level, data);
		ObjectManipulator.setDeclaredMemberObject(level, "serverLevelData", data);
		return orig;
	}

	/**
	 * 设置目标服务器的维度
	 * 
	 * @param server
	 * @param levels
	 */
	public static final void setLevels(MinecraftServer server, Map<ResourceKey<Level>, ServerLevel> levels) {
		ObjectManipulator.setStaticObject(MinecraftServer.class, "levels", levels);
		updateTickingLevelsList(server);
	}

	public static final void setLevels(Map<ResourceKey<Level>, ServerLevel> levels) {
		setLevels(ServerInstance.server, levels);
	}

	public static final Map<ResourceKey<Level>, ServerLevel> levels = null;

	/**
	 * 移除世界并且不记录
	 * 
	 * @param level_keys
	 */
	public static final void removeLevels(MinecraftServer server, String... level_keys) {
		for (String level_key : level_keys) {
			levels.remove(ExtDimension.Stem.levelKey(level_key));
		}
		updateTickingLevelsList(server);
	}

	public static final void removeLevels(String... level_keys) {
		removeLevels(ServerInstance.server, level_keys);
	}

	/**
	 * 移除世界并且不记录
	 * 
	 * @param level_keys
	 */
	@SuppressWarnings("unchecked")
	public static final void removeLevels(MinecraftServer server, ResourceKey<Level>... level_keys) {
		for (ResourceKey<Level> level_key : level_keys) {
			levels.remove(level_key);
		}
		updateTickingLevelsList(server);
	}

	@SuppressWarnings("unchecked")
	public static final void removeLevels(ResourceKey<Level>... level_keys) {
		removeLevels(ServerInstance.server, level_keys);
	}

	/**
	 * 暂时移除的世界
	 */
	private static final Map<ResourceKey<Level>, ServerLevel> disabled_levels = new HashMap<>();

	public static final void disableLevels(MinecraftServer server, String... level_keys) {
		for (String level_key : level_keys) {
			ResourceKey<Level> res_key = ExtDimension.Stem.levelKey(level_key);
			ServerLevel level = levels.remove(res_key);
			if (level != null)
				disabled_levels.put(res_key, level);
		}
		updateTickingLevelsList(server);
	}

	public static final void disableLevels(String... level_keys) {
		disableLevels(ServerInstance.server, level_keys);
	}

	/**
	 * 禁用世界，暂时移除
	 * 
	 * @param level_keys
	 */
	@SuppressWarnings("unchecked")
	public static final void disableLevels(MinecraftServer server, ResourceKey<Level>... level_keys) {
		for (ResourceKey<Level> level_key : level_keys) {
			ServerLevel level = levels.remove(level_key);
			if (level != null)
				disabled_levels.put(level_key, level);
		}
		updateTickingLevelsList(server);
	}

	@SuppressWarnings("unchecked")
	public static final void disableLevels(ResourceKey<Level>... level_keys) {
		disableLevels(ServerInstance.server, level_keys);
	}

	public static final void disableAllLevels(MinecraftServer server) {
		Iterator<Entry<ResourceKey<Level>, ServerLevel>> iter = levels.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<ResourceKey<Level>, ServerLevel> entry = iter.next();
			disabled_levels.put(entry.getKey(), entry.getValue());
			iter.remove();
		}
		updateTickingLevelsList(server);
	}

	public static final void disableAllLevels() {
		disableAllLevels(ServerInstance.server);
	}

	public static final void enableLevels(MinecraftServer server, String... level_keys) {
		for (String level_key : level_keys) {
			ResourceKey<Level> res_key = ExtDimension.Stem.levelKey(level_key);
			ServerLevel level = disabled_levels.remove(res_key);
			if (level != null)
				levels.put(res_key, level);
		}
		updateTickingLevelsList(server);
	}

	public static final void enableLevels(String... level_keys) {
		enableLevels(ServerInstance.server, level_keys);
	}

	/**
	 * 启用世界，恢复到世界列表
	 * 
	 * @param level_keys
	 */
	@SuppressWarnings("unchecked")
	public static final void enableLevels(MinecraftServer server, ResourceKey<Level>... level_keys) {
		for (ResourceKey<Level> level_key : level_keys) {
			ServerLevel level = disabled_levels.remove(level_key);
			if (level != null)
				levels.put(level_key, level);
		}
		updateTickingLevelsList(server);
	}

	@SuppressWarnings("unchecked")
	public static final void enableLevels(ResourceKey<Level>... level_keys) {
		enableLevels(ServerInstance.server, level_keys);
	}

	public static final void enableAllLevels(MinecraftServer server) {
		Iterator<Entry<ResourceKey<Level>, ServerLevel>> iter = disabled_levels.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<ResourceKey<Level>, ServerLevel> entry = iter.next();
			levels.put(entry.getKey(), entry.getValue());
			iter.remove();
		}
		updateTickingLevelsList(server);
	}

	public static final void enableAllLevels() {
		enableAllLevels(ServerInstance.server);
	}
}
