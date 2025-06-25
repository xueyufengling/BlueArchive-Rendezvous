package fw.dimension;

import fw.core.Core;
import fw.core.ServerEntry;
import fw.core.registry.MappedRegistries;
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
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;

@EventBusSubscriber(modid = Core.ModId, bus = Bus.MOD)
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
			ServerEntry.delegateRecoverableRedirectors(
					FieldReference.of(BuiltinDimensionTypes.class, "NETHER"),
					FieldReference.of(LevelStem.class, "NETHER"));
		}
		if (unregisterEnd) { // 删除末地
			mutableDimensionTypeRegistry.unregister(BuiltinDimensionTypes.END);
			mutableDimensionRegistry.unregister(Level.END);
			mutableLevelStemRegistry.unregister(LevelStem.END);
			FieldReference.of(Level.class, "END").redirect();
			ServerEntry.delegateRecoverableRedirectors(
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
		overworldDimensionType.redirectTo(overworldDimensionTypeKey).redirect();
		overworldLevel.redirectTo(ExtDimension.Stem.levelKey(overworldLevelStemKey)).redirect();
		overworldLevelStem.redirectTo(overworldLevelStemKey).redirect();
	}

	private static void redirectOverworld(MinecraftServer server) {
		if (overworldDimensionTypeKey == null || overworldLevelStemKey == null)
			return;

		// 服务器关闭后需要将主世界相关注册表复原，否则会抛出错误。
		ServerEntry.delegateRecoverableRedirectors(
				mutableDimensionTypeRegistry,
				mutableDimensionRegistry,
				mutableLevelStemRegistry,
				overworldDimensionType.redirectTo(overworldDimensionTypeKey),
				overworldLevel.redirectTo(ExtDimension.Stem.levelKey(overworldLevelStemKey)),
				overworldLevelStem.redirectTo(overworldLevelStemKey));
		// dimensionType.recovery();
		// level.recovery();
		// levelStem.recovery();
		// if (BuiltinDimensionTypes.OVERWORLD != redirectedOverworldDimensionType) {
		mutableDimensionTypeRegistry.unregister(BuiltinDimensionTypes.OVERWORLD);
		// }
		// if (LevelStem.OVERWORLD != redirectedOverworldLevelStem) {
		mutableDimensionRegistry.unregister(Level.OVERWORLD);
		mutableLevelStemRegistry.unregister(LevelStem.OVERWORLD);

		System.err.print("redir " + overworldDimensionTypeKey);
	}

	public static final void redirectOverworld(ResourceKey<DimensionType> dimensionType, ResourceKey<LevelStem> levelStem) {
		overworldDimensionTypeKey = dimensionType;
		overworldLevelStemKey = levelStem;
		redirectOverworldFields();
	}

	public static final void redirectOverworld(DatagenHolder<DimensionType> dimensionType, DatagenHolder<LevelStem> levelStem) {
		redirectOverworld(dimensionType.resourceKey, levelStem.resourceKey);
	}

	public static final void redirectOverworld(String dimensionType, String levelStem) {
		redirectOverworld(ResourceKeyBuilder.build(Registries.DIMENSION_TYPE, levelStem), ResourceKeyBuilder.build(Registries.LEVEL_STEM, levelStem));
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
			mutableDimensionTypeRegistry = MutableMappedRegistry.from(MappedRegistries.DIMENSION_TYPE);
		if (mutableDimensionRegistry == null)
			mutableDimensionRegistry = MutableMappedRegistry.from(MappedRegistries.DIMENSION);
		if (mutableLevelStemRegistry == null)
			mutableLevelStemRegistry = MutableMappedRegistry.from(MappedRegistries.LEVEL_STEM);
		removeTheNetherAndTheEnd();
		mutableDimensionTypeRegistry.asPrimary();
		mutableDimensionRegistry.asPrimary();
		mutableLevelStemRegistry.asPrimary();
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	private static final void init(FMLConstructModEvent event) {
		ServerEntry.addBeforeServerStartCallback(Dimensions::unregisterVanillaDimensions);
		ServerEntry.addBeforeServerStartCallback(Dimensions::redirectOverworld);
		redirectOverworldFields();
	}
}
