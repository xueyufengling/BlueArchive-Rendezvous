package fw.dimension;

import ba.entries.dimension.kivotos.Kivotos;
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

	private static void removeTheNetherAndTheEnd(MinecraftServer server) {
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

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static final void unregisterVanillaDimensions(FMLConstructModEvent event) {
		mutableDimensionTypeRegistry = MutableMappedRegistry.from(MappedRegistries.DIMENSION_TYPE);
		mutableDimensionRegistry = MutableMappedRegistry.from(MappedRegistries.DIMENSION);
		mutableLevelStemRegistry = MutableMappedRegistry.from(MappedRegistries.LEVEL_STEM);
		ServerEntry.addBeforeServerStartCallback(Dimensions::removeTheNetherAndTheEnd);
		ServerEntry.addBeforeServerStartCallback(Dimensions::redirectOverworld);
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

	private static ResourceKey<DimensionType> redirectOverworldDimensionType;
	private static ResourceKey<LevelStem> redirectOverworldLevelStem;

	private static void redirectOverworld(MinecraftServer server) {
		mutableDimensionTypeRegistry.unregister(BuiltinDimensionTypes.OVERWORLD);
		mutableDimensionRegistry.unregister(Level.OVERWORLD);
		mutableLevelStemRegistry.unregister(LevelStem.OVERWORLD);
		FieldReference.of(Level.class, "OVERWORLD", ExtDimension.Stem.levelKey(redirectOverworldLevelStem)).redirect(); // 重定向主世界
		ServerEntry.delegateRecoverableRedirectors(
				FieldReference.of(BuiltinDimensionTypes.class, "OVERWORLD", redirectOverworldDimensionType),
				FieldReference.of(LevelStem.class, "OVERWORLD", redirectOverworldLevelStem));
	}

	public static final void redirectOverworld(ResourceKey<DimensionType> dimensionType, ResourceKey<LevelStem> levelStem) {
		redirectOverworldDimensionType = dimensionType;
		redirectOverworldLevelStem = levelStem;
	}

	public static final void redirectOverworld(DatagenHolder<DimensionType> dimensionType, DatagenHolder<LevelStem> levelStem) {
		redirectOverworld(dimensionType.resourceKey, levelStem.resourceKey);
	}

	public static final void redirectOverworld(String dimensionType, String levelStem) {
		redirectOverworld(ResourceKeyBuilder.build(Registries.DIMENSION_TYPE, levelStem), ResourceKeyBuilder.build(Registries.LEVEL_STEM, levelStem));
	}
}
