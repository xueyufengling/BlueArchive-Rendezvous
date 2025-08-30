package lepus.mc.terrain.decoration;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.level.levelgen.SurfaceRules;

/**
 * 地表填充规则
 */
public class TerrainDecorator {
	private final ArrayList<Decoration> decorations = new ArrayList<>();

	private TerrainDecorator() {

	}

	public static final TerrainDecorator begin() {
		return new TerrainDecorator();
	}

	public TerrainDecorator layer(Decoration... decorations) {
		this.decorations.addAll(List.of(decorations));
		return this;
	}

	public SurfaceRules.RuleSource toRuleSource() {
		SurfaceRules.RuleSource[] rules = new SurfaceRules.RuleSource[decorations.size()];
		for (int idx = 0; idx < rules.length; ++idx)
			rules[idx] = decorations.get(idx).toRuleSource();
		return SurfaceRules.sequence(rules);
	}
}
