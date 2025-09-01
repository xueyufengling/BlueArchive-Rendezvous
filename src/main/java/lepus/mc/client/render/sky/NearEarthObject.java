package lepus.mc.client.render.sky;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.mojang.math.Axis;

import lepus.mc.client.render.SceneGraphNode;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * 近地物体渲染追踪器<br>
 * 负责为指定物体设置变换矩阵<br>
 * 近地物体指玩家x、y、z坐标变化时物体会有形状剪切，一般来讲，y坐标变化时，物体在玩家视野中的位置固定不变（niew_height使用OffsetDecay.FIXED_Y），当然也可以通过设置OffsetDecay使其变化。
 */
@OnlyIn(Dist.CLIENT)
public class NearEarthObject {
	private SceneGraphNode node;

	public static class Pos {
		/**
		 * x、z坐标差衰减，y为物体渲染相对高度<br>
		 * (近地物体坐标-玩家坐标)=offset<br>
		 * 对x、z坐标，渲染坐标=offset/decay；方块坐标到渲染坐标的转换定义在SectionPos.blockToSectionCoord()，实际上就是除以16<br>
		 * 对高度y坐标，渲染坐标=view_height。<br>
		 */
		@FunctionalInterface
		public static interface NumericalCalculation {
			public float calc(float object_x, float dx, float object_y, float dy, float object_z, float dz);

			/**
			 * 固定坐标差衰减
			 * 
			 * @param decay
			 * @return
			 */
			public static NumericalCalculation fixed(float decay) {
				return (float object_x, float dx, float object_y, float dy, float object_z, float dz) -> decay;
			}

		}

		@FunctionalInterface
		public static interface ViewHeight extends NumericalCalculation {
			public float calc(float object_y, float dy);

			public default float calc(float object_x, float dx, float object_y, float dy, float object_z, float dz) {
				return calc(object_y, dy);
			}

			/**
			 * 物体固定在相机上方view_height处
			 */
			public static ViewHeight FIXED_Y_VIEW_HEIGHT = (float object_y, float dy) -> object_y;

			/**
			 * 线性插值衰减函数，
			 * 
			 * @param fromDy    开始插值时物体与相机的坐标高度差
			 * @param fromValue 相机与物体高度差大于等于fromDy时采用的渲染高度差
			 * @param toDy      物体与相机的坐标高度差
			 * @param toValue   相机与物体高度差小于等于toDy时采用的渲染高度差
			 * @return
			 */
			public static ViewHeight lerpDecayTo(float fromDy, float fromValue, float toDy, float toValue) {
				return new ViewHeight() {
					float k = (toValue - fromValue) / (toDy - fromDy);

					@Override
					public float calc(float object_y, float dy) {
						if (dy >= fromDy)
							return fromValue;
						else if (dy <= toDy)
							return toValue;
						else
							return fromValue + k * (dy - fromDy);
					}

					@Override
					public String toString() {
						return "ViewHeightResolver[k=" + k + "]";
					}
				};
			}
		}

		/**
		 * 物体坐标，位于这个坐标时物体在90°天顶
		 */
		public final float object_x;

		/**
		 * 物体高度坐标
		 */
		public final float object_y;
		public final float object_z;
		/**
		 * 方块坐标与渲染坐标的比例
		 */
		public final NumericalCalculation x_offset_decay;
		/**
		 * 物体相对相机的高度
		 */
		public final NumericalCalculation final_view_height;
		public final NumericalCalculation z_offset_decay;

		public static final NumericalCalculation DEFAULT_XZ_DISTANCE_DECAY = NumericalCalculation.fixed(100);

		/**
		 * y坐标变化时，物体在玩家视野中的位置固定不变
		 */
		public static final NumericalCalculation DEFAULT_Y_VIEW_HEIGHT = ViewHeight.FIXED_Y_VIEW_HEIGHT;

		private Pos(float object_x, float object_y, float object_z, NumericalCalculation x_offset_decay, NumericalCalculation final_view_height, NumericalCalculation z_offset_decay) {
			this.object_x = object_x;
			this.object_y = object_y;
			this.object_z = object_z;
			this.x_offset_decay = x_offset_decay;
			this.final_view_height = final_view_height;
			this.z_offset_decay = z_offset_decay;
		}

		public static Pos of(float object_x, float object_y, float object_z, NumericalCalculation x_offset_decay, NumericalCalculation final_view_height, NumericalCalculation z_offset_decay) {
			return new Pos(object_x, object_y, object_z, x_offset_decay, final_view_height, z_offset_decay);
		}

		public static Pos of(float object_x, float object_y, float object_z, NumericalCalculation xz_offset_decay, NumericalCalculation final_view_height) {
			return of(object_x, object_y, object_z, xz_offset_decay, final_view_height, xz_offset_decay);
		}

		public static Pos of(float object_x, float object_y, float object_z, NumericalCalculation xz_offset_decay) {
			return of(object_x, object_y, object_z, xz_offset_decay, DEFAULT_Y_VIEW_HEIGHT);
		}

		public static Pos of(float object_x, float object_y, float object_z, ViewHeight final_view_height) {
			return of(object_x, object_y, object_z, DEFAULT_XZ_DISTANCE_DECAY, final_view_height);
		}

		public static Pos of(float object_x, float object_y, float object_z) {
			return of(object_x, object_y, object_z, DEFAULT_XZ_DISTANCE_DECAY);
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
		 * @param object_y
		 * @param object_z
		 * @return
		 */
		public static Orbit fixed(float object_x, float object_y, float object_z) {
			return (float time) -> Pos.of(object_x, object_y, object_z);
		}

		public static Orbit fixed(float object_x, float object_y, float object_z, Pos.ViewHeight final_view_height) {
			return (float time) -> Pos.of(object_x, object_y, object_z, final_view_height);
		}

		/**
		 * 固定高度正圆形轨道
		 * 
		 * @param center_x
		 * @param center_y
		 * @param center_z
		 * @param radius
		 * @param angular_speed 角速度
		 * @param initial_phase 初始相位
		 * @return
		 */
		public static Orbit circle(float center_x, float center_y, float center_z, float radius, float angular_speed, float initial_phase) {
			return (float time) -> {
				float phase = initial_phase + angular_speed * time;
				return Pos.of(center_x + (float) Math.sin(phase) * radius, center_y, center_z + (float) Math.cos(phase) * radius);
			};
		}

		public static Orbit circle(float center_x, float center_y, float center_z, float radius, float angular_speed, float initial_phase, Pos.ViewHeight final_view_height) {
			return (float time) -> {
				float phase = initial_phase + angular_speed * time;
				return Pos.of(center_x + (float) Math.sin(phase) * radius, center_y, center_z + (float) Math.cos(phase) * radius, final_view_height);
			};
		}

		/**
		 * 圆心在其他轨道上的圆形轨道
		 * 
		 * @param center_orbit  圆心所处轨道，该轨道只取x、z值
		 * @param center_height
		 * @param radius
		 * @param angular_speed
		 * @param initial_phase
		 * @return
		 */
		public static Orbit circle(Orbit center_orbit, float radius, float angular_speed, float initial_phase) {
			return (float time) -> {
				float phase = initial_phase + angular_speed * time;
				Pos center = center_orbit.position(time);
				return Pos.of(center.object_x + (float) Math.sin(phase) * radius, center.object_y, center.object_z + (float) Math.cos(phase) * radius);
			};
		}

		public static Orbit circle(Orbit center_orbit, float radius, float angular_speed) {
			return circle(center_orbit, radius, angular_speed, 0);
		}

		public static Orbit circle(Orbit center_orbit, float radius, float angular_speed, float initial_phase, Pos.ViewHeight final_view_height) {
			return (float time) -> {
				float phase = initial_phase + angular_speed * time;
				Pos center = center_orbit.position(time);
				return Pos.of(center.object_x + (float) Math.sin(phase) * radius, center.object_y, center.object_z + (float) Math.cos(phase) * radius, final_view_height);
			};
		}

		public static Orbit circle(Orbit center_orbit, float radius, float angular_speed, Pos.ViewHeight final_view_height) {
			return circle(center_orbit, radius, angular_speed, 0, final_view_height);
		}

		/**
		 * @param center_x
		 * @param center_z
		 * @param radius
		 * @param view_height
		 * @param angular_speed 单位rad/tick
		 * @return
		 */
		public static Orbit circle(float center_x, float center_height, float center_z, float radius, float angular_speed) {
			return circle(center_x, center_height, center_z, radius, angular_speed, 0);
		}

		public static Orbit circle(float center_x, float center_height, float center_z, float radius, float angular_speed, Pos.ViewHeight final_view_height) {
			return circle(center_x, center_height, center_z, radius, angular_speed, 0, final_view_height);
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
		node.setUpdate((SceneGraphNode dest_node, float cam_x, float cam_y, float cam_z, float time) -> {
			track.updateTransform(cam_x, cam_y, cam_z, time);
		});
		return track;
	}

	public static final NearEarthObject bind(SceneGraphNode node, float object_x, float object_y, float object_z) {
		return bind(node, Orbit.fixed(object_x, object_y, object_z));
	}

	public static final NearEarthObject bind(SceneGraphNode node, float object_x, float object_y, float object_z, Pos.ViewHeight final_view_height) {
		return bind(node, Orbit.fixed(object_x, object_y, object_z, final_view_height));
	}

	private void updateTransform(float player_x, float player_y, float player_z, float time) {
		Matrix4f incline = new Matrix4f();
		incline.rotate(Axis.XP.rotationDegrees(-90.0f));// 绕正x轴旋转90°，让物体位于仰视视角
		incline.rotate(Axis.YN.rotation(spin.angularSpeed(time)));
		Pos pos = orbit.position(time);// 计算轨道坐标
		float offset_x = pos.object_x - player_x;
		float offset_z = player_z - pos.object_z;
		float offset_y = pos.object_y - player_y;
		incline.translate(new Vector3f(
				offset_x / pos.x_offset_decay.calc(pos.object_x, offset_x, pos.object_y, offset_y, pos.object_z, offset_z),
				offset_z / pos.z_offset_decay.calc(pos.object_x, offset_x, pos.object_y, offset_y, pos.object_z, offset_z),
				pos.final_view_height.calc(pos.object_x, offset_x, pos.object_y, offset_y, pos.object_z, offset_z)));
		node.setTransform(incline);
	}
}
