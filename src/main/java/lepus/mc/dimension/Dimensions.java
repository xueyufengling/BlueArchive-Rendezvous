package lepus.mc.dimension;

import lepus.mc.core.ModInit;
import lepus.mc.core.ServerInstance;
import lepus.mc.core.ServerLevels;
import lepus.mc.core.ModInit.Stage;
import lepus.mc.core.registry.MutableMappedRegistry;
import lepus.mc.core.registry.registries.server.DynamicRegistries;
import lepus.mc.datagen.EntryHolder;
import lepus.mc.event.ServerLifecycleTrigger;
import lepus.mc.resources.ResourceKeys;
import lyra.alpha.reference.FieldReference;
import lyra.object.ObjectManipulator;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.storage.DerivedLevelData;
import net.neoforged.bus.api.EventPriority;

public class Dimensions {
	private static MutableMappedRegistry<DimensionType> mutableDimensionTypeRegistry;
	private static MutableMappedRegistry<Level> mutableDimensionRegistry;
	private static MutableMappedRegistry<LevelStem> mutableLevelStemRegistry;

	private static FieldReference overworldDimensionType;
	private static FieldReference overworldLevel;
	private static FieldReference overworldLevelStem;
	private static final String vanillaOverworldKey = BuiltinDimensionTypes.OVERWORLD.location().toString();

	private static FieldReference netherDimensionType;
	private static FieldReference netherLevel;
	private static FieldReference netherLevelStem;
	private static final ResourceKey<DimensionType> vanillaNetherDimensionType = BuiltinDimensionTypes.NETHER;
	private static final ResourceKey<Level> vanillaNetherLevel = Level.NETHER;
	private static final ResourceKey<LevelStem> vanillaNetherLevelStem = LevelStem.NETHER;

	private static FieldReference endDimensionType;
	private static FieldReference endLevel;
	private static FieldReference endLevelStem;
	private static final ResourceKey<DimensionType> vanillaEndDimensionType = BuiltinDimensionTypes.END;
	private static final ResourceKey<Level> vanillaEndLevel = Level.END;
	private static final ResourceKey<LevelStem> vanillaEndLevelStem = LevelStem.END;

	static {
		overworldDimensionType = FieldReference.of(BuiltinDimensionTypes.class, "OVERWORLD");
		overworldLevel = FieldReference.of(Level.class, "OVERWORLD");
		overworldLevelStem = FieldReference.of(LevelStem.class, "OVERWORLD");
		netherDimensionType = FieldReference.of(BuiltinDimensionTypes.class, "NETHER");
		netherLevel = FieldReference.of(Level.class, "NETHER");
		netherLevelStem = FieldReference.of(LevelStem.class, "NETHER");
		endDimensionType = FieldReference.of(BuiltinDimensionTypes.class, "END");
		endLevel = FieldReference.of(Level.class, "END");
		endLevelStem = FieldReference.of(LevelStem.class, "END");
		ModInit.Initializer.forInit();
	}

	private static ResourceKey<DimensionType> overworldDimensionTypeKey = null;
	private static ResourceKey<LevelStem> overworldLevelStemKey = null;
	private static ResourceKey<Level> overworldLevelKey = null;

	/**
	 * 注册表检查时机：<br>
	 * 当没有世界时，则自动进入创建新世界界面，在创建新世界时将检查原版主世界Level.OVERWORLD等的注册表。此时该字段必须是minecraft:overworld，否则报错无法解析vanilla数据包世界预设presets;<br>
	 * 当创建新世界开始时，先创建服务器，再从Level.OVERWORLD等指定的注册条目读取LevelStem和DimensionType，此时可在beforeServerStart时修改主世界的key，创建的新世界直接就是重定向后的世界；<br>
	 * 当有世界时，在世界选择界面不作检查；<br>
	 * 当进入已经已经存在的存档时，将先检查存档主世界Level.OVERWORLD等的注册表。此时该字段必须是该存档主世界的实际维度，否则报错。检查完之后才会创建服务器。<br>
	 * 
	 * @param server
	 */
	private static void redirectOverworld(MinecraftServer server) {
		boolean shouldRedirect = false;
		if (overworldDimensionTypeKey != null) {
			overworldDimensionType.redirectTo(overworldDimensionTypeKey);
			shouldRedirect = true;
		}
		if (overworldLevelStemKey != null) {
			overworldLevelKey = ExtDimension.Stem.levelKey(overworldLevelStemKey);
			overworldLevel.redirectTo(overworldLevelKey);
			overworldLevelStem.redirectTo(overworldLevelStemKey);
			shouldRedirect = true;
		}
		if (shouldRedirect) {
			// 服务器关闭后需要将主世界相关注册表复原，否则会抛出错误。
			ServerInstance.delegateRecoverableRedirectors(
					ServerLifecycleTrigger.AFTER_SERVER_LOAD_LEVEL, EventPriority.LOWEST,
					ServerLifecycleTrigger.BEFORE_SERVER_STOP, EventPriority.LOWEST, // 原版主世界必须保存
					mutableDimensionTypeRegistry,
					mutableDimensionRegistry,
					mutableLevelStemRegistry,
					overworldDimensionType,
					overworldLevel,
					overworldLevelStem);
			mutableDimensionTypeRegistry.unregister(BuiltinDimensionTypes.OVERWORLD);
			mutableDimensionRegistry.unregister(Level.OVERWORLD);
			mutableLevelStemRegistry.unregister(LevelStem.OVERWORLD);
			ServerLevels.disableLevels(vanillaOverworldKey);// 暂时禁用主世界
			ServerLifecycleTrigger.AFTER_SERVER_LOAD_LEVEL.addTempCallback(EventPriority.LOWEST, (MinecraftServer s) -> {
				redirectServerLevelDataMain();// 设置该维度的WorldData为主世界的，否则tick时无法修改世界的属性，例如增加时间设置天气等
				tickTime(true);// 开启tick时间（默认只有原版主世界开启）
			});// 替换主世界后，需要将新的主世界设置为开启tick时间（除了原版主世界，其他世界tickTime默认均为false）
			ServerLifecycleTrigger.BEFORE_SERVER_STOP.addTempCallback(EventPriority.LOWEST, (MinecraftServer s) -> {
				tickTime(false);
				recoveryServerLevelDataMain();
				ServerLevels.enableLevels(vanillaOverworldKey);// 在存档前恢复主世界
			});
		}
	}

	public static final void redirectOverworld(ResourceKey<DimensionType> dimensionType, ResourceKey<LevelStem> levelStem) {
		overworldDimensionTypeKey = dimensionType;
		overworldLevelStemKey = levelStem;
	}

	public static final void redirectOverworld(EntryHolder<DimensionType> dimensionType, EntryHolder<LevelStem> levelStem) {
		redirectOverworld(dimensionType.getKey(), levelStem.getKey());
	}

	public static final void redirectOverworld(String dimensionType, String levelStem) {
		redirectOverworld(ResourceKeys.build(Registries.DIMENSION_TYPE, dimensionType), ResourceKeys.build(Registries.LEVEL_STEM, levelStem));
	}

	/**
	 * 将主世界重定向到指定的的维度。<br>
	 * 必须在@ModInit注解的方法里调用
	 * 
	 * @param dimensionId 带命名空间的维度id
	 */
	public static final void redirectOverworld(String overworldDimensionId) {
		redirectOverworld(overworldDimensionId, overworldDimensionId);
	}

	/**
	 * 设置世界是否有时间流动
	 * 
	 * @param level
	 * @param tick
	 */
	public static final void tickTime(ServerLevel level, boolean tick) {
		ObjectManipulator.setDeclaredMemberBoolean(level, "tickTime", tick);
	}

	/**
	 * 设置所有世界是否有时间流动，只需要设置主世界即可
	 * 
	 * @param tick
	 */
	public static final void tickTime(boolean tick) {
		tickTime(ServerLevels.levels.get(overworldLevelKey), tick);
	}

	/**
	 * 非主世界的LevelData均为DerivedLevelData
	 */
	private static DerivedLevelData targetOverworldOrigData;

	/**
	 * 将目标Level的WorldData数据改为主世界的可修改实例<br>
	 * 重定向主世界后必须调用，否则重定向后的主世界时间不会流动，且一切更改世界属性的操作都不会被执行
	 */
	private static final void redirectServerLevelDataMain() {
		ServerLevel targetOverworld = ServerLevels.levels.get(overworldLevelKey);
		if (targetOverworld == null)
			throw new IllegalArgumentException("Target overworld level to be redirected to " + overworldLevelKey + " is not found.");
		if (ServerLevels.setServerLevelData(targetOverworld, ServerLevels.mainData) instanceof DerivedLevelData derivedData)
			targetOverworldOrigData = derivedData;
	}

	private static final void recoveryServerLevelDataMain() {
		ServerLevel targetOverworld = ServerLevels.levels.get(overworldLevelKey);
		ServerLevels.setServerLevelData(targetOverworld, targetOverworldOrigData);
		targetOverworldOrigData = null;
	}

	private static final void collectMutableMappedRegistry(MinecraftServer server) {
		mutableDimensionTypeRegistry = MutableMappedRegistry.from(DynamicRegistries.DIMENSION_TYPE);
		mutableDimensionRegistry = MutableMappedRegistry.from(DynamicRegistries.DIMENSION);
		mutableLevelStemRegistry = MutableMappedRegistry.from(DynamicRegistries.LEVEL_STEM);
	}

	private static ResourceKey<DimensionType> netherDimensionTypeKey = vanillaNetherDimensionType;
	private static ResourceKey<LevelStem> netherLevelStemKey = vanillaNetherLevelStem;

	/**
	 * 下界同其他非主世界维度一样，可以安全地注销注册而无需恢复条目
	 * 
	 * @param server
	 */
	@SuppressWarnings("unchecked")
	private static void redirectTheNether(MinecraftServer server) {
		boolean shouldRedirect = false;
		if (!vanillaNetherDimensionType.equals(netherDimensionTypeKey)) {
			netherDimensionType.redirectTo(netherDimensionTypeKey);
			shouldRedirect = true;
		}
		if (!vanillaNetherLevelStem.equals(netherLevelStemKey)) {
			netherLevel.redirectTo(ExtDimension.Stem.levelKey(netherLevelStemKey));
			netherLevelStem.redirectTo(netherLevelStemKey);
			shouldRedirect = true;
		}
		if (shouldRedirect) {
			netherDimensionType.redirectTo(netherDimensionTypeKey);
			netherLevel.redirectTo(ExtDimension.Stem.levelKey(netherLevelStemKey));
			netherLevelStem.redirectTo(netherLevelStemKey);
			// 删除下界
			mutableDimensionTypeRegistry.unregister(vanillaNetherDimensionType);
			mutableDimensionRegistry.unregister(vanillaNetherLevel);
			mutableLevelStemRegistry.unregister(vanillaNetherLevelStem);
			ServerInstance.delegateRecoverableRedirectors(
					ServerLifecycleTrigger.BEFORE_SERVER_START, EventPriority.HIGHEST,
					ServerLifecycleTrigger.AFTER_SERVER_STOP, EventPriority.LOWEST,
					netherDimensionType,
					netherLevel,
					netherLevelStem);
			ServerLevels.disableLevels(vanillaNetherLevel);// 暂时禁用地狱

			ServerLifecycleTrigger.AFTER_SERVER_STOP.addTempCallback(EventPriority.LOWEST, (MinecraftServer s) -> {
				ServerLevels.enableLevels(vanillaNetherLevel);// 在存档后恢复地狱
			});
		}
	}

	private static ResourceKey<DimensionType> endDimensionTypeKey = vanillaEndDimensionType;
	private static ResourceKey<LevelStem> endLevelStemKey = vanillaEndLevelStem;

	/**
	 * 末地同其他非主世界维度一样，可以安全地注销注册而无需恢复条目
	 * 
	 * @param server
	 */
	@SuppressWarnings("unchecked")
	private static void redirectTheEnd(MinecraftServer server) {
		boolean shouldRedirect = false;
		if (!vanillaEndDimensionType.equals(endDimensionTypeKey)) {
			endDimensionType.redirectTo(endDimensionTypeKey);
			shouldRedirect = true;
		}
		if (!vanillaEndLevelStem.equals(endLevelStemKey)) {
			endLevel.redirectTo(ExtDimension.Stem.levelKey(endLevelStemKey));
			endLevelStem.redirectTo(endLevelStemKey);
			shouldRedirect = true;
		}
		if (shouldRedirect) { // 删除末地
			mutableDimensionTypeRegistry.unregister(vanillaEndDimensionType);
			mutableDimensionRegistry.unregister(vanillaEndLevel);
			mutableLevelStemRegistry.unregister(vanillaEndLevelStem);
			ServerInstance.delegateRecoverableRedirectors(
					ServerLifecycleTrigger.BEFORE_SERVER_START, EventPriority.HIGHEST,
					ServerLifecycleTrigger.AFTER_SERVER_STOP, EventPriority.LOWEST,
					endDimensionType,
					endLevel,
					endLevelStem);
			ServerLevels.disableLevels(vanillaEndLevel);// 暂时禁用末地
			ServerLifecycleTrigger.AFTER_SERVER_STOP.addTempCallback(EventPriority.LOWEST, (MinecraftServer s) -> {
				ServerLevels.enableLevels(vanillaEndLevel);// 在存档后恢复末地
			});
		}
	}

	/**
	 * 是否移除地狱
	 * 
	 * @param rm
	 */
	public static final void removeTheNether(boolean rm) {
		if (rm) {
			netherDimensionTypeKey = null;
			netherLevelStemKey = null;
		}
	}

	/**
	 * 是否移除末地
	 * 
	 * @param rm
	 */
	public static final void removeTheEnd(boolean rm) {
		if (rm) {
			endDimensionTypeKey = null;
			endLevelStemKey = null;
		}
	}

	@ModInit(exec_stage = Stage.POST_INIT)
	public static final void modifyVanillaDimensions() {
		ServerInstance.addBeforeServerStartCallback(Dimensions::collectMutableMappedRegistry);
		ServerInstance.addAfterServerLoadLevelCallback(Dimensions::redirectTheNether);
		ServerInstance.addAfterServerLoadLevelCallback(Dimensions::redirectTheEnd);
		ServerInstance.addAfterServerLoadLevelCallback(Dimensions::redirectOverworld);
	}
}
