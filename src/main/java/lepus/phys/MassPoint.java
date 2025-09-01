package lepus.phys;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

/**
 * 具有物理属性的质点
 */
public class MassPoint {
	/**
	 * 静质量
	 */
	private final PhysScalar.TimeIndependent static_mass;

	/**
	 * 位移
	 */
	private final PhysVector displacement;

	private final Vector3f currentDisplacement;

	private final PhysVector velocity;

	/**
	 * 动量
	 */
	private final PhysVector momentum;

	private final Vector3f currentMomentum;

	private final PhysVector resultantForce;

	private final Vector3f currentResultantForce;

	private final ArrayList<PhysVector> forces = new ArrayList<>();

	public MassPoint(float mass, Vector3f initialPosition, Vector3f initialMomentum, float dt) {
		this.static_mass = new PhysScalar.TimeIndependent(mass);
		this.currentResultantForce = new Vector3f();
		this.resultantForce = PhysVector.resultant(forces, currentResultantForce);
		this.currentMomentum = initialMomentum;
		this.momentum = PhysVectors.momentumFromForce(initialMomentum, resultantForce, dt);
		this.velocity = PhysVectors.velocityFromMomentum(momentum, static_mass);
		this.currentDisplacement = initialPosition;
		this.displacement = PhysVectors.displacementFromVelocity(initialPosition, velocity, dt);
	}

	public MassPoint(float mass, Vector3f initialPosition, float dt) {
		this(mass, initialPosition, new Vector3f(), dt);
	}

	public MassPoint deltaStaticMass(float dm) {
		this.static_mass.value += dm;
		return this;
	}

	public float staticMass() {
		return static_mass.value;
	}

	public MassPoint addForce(PhysVector... Fs) {
		this.forces.addAll(List.of(Fs));
		return this;
	}

	public PhysVector momentum() {
		return momentum;
	}

	public Vector3f momentum(float t) {
		return momentum.value(t);
	}

	/**
	 * 当前动量
	 * 
	 * @return
	 */
	public Vector3f currentMomentum() {
		return currentMomentum;
	}

	public MassPoint setMomentum(float x, float y, float z) {
		currentMomentum.set(x, y, z);
		return this;
	}

	/**
	 * 添加冲量
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public MassPoint impulse(float x, float y, float z) {
		currentMomentum.add(x, y, z);
		return this;
	}

	public MassPoint impulse(Vector3f I) {
		currentMomentum.add(I);
		return this;
	}

	/**
	 * 速度表达式
	 * 
	 * @return
	 */
	public PhysVector velocity() {
		return velocity;
	}

	public Vector3f velocity(float t) {
		return velocity.value(t);
	}

	public PhysVector displacement() {
		return displacement;
	}

	public Vector3f displacement(float t) {
		return displacement.value(t);
	}

	public MassPoint setDisplacement(float x, float y, float z) {
		currentDisplacement.set(x, y, z);
		return this;
	}

	/**
	 * 合力
	 * 
	 * @return
	 */
	public PhysVector resultantForce() {
		return resultantForce;
	}

	public Vector3f resultantForce(float t) {
		return resultantForce.value(t);
	}
}
