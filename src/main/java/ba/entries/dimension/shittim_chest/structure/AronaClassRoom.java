package ba.entries.dimension.shittim_chest.structure;

import java.util.Optional;

import com.mojang.serialization.MapCodec;

import fw.codec.CodecHolder;
import fw.codec.annotation.CodecAutogen;
import fw.codec.annotation.CodecTarget;
import fw.datagen.DatagenHolder;
import fw.datagen.annotation.RegistryDatagen;
import fw.terrain.HeightProviders;
import fw.terrain.structure.ExtStructure;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pools.DimensionPadding;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasLookup;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;

public class AronaClassRoom extends ExtStructure {

	static {
		CodecHolder.CODEC();
		RegistryDatagen.RegistriesProvider.forDatagen();
	}

	@CodecAutogen(null_if_empty = false)
	public static final MapCodec<AronaClassRoom> CODEC = null;

	@CodecTarget
	public AronaClassRoom(String name,
			Structure.StructureSettings settings,
			Holder<StructureTemplatePool> template_pool,
			Optional<ResourceLocation> start_jigsaw_name,
			int max_depth,
			HeightProvider start_height,
			Optional<Heightmap.Types> project_start_to_heightmap,
			int max_distance_from_center,
			PoolAliasLookup alias_lookup,
			DimensionPadding dimension_padding,
			LiquidSettings liquid_settings) {
		super(name, settings, template_pool, start_jigsaw_name, max_depth, start_height, project_start_to_heightmap, max_distance_from_center, alias_lookup, dimension_padding, liquid_settings);
	}

	public static final String ID = "arona_class_room";

	//@RegistryDatagen
	public static final DatagenHolder<Structure> ARONA_CLASS_ROOM = ExtStructure.register(ID, (BootstrapContext<?> context, RegistryAccess registryAccess) -> {
		return new AronaClassRoom(context);
	});

	//@RegistryDatagen
	public static final DatagenHolder<StructureType<?>> ARONA_CLASS_ROOM_TYPE = ExtStructure.Type.register(ID, (BootstrapContext<?> context, RegistryAccess registryAccess) -> {
		return ExtStructure.Type.build(CODEC);
	});

	public AronaClassRoom(BootstrapContext<?> context) {
		super(Settings.of(
				Settings.validTagBiomes(context, "ba:shittim_chest"),
				Settings.spawnOverrides(),
				GenerationStep.Decoration.SURFACE_STRUCTURES,
				TerrainAdjustment.NONE),
				ID,
				context,
				ID,
				ID,
				0,
				HeightProviders.ofConstantHeight(VerticalAnchor.aboveBottom(10)),
				Heightmap.Types.WORLD_SURFACE,
				10,
				0, 1,
				LiquidSettings.IGNORE_WATERLOGGING);
	}

}
