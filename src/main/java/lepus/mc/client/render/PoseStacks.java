package lepus.mc.client.render;

import java.lang.reflect.Field;
import java.util.ArrayDeque;

import com.mojang.blaze3d.vertex.PoseStack;

import lyra.lang.InternalUnsafe;
import lyra.lang.Reflection;
import lyra.object.ObjectManipulator;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PoseStacks {
	private static final Field ArrayDeque_elements;
	private static final Field ArrayDeque_head;

	static {
		ArrayDeque_elements = Reflection.getField(ArrayDeque.class, "elements");
		ArrayDeque_head = Reflection.getField(ArrayDeque.class, "head");
	}

	/**
	 * 获取PoseStack内部的Pose
	 * 
	 * @param poseStack
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static ArrayDeque<PoseStack.Pose> poseStackImpl(PoseStack poseStack) {
		return (ArrayDeque<PoseStack.Pose>) ObjectManipulator.getDeclaredMemberObject(poseStack, "poseStack");
	}

	/**
	 * 在PoseStack顶层推入指定Pose
	 * 
	 * @param poseStack
	 * @param pose
	 */
	public static void pushPose(PoseStack poseStack, PoseStack.Pose pose) {
		poseStackImpl(poseStack).addLast(pose);
	}

	public static PoseStack.Pose clone(PoseStack.Pose pose) {
		PoseStack.Pose cloned = null;
		try {
			cloned = InternalUnsafe.allocateInstance(PoseStack.Pose.class);
			ObjectManipulator.setDeclaredMemberObject(cloned, "pose", pose.pose().clone());
			ObjectManipulator.setDeclaredMemberObject(cloned, "normal", pose.normal().clone());
		} catch (CloneNotSupportedException ex) {
			ex.printStackTrace();
		}
		return cloned;
	}

	public static PoseStack clone(PoseStack poseStack) {
		PoseStack cloned = null;
		cloned = InternalUnsafe.allocateInstance(PoseStack.class);// 分配内存
		ArrayDeque<PoseStack.Pose> innerPoseStack = poseStackImpl(poseStack).clone();// 拷贝ArrayDeque对象
		Object[] elements = (Object[]) ObjectManipulator.access(innerPoseStack, ArrayDeque_elements);// 对ArrayDeque对象的内部数组进行深拷贝
		int head = (int) ObjectManipulator.access(innerPoseStack, ArrayDeque_head);// 获取第一个元素的偏移量
		for (int i = 0; i < innerPoseStack.size(); ++i) {
			elements[head + i] = clone((PoseStack.Pose) (elements[head + i]));
		}
		ObjectManipulator.setDeclaredMemberObject(cloned, "poseStack", innerPoseStack);
		return cloned;
	}
}
