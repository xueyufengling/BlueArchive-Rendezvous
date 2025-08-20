package fw.client.render.sky;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.mojang.math.Axis;

import fw.client.render.scene.SceneGraphNode;
import fw.mixins.internal.LevelRendererInternal;
import net.minecraft.world.phys.Vec3;

/**
 * 近地物体渲染追踪器<br>
 * 负责为指定物体设置变换矩阵<br>
 * 近地物体指玩家x、z坐标变化时物体会有形状剪切，y坐标变化时，物体在玩家视野中的位置固定不变。
 */
public class NearEarthObject {
	private SceneGraphNode node;

	public static class Pos {
		/**
		 * 物体坐标，位于这个坐标时物体在90°天顶
		 */
		public final float object_x;
		public final float object_z;
		/**
		 * 方块坐标与渲染坐标的比例
		 */
		public final float distance_decay;

		/**
		 * 相对于相机的高度
		 */
		public final float view_height;

		private Pos(float object_x, float object_z, float view_height, float distance_decay) {
			this.object_x = object_x;
			this.object_z = object_z;
			this.view_height = view_height;
			this.distance_decay = distance_decay;
		}

		public static Pos of(float object_x, float object_z, float view_height, float distance_decay) {
			return new Pos(object_x, object_z, view_height, distance_decay);
		}

		public static final float DEFAULT_DISTANCE_DECAY = 100;

		public static Pos of(float object_x, float object_z, float view_height) {
			return of(object_x, object_z, view_height, DEFAULT_DISTANCE_DECAY);
		}
	}

	@FunctionalInterface
	public static interface Spin {
		/**
		 * 实时计算自旋角速度
		 * 
		 * @param time tick为单位
		 * @return
		 */
		public abstract float angularSpeed(float time);

		public static final Spin ZERO = (float time) -> 0;

		/**
		 * 固定自旋角速度
		 * 
		 * @param omega
		 * @return
		 */
		public static Spin fixed(float omega) {
			return (float time) -> omega;
		}
	}

	/**
	 * 轨道
	 */
	@FunctionalInterface
	public static interface Orbit {
		/**
		 * 实时计算轨道
		 * 
		 * @param time tick为单位
		 * @return
		 */
		public abstract Pos position(float time);

		/**
		 * 固定点
		 * 
		 * @param object_x
		 * @param object_z
		 * @param view_height
		 * @return
		 */
		public static Orbit fixed(float object_x, float object_z, float view_height) {
			return (float time) -> Pos.of(object_x, object_z, view_height);
		}

		/**
		 * 固定高度正圆形轨道
		 * 
		 * @param center_x
		 * @param center_z
		 * @param radius
		 * @param view_height
		 * @param angular_speed 角速度
		 * @param initial_phase 初始相位
		 * @return
		 */
		public static Orbit circle(float center_x, float center_z, float radius, float view_height, float angular_speed, float initial_phase) {
			return (float time) -> {
				float phase = initial_phase + angular_speed * time;
				return Pos.of(center_x + (float) Math.sin(phase) * radius, center_z + (float) Math.cos(phase) * radius, view_height);
			};
		}

		/**
		 * 圆心在其他轨道上的圆形轨道
		 * 
		 * @param center_orbit  圆心所处轨道，该轨道只取x、z值
		 * @param radius
		 * @param view_height
		 * @param angular_speed
		 * @param initial_phase
		 * @return
		 */
		public static Orbit circle(Orbit center_orbit, float radius, float view_height, float angular_speed, float initial_phase) {
			return (float time) -> {
				float phase = initial_phase + angular_speed * time;
				Pos center = center_orbit.position(time);
				return Pos.of(center.object_x + (float) Math.sin(phase) * radius, center.object_z + (float) Math.cos(phase) * radius, view_height);
			};
		}

		public static Orbit circle(Orbit center_orbit, float radius, float view_height, float angular_speed) {
			return circle(center_orbit, radius, view_height, angular_speed, 0);
		}

		/**
		 * @param center_x
		 * @param center_z
		 * @param radius
		 * @param view_height
		 * @param angular_speed 单位rad/tick
		 * @return
		 */
		public static Orbit circle(float center_x, float center_z, float radius, float view_height, float angular_speed) {
			return circle(center_x, center_z, radius, view_height, angular_speed, 0);
		}

	}

	private Orbit orbit;

	private Spin spin = Spin.ZERO;

	private NearEarthObject(SceneGraphNode node, Orbit orbit) {
		this.node = node;
		this.orbit = orbit;
	}

	public final NearEarthObject setOrbit(Orbit orbit) {
		this.orbit = orbit;
		return this;
	}

	public final NearEarthObject setSpin(Spin spin) {
		this.spin = spin;
		return this;
	}

	public static final NearEarthObject bind(SceneGraphNode node, Orbit orbit) {
		NearEarthObject track = new NearEarthObject(node, orbit);
		node.setPreRenderOperation((SceneGraphNode n) -> {
			track.updateTransform();
		});
		return track;
	}

	public static final NearEarthObject bind(SceneGraphNode node, float object_x, float object_z, float view_height) {
		return bind(node, Orbit.fixed(object_x, object_z, view_height));
	}

	private void updateTransform(float player_x, float player_z) {
		Matrix4f incline = new Matrix4f();
		incline.rotate(Axis.XP.rotationDegrees(-90.0f));// 绕正x轴旋转90°，让物体位于仰视视角
		incline.rotate(Axis.YN.rotation(spin.angularSpeed(LevelRendererInternal.RenderLevel.LocalVars.time)));
		Pos pos = orbit.position(LevelRendererInternal.RenderLevel.LocalVars.time);// 计算轨道坐标
		incline.translate(new Vector3f((pos.object_x - player_x) / pos.distance_decay, (player_z - pos.object_z) / pos.distance_decay, pos.view_height));
		node.transform = incline;
	}

	private void updateTransform() {
		Vec3 pos = LevelRendererInternal.RenderLevel.LocalVars.camPos;
		updateTransform((float) pos.x, (float) pos.z);
	}
}
