package ba.entity;

import ba.equipment.armor.DefenseType;
import ba.equipment.weapon.AttackType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;


public class BaseEnemyEntity extends Monster{
	
	AttackType attackType;
	DefenseType defenseType;

	protected BaseEnemyEntity(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level);
		// TODO Auto-generated constructor stub
	}
}
