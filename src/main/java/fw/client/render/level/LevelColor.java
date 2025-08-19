package fw.client.render.level;

import java.util.ArrayList;
import java.util.HashMap;

import fw.math.interpolation.ColorLinearInterpolation;
import fw.math.interpolation.Vec3LinearInterpolation;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.Vec3;

public class LevelColor {
	@FunctionalInterface
	public static interface Resolver {
		public Vec3 resolve(Vec3 orig, ClientLevel level, float partialTick, Holder<Biome> biome, Vec3 camPos, long time);
	}

	private final ArrayList<Resolver> colorResolvers = new ArrayList<>();

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
	public final Vec3 resolve(Vec3 orig, ClientLevel level, float partialTick, Holder<Biome> biome, Vec3 camPos, long time) {
		for (Resolver resolver : colorResolvers) {
			if (resolver != null) {
				Vec3 result = resolver.resolve(orig, level, partialTick, biome, camPos, time);
				if (result != null)
					return result;
			}
		}
		return orig;
	}

	public final void addColorResolver(Resolver resolver) {
		colorResolvers.add(resolver);
	}

	public final void removeColorResolver(Resolver resolver) {
		colorResolvers.remove(resolver);
	}

	public final void addColorResolver(ColorLinearInterpolation color) {
		addColorResolver((Vec3 orig, ClientLevel level, float partialTick, Holder<Biome> biome, Vec3 camPos, long time) -> {
			return color.interploteNormalizedVec3(time);
		});
	}

	public static abstract class MappedResolver<K> implements Resolver {
		private final HashMap<K, Resolver> resolversMap = new HashMap<>();

		@Override
		public final Vec3 resolve(Vec3 orig, ClientLevel level, float partialTick, Holder<Biome> biome, Vec3 camPos, long time) {
			Resolver resolver = resolversMap.get(key(orig, level, partialTick, biome, camPos, time));
			if (resolver == null)
				return null;
			else
				return resolver.resolve(orig, level, partialTick, biome, camPos, time);
		}

		public abstract K key(Vec3 orig, ClientLevel level, float partialTick, Holder<Biome> biome, Vec3 camPos, long time);

		public final MappedResolver<K> addEntry(K key, Resolver resolver) {
			resolversMap.put(key, resolver);
			return this;
		}
	}

	public static class LevelBasedResolver extends MappedResolver<String> {
		@Override
		public String key(Vec3 orig, ClientLevel level, float partialTick, Holder<Biome> biome, Vec3 camPos, long time) {
			return level.dimension().location().toString();
		}
	};

	public static class BiomeBasedResolver extends MappedResolver<String> {
		@Override
		public String key(Vec3 orig, ClientLevel level, float partialTick, Holder<Biome> biome, Vec3 camPos, long time) {
			return biome.getKey().location().toString();
		}
	};

	public static final Resolver resolver(ColorLinearInterpolation color) {
		return (Vec3 orig, ClientLevel level, float partialTick, Holder<Biome> biome, Vec3 camPos, long time) -> {
			return color.interploteNormalizedVec3(time);
		};
	}

	public static final Resolver resolver(ColorLinearInterpolation color, double ratio) {
		return (Vec3 orig, ClientLevel level, float partialTick, Holder<Biome> biome, Vec3 camPos, long time) -> {
			return color.interploteNormalizedVec3(time).scale(ratio).add(orig.scale(1.0 - ratio));
		};
	}

	public static final Resolver resolver(ColorLinearInterpolation color, Vec3LinearInterpolation weight) {
		return (Vec3 orig, ClientLevel level, float partialTick, Holder<Biome> biome, Vec3 camPos, long time) -> {
			Vec3 w = weight.interploteVec3(time);
			return color.interploteNormalizedVec3(time).multiply(w.x, w.y, w.z).add(orig.multiply(1.0 - w.x, 1.0 - w.y, 1.0 - w.z));
		};
	}
}
