package ba.equipment.weapon;

/**
 * 克制伤害效果，包括抵抗、普通、特效、弱点
 */
public enum AttackDamageEffect {
	Resist(0.5f),
	Normal(1.0f),
	Effective(1.5f),
	Weak(2.0f);

	public final float damageDealt;

	private AttackDamageEffect(float ratio) {
		damageDealt = ratio;
	}
}
