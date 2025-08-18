package fw.terrain.decoration;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.SurfaceRules;

public class Decoration {
	BlockState block;

	private Decoration(BlockState block) {
		this.block = block;
	}

	public Decoration of(BlockState block) {
		return new Decoration(block);
	}

	public Decoration of(Block block) {
		return new Decoration(block.defaultBlockState());
	}

	public SurfaceRules.RuleSource toRuleSource() {
		return SurfaceRules.state(block);
	}
}
