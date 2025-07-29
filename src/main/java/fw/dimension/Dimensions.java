package fw.dimension;

import fw.core.Core;
import fw.core.ServerInstance;
import fw.core.registry.MutableMappedRegistry;
import fw.core.registry.registries.server.DynamicRegistries;
import fw.datagen.DatagenHolder;
import fw.event.ServerLifecycleTrigger;
import fw.resources.ResourceKeyBuilder;
import lyra.alpha.reference.FieldReference;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
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
		Core.addPostinit(Dimensions::modifyVanillaDimensions);// 修改原版维度
	}

	private static ResourceKey<DimensionType> overworldDimensionTypeKey = null;
	private static ResourceKey<LevelStem> overworldLevelStemKey = null;

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
			overworldLevel.redirectTo(ExtDimension.Stem.levelKey(overworldLevelStemKey));
			overworldLevelStem.redirectTo(overworldLevelStemKey);
			shouldRedirect = true;
		}
		if (shouldRedirect) {
			// 服务器关闭后需要将主世界相关注册表复原，否则会抛出错误。
			ServerInstance.delegateRecoverableRedirectors(
					ServerLifecycleTrigger.AFTER_SERVER_LOAD_LEVEL,
					ServerLifecycleTrigger.BEFORE_SERVER_STOP, // 原版主世界必须保存
					mutableDimensionTypeRegistry,
					mutableDimensionRegistry,
					mutableLevelStemRegistry,
					overworldDimensionType,
					overworldLevel,
					overworldLevelStem);
			mutableDimensionTypeRegistry.unregister(BuiltinDimensionTypes.OVERWORLD);
			mutableDimensionRegistry.unregister(Level.OVERWORLD);
			mutableLevelStemRegistry.unregister(LevelStem.OVERWORLD);
			ServerInstance.disableLevels(vanillaOverworldKey);// 暂时禁用主世界
			ServerLifecycleTrigger.BEFORE_SERVER_STOP.addTempCallback(EventPriority.LOWEST, (MinecraftServer s) -> {
				ServerInstance.enableLevels(vanillaOverworldKey);// 在存档前恢复主世界
			});
		}
	}

	public static final void redirectOverworld(ResourceKey<DimensionType> dimensionType, ResourceKey<LevelStem> levelStem) {
		overworldDimensionTypeKey = dimensionType;
		overworldLevelStemKey = levelStem;
	}

	public static final void redirectOverworld(DatagenHolder<DimensionType> dimensionType, DatagenHolder<LevelStem> levelStem) {
		redirectOverworld(dimensionType.resourceKey, levelStem.resourceKey);
	}

	public static final void redirectOverworld(String dimensionType, String levelStem) {
		redirectOverworld(ResourceKeyBuilder.build(Registries.DIMENSION_TYPE, dimensionType), ResourceKeyBuilder.build(Registries.LEVEL_STEM, levelStem));
	}

	public static final void redirectOverworld(String overworldDimensionId) {
		redirectOverworld(overworldDimensionId, overworldDimensionId);
	}

	/**
	 * 将主世界重定向到指定的不带modid的维度。<br>
	 * modid为Core.ModId。
	 * 
	 * @param modDimensionId
	 */
	public static final void redirectOverworldMod(String modDimensionId) {
		redirectOverworld(Core.modNamespacedId(modDimensionId));
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
					ServerLifecycleTrigger.BEFORE_SERVER_START,
					ServerLifecycleTrigger.AFTER_SERVER_STOP,
					netherDimensionType,
					netherLevel,
					netherLevelStem);
			ServerInstance.disableLevels(vanillaNetherLevel);// 暂时禁用地狱
			ServerLifecycleTrigger.AFTER_SERVER_STOP.addTempCallback(EventPriority.LOWEST, (MinecraftServer s) -> {
				ServerInstance.enableLevels(vanillaNetherLevel);// 在存档后恢复地狱
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
					ServerLifecycleTrigger.BEFORE_SERVER_START,
					ServerLifecycleTrigger.AFTER_SERVER_STOP,
					endDimensionType,
					endLevel,
					endLevelStem);
			ServerInstance.disableLevels(vanillaEndLevel);// 暂时禁用末地
			ServerLifecycleTrigger.AFTER_SERVER_STOP.addTempCallback(EventPriority.LOWEST, (MinecraftServer s) -> {
				ServerInstance.enableLevels(vanillaEndLevel);// 在存档后恢复末地
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

	public static final void modifyVanillaDimensions() {
		ServerInstance.addBeforeServerStartCallback(Dimensions::collectMutableMappedRegistry);
		ServerInstance.addAfterServerLoadLevelCallback(Dimensions::redirectTheNether);
		ServerInstance.addAfterServerLoadLevelCallback(Dimensions::redirectTheEnd);
		ServerInstance.addAfterServerLoadLevelCallback(Dimensions::redirectOverworld);
	}
}
