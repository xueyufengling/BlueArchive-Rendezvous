package ba.equipment.armor;

import ba.equipment.weapon.AttackDamageEffect;
import ba.equipment.weapon.AttackType;

public enum DefenseType {
	/**
	 * 普通装甲
	 */
	NormalArmor(0),

	/**
	 * 轻装甲
	 */
	LightArmor(1),

	/**
	 * 重装甲
	 */
	HeavyArmor(2),

	/**
	 * 特殊装甲
	 */
	SpecialArmor(3),

	/**
	 * 弹力装甲
	 */
	ElasticArmor(4),

	/**
	 * 构造物装甲
	 */
	Structure(5);

	public final int id;

	private DefenseType(int id) {
		this.id = id;
	}

	/**
	 * 根据传入的攻击类型决定克制效果
	 * 
	 * @param atkType
	 * @return
	 */
	public AttackDamageEffect resolveDamageEffect(AttackType atkType) {
		return atkType.resolveDamageEffect(this);
	}

	/**
	 * 根据传入的攻击类型决定克制造成的伤害比例
	 * 
	 * @param atkType
	 * @return
	 */
	public float resolveDamageDealt(AttackType atkType) {
		return this.resolveDamageEffect(atkType).damageDealt;
	}
}
