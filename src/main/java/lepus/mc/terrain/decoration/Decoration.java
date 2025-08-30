package lepus.mc.terrain.decoration;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.SurfaceRules;

/**
 * 有条件的方块替换
 */
public class Decoration {
	private final ArrayList<SurfaceRules.ConditionSource> conditions = new ArrayList<>();
	public BlockState block;

	private Decoration() {

	}

	private Decoration(BlockState block) {
		this.block = block;
	}

	public static final Decoration begin() {
		return new Decoration();
	}

	public static final Decoration begin(BlockState block) {
		return new Decoration(block);
	}

	public static final Decoration begin(Block block) {
		return new Decoration(block.defaultBlockState());
	}

	public Decoration when(SurfaceRules.ConditionSource... conditions) {
		this.conditions.addAll(List.of(conditions));
		return this;
	}

	public Decoration setBlock(BlockState block) {
		this.block = block;
		return this;
	}

	public Decoration setBlock(Block block) {
		this.block = block.defaultBlockState();
		return this;
	}

	public SurfaceRules.RuleSource toRuleSource() {
		int last_idx = conditions.size() - 1;
		SurfaceRules.RuleSource finalRule = SurfaceRules.ifTrue(
				conditions.get(last_idx),
				SurfaceRules.state(block));
		for (int idx = last_idx - 1; idx >= 0; --idx)
			finalRule = SurfaceRules.ifTrue(
					conditions.get(idx),
					finalRule);
		return finalRule;
	}
}