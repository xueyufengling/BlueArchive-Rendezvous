package fw.dimension;

import fw.core.Core;
import fw.core.ServerEntry;
import fw.core.registry.DynamicRegistries;
import fw.core.registry.MutableMappedRegistry;
import fw.datagen.DatagenHolder;
import fw.resources.ResourceKeyBuilder;
import lyra.alpha.reference.FieldReference;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;

public class Dimensions {
	private static boolean unregisterNether = true;
	private static boolean unregisterEnd = true;

	private static MutableMappedRegistry<DimensionType> mutableDimensionTypeRegistry;
	private static MutableMappedRegistry<Level> mutableDimensionRegistry;
	private static MutableMappedRegistry<LevelStem> mutableLevelStemRegistry;

	private static void removeTheNetherAndTheEnd() {
		if (unregisterNether) {// 删除下界
			mutableDimensionTypeRegistry.unregister(BuiltinDimensionTypes.NETHER);
			mutableDimensionRegistry.unregister(Level.NETHER);
			mutableLevelStemRegistry.unregister(LevelStem.NETHER);
			FieldReference.of(Level.class, "NETHER").redirect();
			ServerEntry.delegateBeforeServerStartRecoverableRedirectors(
					FieldReference.of(BuiltinDimensionTypes.class, "NETHER"),
					FieldReference.of(LevelStem.class, "NETHER"));
		}
		if (unregisterEnd) { // 删除末地
			mutableDimensionTypeRegistry.unregister(BuiltinDimensionTypes.END);
			mutableDimensionRegistry.unregister(Level.END);
			mutableLevelStemRegistry.unregister(LevelStem.END);
			FieldReference.of(Level.class, "END").redirect();
			ServerEntry.delegateBeforeServerStartRecoverableRedirectors(
					FieldReference.of(BuiltinDimensionTypes.class, "END"),
					FieldReference.of(LevelStem.class, "END"));
		}
	}

	/**
	 * 是否移除地狱
	 * 
	 * @param rm
	 */
	public static final void removeTheNether(boolean rm) {
		unregisterNether = rm;
	}

	/**
	 * 是否移除末地
	 * 
	 * @param rm
	 */
	public static final void removeTheEnd(boolean rm) {
		unregisterEnd = rm;
	}

	private static ResourceKey<DimensionType> overworldDimensionTypeKey = null;
	private static ResourceKey<LevelStem> overworldLevelStemKey = null;

	private static FieldReference overworldDimensionType;
	private static FieldReference overworldLevel;
	private static FieldReference overworldLevelStem;

	static {
		overworldDimensionType = FieldReference.of(BuiltinDimensionTypes.class, "OVERWORLD");
		overworldLevel = FieldReference.of(Level.class, "OVERWORLD");
		overworldLevelStem = FieldReference.of(LevelStem.class, "OVERWORLD");
	}

	private static void redirectOverworldFields() {
		if (overworldDimensionTypeKey != null) {
			overworldDimensionType.redirectTo(overworldDimensionTypeKey).redirect();
		}
		if (overworldLevelStemKey != null) {
			overworldLevel.redirectTo(ExtDimension.Stem.levelKey(overworldLevelStemKey)).redirect();
			overworldLevelStem.redirectTo(overworldLevelStemKey).redirect();
		}
	}

	/**
	 * 注册表检查时机：<br>
	 * 当没有世界时，则自动进入创建新世界界面，在创建新世界时将检查原版主世界Level.OVERWORLD等的注册表。此时该字段必须是minecraft:overworld，否则报错无法解析vanilla数据包世界预设presets;<br>
	 * 当创建新世界开始时，先创建服务器，再从Level.OVERWORLD等指定的注册条目读取LevelStem和DimensionType，此时可在beforeServerStart时修改主世界的key，创建的新世界直接就是重定向后的世界；<br>
	 * 当有世界时，在世界选择界面不作检查；<br>
	 * 当进入已经已经存在的存档时，将先检查原版主世界Level.OVERWORLD等的注册表。此时该字段必须是该存档主世界的实际维度，否则报错。检查完之后才会创建服务器。<br>
	 * 
	 * @param server
	 */
	private static void redirectOverworld(MinecraftServer server) {
		// 服务器关闭后需要将主世界相关注册表复原，否则会抛出错误。
		ServerEntry.delegateAfterServerLoadLevelRecoverableRedirectors(
				mutableDimensionTypeRegistry,
				mutableDimensionRegistry,
				mutableLevelStemRegistry);

		redirectOverworldFields();
		// dimensionType.recovery();
		// level.recovery();
		// levelStem.recovery();
		//
		// mutableDimensionTypeRegistry.unregister(BuiltinDimensionTypes.OVERWORLD);
		// mutableDimensionRegistry.unregister(Level.OVERWORLD);
		// mutableLevelStemRegistry.unregister(LevelStem.OVERWORLD);

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
		redirectOverworld(Core.namespacedId(modDimensionId));
	}

	public static final void unregisterVanillaDimensions(MinecraftServer server) {
		if (mutableDimensionTypeRegistry == null)
			mutableDimensionTypeRegistry = MutableMappedRegistry.from(DynamicRegistries.DIMENSION_TYPE);
		if (mutableDimensionRegistry == null)
			mutableDimensionRegistry = MutableMappedRegistry.from(DynamicRegistries.DIMENSION);
		if (mutableLevelStemRegistry == null)
			mutableLevelStemRegistry = MutableMappedRegistry.from(DynamicRegistries.LEVEL_STEM);
		removeTheNetherAndTheEnd();
		mutableDimensionTypeRegistry.asPrimary();
		mutableDimensionRegistry.asPrimary();
		mutableLevelStemRegistry.asPrimary();
	}

	public static final void modifyVanillaDimensions() {
		ServerEntry.addBeforeServerStartCallback(Dimensions::unregisterVanillaDimensions);
		ServerEntry.addBeforeServerStartCallback(Dimensions::redirectOverworld);
		redirectOverworldFields();
	}
}
