package lepus.mc.client.render.level;

import java.util.Optional;
import java.util.OptionalInt;

import lepus.math.interpolation.ColorLinearInterpolation;
import lepus.mc.core.registry.registries.client.DynamicRegistries;
import lepus.mc.event.LevelTickTrigger;
import lepus.mc.event.LevelTrigger;
import lepus.mc.resources.ResourceKeys;
import lyra.alpha.reference.FieldRecoverable;
import lyra.object.ObjectManipulator;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.AmbientAdditionsSettings;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.EventPriority;

@OnlyIn(Dist.CLIENT)
public class MutableBiomeSpecialEffects implements FieldRecoverable<MutableBiomeSpecialEffects> {
	@FunctionalInterface
	public static interface TickOperation {
		public void operate(Level level, long dayTime, MutableBiomeSpecialEffects effects);
	}

	private BiomeSpecialEffects effects;

	private MutableBiomeSpecialEffects(BiomeSpecialEffects effects) {
		this.effects = effects;
	}

	public static final MutableBiomeSpecialEffects from(BiomeSpecialEffects effects) {
		return effects == null ? null : new MutableBiomeSpecialEffects(effects);
	}

	private MutableBiomeSpecialEffects(String biomeKey) {
		LevelTrigger.CLIENT_LEVEL_LOAD.addCallback(EventPriority.HIGH, (LevelAccessor level) -> {
			this.effects = DynamicRegistries.BIOME.getHolderOrThrow(ResourceKeys.build(Registries.BIOME, biomeKey)).value().getSpecialEffects();
		});
	}

	public static final MutableBiomeSpecialEffects from(String biomeKey) {
		return biomeKey == null ? null : new MutableBiomeSpecialEffects(biomeKey);
	}

	/**
	 * 将该群系效果关联到指定群系上，任何修改都将实时反映在这些群系上<br>
	 * 在每次服务器加载完成后执行
	 * 
	 * @param biomesWithNamespace
	 * @return
	 */
	public final MutableBiomeSpecialEffects bindTo(String... biomesWithNamespace) {
		LevelTrigger.CLIENT_LEVEL_LOAD.addCallback(EventPriority.LOWEST, (LevelAccessor level) -> {
			for (String biomeKey : biomesWithNamespace) {
				Holder.Reference<Biome> holder = DynamicRegistries.BIOME.getHolder(ResourceKeys.build(Registries.BIOME, biomeKey)).orElse(null);
				if (holder != null)
					ObjectManipulator.setDeclaredMemberObject(holder.value(), "specialEffects", effects);
			}
		});
		return this;
	}

	public int getFogColor() {
		return effects.getFogColor();
	}

	public int getWaterColor() {
		return effects.getWaterColor();
	}

	public int getWaterFogColor() {
		return effects.getWaterFogColor();
	}

	public int getSkyColor() {
		return effects.getSkyColor();
	}

	public Optional<Integer> getFoliageColorOverride() {
		return effects.getFoliageColorOverride();
	}

	public Optional<Integer> getGrassColorOverride() {
		return effects.getGrassColorOverride();
	}

	public BiomeSpecialEffects.GrassColorModifier getGrassColorModifier() {
		return effects.getGrassColorModifier();
	}

	public Optional<AmbientParticleSettings> getAmbientParticleSettings() {
		return effects.getAmbientParticleSettings();
	}

	public Optional<Holder<SoundEvent>> getAmbientLoopSoundEvent() {
		return effects.getAmbientLoopSoundEvent();
	}

	public Optional<AmbientMoodSettings> getAmbientMoodSettings() {
		return effects.getAmbientMoodSettings();
	}

	public Optional<AmbientAdditionsSettings> getAmbientAdditionsSettings() {
		return effects.getAmbientAdditionsSettings();
	}

	public Optional<Music> getBackgroundMusic() {
		return effects.getBackgroundMusic();
	}

	public MutableBiomeSpecialEffects fogColor(int fogColor) {
		ObjectManipulator.setDeclaredMemberInt(effects, "fogColor", fogColor);
		return this;
	}

	public MutableBiomeSpecialEffects waterColor(int waterColor) {
		ObjectManipulator.setDeclaredMemberInt(effects, "waterColor", waterColor);
		return this;
	}

	public MutableBiomeSpecialEffects waterFogColor(int waterFogColor) {
		ObjectManipulator.setDeclaredMemberInt(effects, "waterFogColor", waterFogColor);
		return this;
	}

	public MutableBiomeSpecialEffects skyColor(int skyColor) {
		ObjectManipulator.setDeclaredMemberInt(effects, "skyColor", skyColor);
		return this;
	}

	public MutableBiomeSpecialEffects foliageColorOverride(int foliageColorOverride) {
		ObjectManipulator.setDeclaredMemberObject(effects, "foliageColorOverride", OptionalInt.of(foliageColorOverride));
		return this;
	}

	public MutableBiomeSpecialEffects grassColorOverride(int grassColorOverride) {
		ObjectManipulator.setDeclaredMemberObject(effects, "grassColorOverride", OptionalInt.of(grassColorOverride));
		return this;
	}

	public MutableBiomeSpecialEffects grassColorModifier(BiomeSpecialEffects.GrassColorModifier grassColorModifier) {
		ObjectManipulator.setDeclaredMemberObject(effects, "grassColorModifier", grassColorModifier);
		return this;
	}

	public MutableBiomeSpecialEffects ambientParticle(AmbientParticleSettings ambientParticle) {
		ObjectManipulator.setDeclaredMemberObject(effects, "ambientParticle", Optional.of(ambientParticle));
		return this;
	}

	public MutableBiomeSpecialEffects ambientLoopSound(Holder<SoundEvent> ambientLoopSoundEvent) {
		ObjectManipulator.setDeclaredMemberObject(effects, "ambientLoopSoundEvent", Optional.of(ambientLoopSoundEvent));
		return this;
	}

	public MutableBiomeSpecialEffects ambientMoodSound(AmbientMoodSettings ambientMoodSettings) {
		ObjectManipulator.setDeclaredMemberObject(effects, "ambientMoodSettings", Optional.of(ambientMoodSettings));
		return this;
	}

	public MutableBiomeSpecialEffects ambientAdditionsSound(AmbientAdditionsSettings ambientAdditionsSettings) {
		ObjectManipulator.setDeclaredMemberObject(effects, "ambientAdditionsSettings", Optional.of(ambientAdditionsSettings));
		return this;
	}

	public MutableBiomeSpecialEffects backgroundMusic(Music backgroundMusic) {
		ObjectManipulator.setDeclaredMemberObject(effects, "backgroundMusic", Optional.ofNullable(backgroundMusic));
		return this;
	}

	public MutableBiomeSpecialEffects tick(MutableBiomeSpecialEffects.TickOperation op) {
		LevelTickTrigger.PRE_CLIENT_LEVEL_TICK.addCallback((Level level) -> {
			op.operate(level, level.getDayTime(), this);
		});
		return this;
	}

	public MutableBiomeSpecialEffects tick(ColorLinearInterpolation skyColor) {
		return tick((Level level, long dayTime, MutableBiomeSpecialEffects effects) -> {
			int color = skyColor.interplotePacked(dayTime);
			effects.skyColor(color);
			effects.fogColor(color);
			effects.waterFogColor(color);
		});
	}
}
