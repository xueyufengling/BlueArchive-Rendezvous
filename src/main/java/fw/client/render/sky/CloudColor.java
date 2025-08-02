package fw.client.render.sky;

import java.util.ArrayList;
import java.util.HashMap;

import fw.client.render.TimeBasedColorLinearInterpolation;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.Vec3;

public class CloudColor {
	@FunctionalInterface
	public static interface Resolver {
		public Vec3 resolve(Vec3 orig, ClientLevel level, float partialTick, Holder<Biome> biome, long time);
	}

	private static final ArrayList<Resolver> colorResolvers = new ArrayList<>();

	/**
	 * colorResolvers中的决议器将依次应用，如果决议器返回null则执行下一个决议器，决议出的第一个非null的Vec3值将作为最终颜色返回
	 * 
	 * @param orig
	 * @param level
	 * @param partialTick
	 * @param biome
	 * @param time
	 * @return
	 */
	public static final Vec3 resolve(Vec3 orig, ClientLevel level, float partialTick, Holder<Biome> biome, long time) {
		for (Resolver resolver : colorResolvers) {
			if (resolver != null) {
				Vec3 result = resolver.resolve(orig, level, partialTick, biome, time);
				if (result != null)
					return result;
			}
		}
		return orig;
	}

	public static final void addCloudColorResolver(Resolver resolver) {
		colorResolvers.add(resolver);
	}

	public static final void addCloudColorResolver(TimeBasedColorLinearInterpolation cloudColor) {
		addCloudColorResolver((Vec3 orig, ClientLevel level, float partialTick, Holder<Biome> biome, long time) -> {
			return cloudColor.interploteVec3f(time);
		});
	}

	public static abstract class MappedResolver<K> implements Resolver {
		private final HashMap<K, Resolver> resolversMap = new HashMap<>();

		@Override
		public final Vec3 resolve(Vec3 orig, ClientLevel level, float partialTick, Holder<Biome> biome, long time) {
			Resolver resolver = resolversMap.get(key(orig, level, partialTick, biome, time));
			if (resolver == null)
				return null;
			else
				return resolver.resolve(orig, level, partialTick, biome, time);
		}

		public abstract K key(Vec3 orig, ClientLevel level, float partialTick, Holder<Biome> biome, long time);

		public final MappedResolver<K> addEntry(K key, Resolver resolver) {
			resolversMap.put(key, resolver);
			return this;
		}
	}

	private static final MappedResolver<String> levelBasedResolver = new MappedResolver<String>() {
		@Override
		public String key(Vec3 orig, ClientLevel level, float partialTick, Holder<Biome> biome, long time) {
			return level.dimension().location().toString();
		}
	};

	private static final MappedResolver<String> biomeBasedResolver = new MappedResolver<String>() {
		@Override
		public String key(Vec3 orig, ClientLevel level, float partialTick, Holder<Biome> biome, long time) {
			return biome.getKey().location().toString();
		}
	};

	static {
		addCloudColorResolver(levelBasedResolver);
		addCloudColorResolver(biomeBasedResolver);
	}

	public static final Resolver resolver(TimeBasedColorLinearInterpolation cloudColor) {
		return (Vec3 orig, ClientLevel level, float partialTick, Holder<Biome> biome, long time) -> {
			return cloudColor.interploteVec3f(time);
		};
	}

	public static final void setLevelCloudColorResolver(String levelKey, TimeBasedColorLinearInterpolation cloudColor) {
		levelBasedResolver.addEntry(levelKey, resolver(cloudColor));
	}

	public static final void setBiomeCloudColorResolver(String levelKey, TimeBasedColorLinearInterpolation cloudColor) {
		biomeBasedResolver.addEntry(levelKey, resolver(cloudColor));
	}
}
