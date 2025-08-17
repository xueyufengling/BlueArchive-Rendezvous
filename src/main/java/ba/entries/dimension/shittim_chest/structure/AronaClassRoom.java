package ba.entries.dimension.shittim_chest.structure;

import com.mojang.serialization.MapCodec;

import ba.entries.biome.shittim_chest.ShittimChestBiomes;
import fw.codec.annotation.CodecAutogen;
import fw.codec.annotation.CodecTarget;
import fw.codec.derived.DerivedCodecHolder;
import fw.datagen.EntryHolder;
import fw.datagen.annotation.RegistryEntry;
import fw.terrain.HeightProviders;
import fw.terrain.structure.ExtStructure;
import fw.terrain.structure.template.ExtStructureProcessor;
import fw.terrain.structure.template.JigsawPlacementContext;
import fw.terrain.structure.template.TemplatePool;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.neoforged.neoforge.registries.DeferredHolder;

public class AronaClassRoom extends ExtStructure {

	static {
		DerivedCodecHolder.CODEC();
		RegistryEntry.RegistriesProvider.forDatagen();
	}

	@CodecAutogen(null_if_empty = false, register = true)
	public static final MapCodec<AronaClassRoom> CODEC = null;

	@CodecTarget
	public AronaClassRoom(String name,
			Structure.StructureSettings settings,
			JigsawPlacementContext jigsaw_placement_settings) {
		super(name, settings, jigsaw_placement_settings);
	}

	public static final String ID = "ba:arona_class_room";

	@RegistryEntry
	public static final EntryHolder<StructureProcessorList> ARONA_CLASS_ROOM_PROCESSOR_LIST = ExtStructureProcessor.registerList(ID, (BootstrapContext<StructureProcessorList> context, RegistryAccess registryAccess) -> {
		return ExtStructureProcessor.listOf(
				ExtStructureProcessor.JigsawReplacementProcessor());
	});

	@RegistryEntry
	public static final EntryHolder<StructureTemplatePool> ARONA_CLASS_ROOM_TEMPLATE_POOL = TemplatePool.register(ID, (BootstrapContext<StructureTemplatePool> context, RegistryAccess registryAccess) -> {
		TemplatePool pool = new TemplatePool(context);
		pool.singleEntry(ID, ID, StructureTemplatePool.Projection.TERRAIN_MATCHING);
		return pool.build();
	});

	@RegistryEntry
	public static final EntryHolder<Structure> ARONA_CLASS_ROOM = ExtStructure.register(ID, (BootstrapContext<Structure> context, RegistryAccess registryAccess) -> {
		return new AronaClassRoom(context);
	});

	public static final DeferredHolder<StructureType<?>, StructureType<?>> ARONA_CLASS_ROOM_TYPE = ExtStructure.Type.register(ID, () -> CODEC);

	public AronaClassRoom(BootstrapContext<Structure> context) {
		super(Settings.of(
				Settings.validBiomes(context, ShittimChestBiomes.SHITTIM_CHEST_ID),
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
