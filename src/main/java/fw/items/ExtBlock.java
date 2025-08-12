package fw.items;

import fw.core.registry.RegistryMap;
import fw.core.registry.RegistryMap.BlockMap;
import fw.datagen.Localizable;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

/**
 * 没有任何功能的简单方块，通常是装饰性方块
 */
public class ExtBlock extends Block implements Localizable {
	public static final RegistryMap.BlockMap BLOCKS = (BlockMap) RegistryMap.of(Registries.BLOCK);

	public ExtBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	public String localizationKey() {
		return Localizable.stdLocalizationKey(this.getDescriptionId());
	}
}