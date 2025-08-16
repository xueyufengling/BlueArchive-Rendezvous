package fw.terrain.feature;

import java.util.List;
import java.util.stream.Stream;

import com.mojang.serialization.Codec;

import fw.codec.annotation.AsDataField;
import fw.codec.annotation.CodecAutogen;
import fw.codec.annotation.CodecEntry;
import fw.codec.annotation.CodecTarget;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

@AsDataField
public class ExtFeatureConfiguration implements FeatureConfiguration {
	@CodecAutogen(null_if_empty = false)
	public static final Codec<ExtFeatureConfiguration> CODEC = null;

	@CodecEntry
	protected List<ConfiguredFeature<?, ?>> feature_list;

	@CodecTarget
	public ExtFeatureConfiguration(List<ConfiguredFeature<?, ?>> feature_list) {
		this.feature_list = feature_list;
	}

	public Stream<ConfiguredFeature<?, ?>> getFeatures() {
		return feature_list.parallelStream();
	}
}
