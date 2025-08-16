package fw.terrain.feature;

import fw.codec.annotation.AsDataField;
import fw.codec.annotation.CodecEntry;
import fw.codec.annotation.CodecTarget;
import fw.core.registry.RegistryMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ExtFeature extends Feature<ExtFeatureConfiguration> {
	@CodecEntry
	ExtFeatureConfiguration configuration;

	@CodecTarget
	public ExtFeature(@AsDataField ExtFeatureConfiguration configuration) {
		super(ExtFeatureConfiguration.CODEC);
		this.configuration = configuration;
	}

	public static final RegistryMap<Feature<?>> FEATURES = RegistryMap.of(Registries.FEATURE);

	public static DeferredHolder<Feature<?>, Feature<?>> register(String name, Feature<? extends FeatureConfiguration> feature) {
		return FEATURES.register(name, () -> feature);
	}

	@Override
	public boolean place(FeaturePlaceContext<ExtFeatureConfiguration> context) {
		// TODO Auto-generated method stub
		return false;
	}
}
