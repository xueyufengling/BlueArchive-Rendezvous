package lepus.mc.client.render.level;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import lyra.object.ObjectManipulator;
import net.minecraft.client.color.block.BlockTintCache;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ColorResolver;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * 客户端ClientLevel渲染相关数据缓存
 */
@OnlyIn(Dist.CLIENT)
public class LevelCache {
	/**
	 * 获取ClientLevel缓存的草地、树叶、水的ColorResolver
	 * 
	 * @param level
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static final Object2ObjectArrayMap<ColorResolver, BlockTintCache> getTintCaches(ClientLevel level) {
		return (Object2ObjectArrayMap<ColorResolver, BlockTintCache>) ObjectManipulator.getDeclaredMemberObject(level, "tintCaches");
	}

	/**
	 * 设置
	 * 
	 * @param level
	 * @param cache
	 */
	public static final void setTintCaches(ClientLevel level, Object2ObjectArrayMap<ColorResolver, BlockTintCache> cache) {
		ObjectManipulator.setDeclaredMemberObject(level, "tintCaches", cache);
	}

	/**
	 * 修改ColorResolver后需要更新缓存
	 * 
	 * @param level
	 * @param grass
	 * @param foliage
	 * @param water
	 */
	public static final void updateColorResolverCache(ClientLevel level, ColorResolver grass, ColorResolver foliage, ColorResolver water) {
		Object2ObjectArrayMap<ColorResolver, BlockTintCache> cache = new Object2ObjectArrayMap<>(3);
		cache.put(grass, new BlockTintCache((BlockPos pos) -> level.calculateBlockTint(pos, grass)));
		cache.put(foliage, new BlockTintCache((BlockPos pos) -> level.calculateBlockTint(pos, foliage)));
		cache.put(water, new BlockTintCache((BlockPos pos) -> level.calculateBlockTint(pos, water)));
		setTintCaches(level, cache);
		ObjectManipulator.setStaticObject(BiomeColors.class, "GRASS_COLOR_RESOLVER", grass);// 该值实际上是索引cache的key，需要连同缓存一起修改
		ObjectManipulator.setStaticObject(BiomeColors.class, "FOLIAGE_COLOR_RESOLVER", foliage);
		ObjectManipulator.setStaticObject(BiomeColors.class, "WATER_COLOR_RESOLVER", water);
	}
}
