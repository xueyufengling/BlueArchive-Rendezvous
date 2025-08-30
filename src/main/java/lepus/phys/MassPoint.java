package lepus.phys;

import org.joml.Vector3f;

/**
 * 具有物理属性的质点
 */
public class MassPoint {
	private float mass;
	private Vector3f position;
	private Vector3f velocity = new Vector3f();
	private Vector3f acceleration = new Vector3f();

	public MassPoint(float mass, Vector3f position) {
		this.mass = mass;
		this.position = position;
	}

	public MassPoint update(float tpf) {
		this.velocity.add(acceleration.mul(tpf));
		this.position.add(velocity.mul(tpf));
		return this;
	}

	public MassPoint deltaMass(float dm) {
		this.mass += dm;
		return this;
	}

	public float mass() {
		return mass;
	}

	public MassPoint addForce(Vector3f F) {
		this.acceleration.add(F.div(mass));
		return this;
	}

	public Vector3f velocity() {
		return velocity;
	}

	public Vector3f acceleration() {
		return acceleration;
	}
}
