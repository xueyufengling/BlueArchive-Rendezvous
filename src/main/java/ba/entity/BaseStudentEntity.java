package ba.entity;

import com.mojang.authlib.GameProfile;

import ba.equipment.armor.DefenseType;
import ba.equipment.weapon.AttackType;
import ba.world.terrain.TerrainAdvantage;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager.ControllerRegistrar;

public class BaseStudentEntity extends Player implements GeoEntity {

	AttackType attackType;
	DefenseType defenseType;
	TerrainAdvantage terrainAdvantage;

	public BaseStudentEntity(Level level, BlockPos pos, float yRot, GameProfile gameProfile) {
		super(level, pos, yRot, gameProfile);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isSpectator() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCreative() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void registerControllers(ControllerRegistrar controllers) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		// TODO Auto-generated method stub
		return null;
	}
}