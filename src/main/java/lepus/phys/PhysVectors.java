package lepus.phys;

import org.joml.Vector3f;

public class PhysVectors {

	public static PhysScalar dynamicMassFromMomentum(PhysVector p, float velocity) {
		return (float t) -> p.length().value(t) / velocity;
	}

	public static PhysScalar dynamicMassFromMomentum(PhysVector p, Vector3f velocity) {
		return (float t) -> p.length().value(t) / velocity.length();
	}

	public static PhysVector accelerationFromForce(PhysVector F, PhysScalar mass) {
		return (float t) -> F.value(t).div(mass.value(t));
	}

	public static PhysVector forceFromMomentum(PhysVector p, float dt) {
		return p.differential(dt);
	}

	/**
	 * @param initialMomentum 初始动量（上一步的结果动量）
	 * @param F
	 * @param dt
	 * @return
	 */
	public static PhysVector momentumFromForce(Vector3f initialMomentum, PhysVector F, float dt) {
		return F.integral(initialMomentum, dt);
	}

	public static PhysVector velocityFromMomentum(PhysVector p, PhysScalar mass) {
		return (float t) -> p.value(t).div(mass.value(t));
	}

	/**
	 * @param v
	 * @param initialPos 初始位置（上一步的结果位置）
	 * @param dt
	 * @return
	 */
	public static PhysVector displacementFromVelocity(Vector3f initialPos, PhysVector v, float dt) {
		return v.integral(initialPos, dt);
	}

	/**
	 * 空气阻力1/2kv*v
	 * 
	 * @param k
	 * @param v
	 * @return
	 */
	public static PhysVector airResistanceForce(float k, Vector3f v) {
		return (float t) -> v.lengthSquared() == 0.0f ? v : new Vector3f(v).normalize().mul(-0.5f * k * v.dot(v));
	}

	public static PhysVector airResistanceForce(float k, MassPoint m) {
		return airResistanceForce(k / m.staticMass() / m.staticMass(), m.currentMomentum());
	}
}
