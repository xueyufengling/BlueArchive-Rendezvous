package ba.world.terrain;

import ba.core.RegistryFactory;
import net.minecraft.resources.ResourceLocation;

public enum TerrainEffect {
	Outstanding("", "SS", 1.3f, 0.75f),
	Excellent("", "S", 1.2f, 0.60f),
	Good("", "A", 1.1f, 0.45f),
	Neutral("", "B", 1.0f, 0.30f),
	Bad("", "C", 0.9f, 0.15f),
	Terrible("", "D", 0.8f, 0.0f);

	public final ResourceLocation icon;
	public final String evaluation;
	public final float damageDealt;
	public final float shieldBlockRate;

	private TerrainEffect(String icon_path, String evaluation, float damageDealt, float shieldBlockRate) {
		this.icon = RegistryFactory.resourceLocation(icon_path);
		this.evaluation = evaluation;
		this.damageDealt = damageDealt;
		this.shieldBlockRate = shieldBlockRate;
	}
}
