package lepus.mc.client.render.level;

import com.mojang.blaze3d.vertex.MeshData;

import lepus.graphics.ColorRGBA;
import lepus.math.interpolation.ColorLinearInterpolation;
import lepus.mc.client.render.VertexBufferManipulator;
import lepus.mc.common.MappedResolver;
import lepus.mc.common.MultiResolver;
import lepus.mc.mixins.internal.LevelRendererInternal;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.Vec3;

/**
 * 设置维度内颜色
 */
public class LevelColor extends MultiResolver<LevelColor.Resolver, ColorRGBA> {
	@FunctionalInterface
	public static interface Resolver {
		public ColorRGBA color(ColorRGBA orig, ClientLevel level, float partialTick, Holder<Biome> biome, Vec3 camPos, float time);
	}

	@Override
	@SuppressWarnings("unchecked")
	public ColorRGBA execute(Resolver resolver, ColorRGBA orig, Object... args) {
		return resolver.color(orig, (ClientLevel) args[0], (float) args[1], (Holder<Biome>) args[2], (Vec3) args[3], (float) args[4]);
	}

	public final void addColorResolver(ColorLinearInterpolation color) {
		super.addResolver((ColorRGBA orig, ClientLevel level, float partialTick, Holder<Biome> biome, Vec3 camPos, float time) -> {
			return color.interploteColor(time);
		});
	}

	public static abstract class MappedColorResolver<K> extends MappedResolver<K, Resolver, ColorRGBA> implements Resolver {

		public abstract K key(ColorRGBA orig, ClientLevel level, float partialTick, Holder<Biome> biome, Vec3 camPos, float time);

		@Override
		@SuppressWarnings("unchecked")
		public ColorRGBA execute(Resolver resolver, ColorRGBA orig, Object... args) {
			return resolver.color(orig, (ClientLevel) args[0], (float) args[1], (Holder<Biome>) args[2], (Vec3) args[3], (float) args[4]);
		}

		@Override
		@SuppressWarnings("unchecked")
		public K key(ColorRGBA orig, Object... args) {
			return key(orig, (ClientLevel) args[0], (float) args[1], (Holder<Biome>) args[2], (Vec3) args[3], (float) args[4]);
		}

		@Override
		public ColorRGBA color(ColorRGBA orig, ClientLevel level, float partialTick, Holder<Biome> biome, Vec3 camPos, float time) {
			return super.resolve(orig, level, partialTick, biome, camPos, time);
		}
	}

	public static class LevelBasedResolver extends MappedColorResolver<String> {
		@Override
		public String key(ColorRGBA orig, ClientLevel level, float partialTick, Holder<Biome> biome, Vec3 camPos, float time) {
			return level.dimension().location().toString();
		}
	};

	public static class BiomeBasedResolver extends MappedColorResolver<String> {
		@Override
		public String key(ColorRGBA orig, ClientLevel level, float partialTick, Holder<Biome> biome, Vec3 camPos, float time) {
			return biome.getKey().location().toString();
		}
	};

	public static final Resolver lerp(ColorLinearInterpolation color) {
		return (ColorRGBA orig, ClientLevel level, float partialTick, Holder<Biome> biome, Vec3 camPos, float time) -> {
			return color.interploteColor(time);
		};
	}

	public static final Resolver blendLerp(ColorLinearInterpolation color, float ratio) {
		return (ColorRGBA orig, ClientLevel level, float partialTick, Holder<Biome> biome, Vec3 camPos, float time) -> {
			return color.interploteColor(time).mul(ratio).add(orig.mul(1.0f - ratio));
		};
	}

	/**
	 * 插值参数color所占权重，取值为各个通道的权重随时间的变化
	 * 
	 * @param color
	 * @param weight
	 * @return
	 */
	public static final Resolver blendLerp(ColorLinearInterpolation color, ColorLinearInterpolation weight) {
		return (ColorRGBA orig, ClientLevel level, float partialTick, Holder<Biome> biome, Vec3 camPos, float time) -> {
			ColorRGBA w = weight.interploteColor(time);
			ColorRGBA c = color.interploteColor(time);
			return c.mul(w).add(orig.mul(w.inv()));
		};
	}

	public static final Resolver fixed(int r, int g, int b) {
		return (ColorRGBA orig, ClientLevel level, float partialTick, Holder<Biome> biome, Vec3 camPos, float time) -> ColorRGBA.of(r, g, b);
	}

	public static final Resolver fixed(int r, int g, int b, int a) {
		return (ColorRGBA orig, ClientLevel level, float partialTick, Holder<Biome> biome, Vec3 camPos, float time) -> ColorRGBA.of(r, g, b, a);
	}

	/**
	 * 按照原本的灰度值重新渲染为指定的颜色
	 * 
	 * @param r
	 * @param g
	 * @param b
	 * @param a
	 * @return
	 */
	public static final Resolver fixedGrayScale(int r, int g, int b, int a) {
		return (ColorRGBA orig, ClientLevel level, float partialTick, Holder<Biome> biome, Vec3 camPos, float time) -> ColorRGBA.of(r, g, b, a).mul(orig.gray());
	}

	/**
	 * 固定颜色
	 * 
	 * @param r
	 * @param g
	 * @param b
	 * @return
	 */
	public static final Resolver fixed(float r, float g, float b) {
		return (ColorRGBA orig, ClientLevel level, float partialTick, Holder<Biome> biome, Vec3 camPos, float time) -> ColorRGBA.of(r, g, b);
	}

	public static final Resolver fixed(float r, float g, float b, float a) {
		return (ColorRGBA orig, ClientLevel level, float partialTick, Holder<Biome> biome, Vec3 camPos, float time) -> ColorRGBA.of(r, g, b, a);
	}

	public static MeshData modifyVertexColor(MeshData mesh, LevelColor resolver) {
		if (resolver != null) {
			VertexBufferManipulator buffer = VertexBufferManipulator.from(mesh);
			for (int idx = 0; idx < buffer.verticesCount(); ++idx) {// 遍历4个顶点
				ColorRGBA orig_color = ColorRGBA.of(buffer.getColor(idx));
				ColorRGBA new_color = resolver.resolve(orig_color, LevelRendererInternal.RenderLevel.LocalVars.level, LevelRendererInternal.RenderLevel.LocalVars.partialTick, LevelRendererInternal.RenderLevel.LocalVars.camPosBiome, LevelRendererInternal.RenderLevel.LocalVars.camPos, LevelRendererInternal.RenderLevel.LocalVars.dayTime);
				buffer.setColor(idx, new_color.r, new_color.g, new_color.b, new_color.a);
			}
		}
		return mesh;
	}
}
