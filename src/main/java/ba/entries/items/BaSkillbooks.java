package ba.entries.items;

import fw.datagen.annotation.ItemDatagen;
import fw.datagen.annotation.LangDatagen;
import fw.datagen.annotation.Translation;
import fw.items.ExtItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import ba.entries.BaCreativeTab;

public class BaSkillbooks {

	static {
		ItemDatagen.ModelProvider.forDatagen(BaSkillbooks.class);
		LangDatagen.LangProvider.forDatagen(BaSkillbooks.class);
	}

	public static final String resourcePath = BaCreativeTab.Id.BA_SKILLBOOKS;


	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Skillbook Abydos 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> skillbook_abydos_0 = ExtItem.register("skillbook_abydos_0", BaCreativeTab.BA_SKILLBOOKS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Skillbook Abydos 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> skillbook_abydos_1 = ExtItem.register("skillbook_abydos_1", BaCreativeTab.BA_SKILLBOOKS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Skillbook Abydos 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> skillbook_abydos_2 = ExtItem.register("skillbook_abydos_2", BaCreativeTab.BA_SKILLBOOKS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Skillbook Abydos 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> skillbook_abydos_3 = ExtItem.register("skillbook_abydos_3", BaCreativeTab.BA_SKILLBOOKS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Skillbook Arius 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> skillbook_arius_0 = ExtItem.register("skillbook_arius_0", BaCreativeTab.BA_SKILLBOOKS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Skillbook Arius 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> skillbook_arius_1 = ExtItem.register("skillbook_arius_1", BaCreativeTab.BA_SKILLBOOKS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Skillbook Arius 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> skillbook_arius_2 = ExtItem.register("skillbook_arius_2", BaCreativeTab.BA_SKILLBOOKS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Skillbook Arius 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> skillbook_arius_3 = ExtItem.register("skillbook_arius_3", BaCreativeTab.BA_SKILLBOOKS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Skillbook Gehenna 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> skillbook_gehenna_0 = ExtItem.register("skillbook_gehenna_0", BaCreativeTab.BA_SKILLBOOKS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Skillbook Gehenna 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> skillbook_gehenna_1 = ExtItem.register("skillbook_gehenna_1", BaCreativeTab.BA_SKILLBOOKS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Skillbook Gehenna 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> skillbook_gehenna_2 = ExtItem.register("skillbook_gehenna_2", BaCreativeTab.BA_SKILLBOOKS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Skillbook Gehenna 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> skillbook_gehenna_3 = ExtItem.register("skillbook_gehenna_3", BaCreativeTab.BA_SKILLBOOKS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Skillbook Hyakkiyako 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> skillbook_hyakkiyako_0 = ExtItem.register("skillbook_hyakkiyako_0", BaCreativeTab.BA_SKILLBOOKS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Skillbook Hyakkiyako 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> skillbook_hyakkiyako_1 = ExtItem.register("skillbook_hyakkiyako_1", BaCreativeTab.BA_SKILLBOOKS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Skillbook Hyakkiyako 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> skillbook_hyakkiyako_2 = ExtItem.register("skillbook_hyakkiyako_2", BaCreativeTab.BA_SKILLBOOKS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Skillbook Hyakkiyako 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> skillbook_hyakkiyako_3 = ExtItem.register("skillbook_hyakkiyako_3", BaCreativeTab.BA_SKILLBOOKS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Skillbook Millennium 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> skillbook_millennium_0 = ExtItem.register("skillbook_millennium_0", BaCreativeTab.BA_SKILLBOOKS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Skillbook Millennium 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> skillbook_millennium_1 = ExtItem.register("skillbook_millennium_1", BaCreativeTab.BA_SKILLBOOKS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Skillbook Millennium 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> skillbook_millennium_2 = ExtItem.register("skillbook_millennium_2", BaCreativeTab.BA_SKILLBOOKS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Skillbook Millennium 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> skillbook_millennium_3 = ExtItem.register("skillbook_millennium_3", BaCreativeTab.BA_SKILLBOOKS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Skillbook Random 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> skillbook_random_0 = ExtItem.register("skillbook_random_0", BaCreativeTab.BA_SKILLBOOKS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Skillbook Random 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> skillbook_random_1 = ExtItem.register("skillbook_random_1", BaCreativeTab.BA_SKILLBOOKS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Skillbook Random 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> skillbook_random_2 = ExtItem.register("skillbook_random_2", BaCreativeTab.BA_SKILLBOOKS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Skillbook Random 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> skillbook_random_3 = ExtItem.register("skillbook_random_3", BaCreativeTab.BA_SKILLBOOKS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Skillbook Redwinter 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> skillbook_redwinter_0 = ExtItem.register("skillbook_redwinter_0", BaCreativeTab.BA_SKILLBOOKS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Skillbook Redwinter 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> skillbook_redwinter_1 = ExtItem.register("skillbook_redwinter_1", BaCreativeTab.BA_SKILLBOOKS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Skillbook Redwinter 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> skillbook_redwinter_2 = ExtItem.register("skillbook_redwinter_2", BaCreativeTab.BA_SKILLBOOKS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Skillbook Redwinter 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> skillbook_redwinter_3 = ExtItem.register("skillbook_redwinter_3", BaCreativeTab.BA_SKILLBOOKS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Skillbook Selection 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> skillbook_selection_0 = ExtItem.register("skillbook_selection_0", BaCreativeTab.BA_SKILLBOOKS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Skillbook Selection 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> skillbook_selection_1 = ExtItem.register("skillbook_selection_1", BaCreativeTab.BA_SKILLBOOKS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Skillbook Selection 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> skillbook_selection_2 = ExtItem.register("skillbook_selection_2", BaCreativeTab.BA_SKILLBOOKS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Skillbook Selection 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> skillbook_selection_3 = ExtItem.register("skillbook_selection_3", BaCreativeTab.BA_SKILLBOOKS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Skillbook Shanhaijing 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> skillbook_shanhaijing_0 = ExtItem.register("skillbook_shanhaijing_0", BaCreativeTab.BA_SKILLBOOKS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Skillbook Shanhaijing 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> skillbook_shanhaijing_1 = ExtItem.register("skillbook_shanhaijing_1", BaCreativeTab.BA_SKILLBOOKS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Skillbook Shanhaijing 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> skillbook_shanhaijing_2 = ExtItem.register("skillbook_shanhaijing_2", BaCreativeTab.BA_SKILLBOOKS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Skillbook Shanhaijing 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> skillbook_shanhaijing_3 = ExtItem.register("skillbook_shanhaijing_3", BaCreativeTab.BA_SKILLBOOKS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Skillbook Trinity 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> skillbook_trinity_0 = ExtItem.register("skillbook_trinity_0", BaCreativeTab.BA_SKILLBOOKS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Skillbook Trinity 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> skillbook_trinity_1 = ExtItem.register("skillbook_trinity_1", BaCreativeTab.BA_SKILLBOOKS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Skillbook Trinity 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> skillbook_trinity_2 = ExtItem.register("skillbook_trinity_2", BaCreativeTab.BA_SKILLBOOKS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Skillbook Trinity 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> skillbook_trinity_3 = ExtItem.register("skillbook_trinity_3", BaCreativeTab.BA_SKILLBOOKS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Skillbook Ultimate"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> skillbook_ultimate = ExtItem.register("skillbook_ultimate", BaCreativeTab.BA_SKILLBOOKS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Skillbook Ultimate Piece"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> skillbook_ultimate_piece = ExtItem.register("skillbook_ultimate_piece", BaCreativeTab.BA_SKILLBOOKS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Skillbook Valkyrie 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> skillbook_valkyrie_0 = ExtItem.register("skillbook_valkyrie_0", BaCreativeTab.BA_SKILLBOOKS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Skillbook Valkyrie 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> skillbook_valkyrie_1 = ExtItem.register("skillbook_valkyrie_1", BaCreativeTab.BA_SKILLBOOKS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Skillbook Valkyrie 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> skillbook_valkyrie_2 = ExtItem.register("skillbook_valkyrie_2", BaCreativeTab.BA_SKILLBOOKS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Skillbook Valkyrie 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> skillbook_valkyrie_3 = ExtItem.register("skillbook_valkyrie_3", BaCreativeTab.BA_SKILLBOOKS);

}
