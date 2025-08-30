package lepus.mc.terrain.decoration;

import java.util.Random;

import lepus.mc.resources.ResourceKeys;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.CaveSurface;

/**
 * 封装一些常用的SurfaceRules.ConditionSource判断条件
 */
public class TerrainTest {
	public static SurfaceRules.ConditionSource not(SurfaceRules.ConditionSource test) {
		return SurfaceRules.not(test);
	}

	public static SurfaceRules.ConditionSource aboveY(int y) {
		return SurfaceRules.yBlockCheck(VerticalAnchor.absolute(y), 0);
	}

	public static SurfaceRules.ConditionSource belowY(int y) {
		return not(SurfaceRules.yBlockCheck(VerticalAnchor.absolute(y), 0));
	}

	public static SurfaceRules.ConditionSource aboveBottom(int height, int range) {
		return SurfaceRules.yBlockCheck(VerticalAnchor.aboveBottom(height), range);
	}

	public static SurfaceRules.ConditionSource belowTop(int depth, int range) {
		return SurfaceRules.yBlockCheck(VerticalAnchor.belowTop(depth), range);
	}

	@SuppressWarnings("unchecked")
	public static SurfaceRules.ConditionSource inBiomes(ResourceKey<Biome>... biomes) {
		return SurfaceRules.isBiome(biomes);
	}

	@SuppressWarnings("unchecked")
	public static SurfaceRules.ConditionSource inBiomes(String... biomes) {
		ResourceKey<Biome>[] keys = new ResourceKey[biomes.length];
		for (int idx = 0; idx < biomes.length; ++idx)
			keys[idx] = ResourceKeys.build(Registries.BIOME, biomes[idx]);
		return SurfaceRules.isBiome(keys);
	}

	/**
	 * y>gradientStartY时测试失败，y<gradientCompleteY时测试成功，在这两者之间则随机过渡<br>
	 * gradientStartY应当始终大于gradientCompleteY
	 * 
	 * @param random
	 * @param gradientStartY    渐变开始坐标，在此坐标以上测试均不通过
	 * @param gradientCompleteY 渐变结束y坐标，在此坐标以下均测试通过
	 * @return
	 */
	public static SurfaceRules.ConditionSource belowYGradient(String random, int gradientStartY, int gradientCompleteY) {
		if (gradientStartY < gradientCompleteY)
			throw new IllegalArgumentException("Invalid belowYGradient argument, gradientStartY should be larger than gradientCompleteY. Found gradientStartY=" + gradientStartY + ", gradientCompleteY=" + gradientCompleteY);
		return SurfaceRules.verticalGradient(random, VerticalAnchor.absolute(gradientCompleteY), VerticalAnchor.absolute(gradientStartY));
	}

	public static SurfaceRules.ConditionSource aboveYGradient(String random, int gradientStartY, int gradientCompleteY) {
		if (gradientStartY > gradientCompleteY)
			throw new IllegalArgumentException("Invalid aboveYGradient argument, gradientStartY should be less than gradientCompleteY. Found gradientStartY=" + gradientStartY + ", gradientCompleteY=" + gradientCompleteY);
		return not(SurfaceRules.verticalGradient(random, VerticalAnchor.absolute(gradientStartY), VerticalAnchor.absolute(gradientCompleteY)));
	}

	private static final Random RANDOM = new Random(0L);

	public static SurfaceRules.ConditionSource belowYGradient(int gradientStartY, int gradientCompleteY) {
		return belowYGradient(String.valueOf(RANDOM.nextLong()), gradientStartY, gradientCompleteY);
	}

	/**
	 * y向上渐变
	 * 
	 * @param gradientStartY
	 * @param gradientCompleteY
	 * @return
	 */
	public static SurfaceRules.ConditionSource aboveYGradient(int gradientStartY, int gradientCompleteY) {
		return aboveYGradient(String.valueOf(RANDOM.nextLong()), gradientStartY, gradientCompleteY);
	}

	public static SurfaceRules.ConditionSource solidSurface(int thickness) {
		return SurfaceRules.stoneDepthCheck(thickness, false, CaveSurface.FLOOR);
	}

	/**
	 * 超出水面的高度，水面为原点，正数表示高于水面，负数表示低于水面
	 * 
	 * @param overSurfaceHeight
	 * @return
	 */
	public static SurfaceRules.ConditionSource liquidSurface(int overSurfaceHeight) {
		return SurfaceRules.waterBlockCheck(-overSurfaceHeight, 0);
	}
}
