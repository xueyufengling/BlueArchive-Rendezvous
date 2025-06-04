package ba.entries.items;

import fw.datagen.annotation.ItemDatagen;
import fw.datagen.annotation.LangDatagen;
import fw.datagen.annotation.Translation;
import fw.items.ExtItems;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import ba.entries.BaCreativeTab;

public class BaMaterials {

	static {
		ItemDatagen.ModelProvider.forDatagen(BaMaterials.class);
		LangDatagen.LangProvider.forDatagen(BaMaterials.class);
	}

	public static final String resourcePath = BaCreativeTab.Id.BA_MATERIALS;


	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Antikythera 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> antikythera_0 = ExtItems.register("antikythera_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Antikythera 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> antikythera_1 = ExtItems.register("antikythera_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Antikythera 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> antikythera_2 = ExtItems.register("antikythera_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Antikythera 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> antikythera_3 = ExtItems.register("antikythera_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Atlantismedal 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> atlantismedal_0 = ExtItems.register("atlantismedal_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Atlantismedal 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> atlantismedal_1 = ExtItems.register("atlantismedal_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Atlantismedal 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> atlantismedal_2 = ExtItems.register("atlantismedal_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Atlantismedal 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> atlantismedal_3 = ExtItems.register("atlantismedal_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Baghdad 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> baghdad_0 = ExtItems.register("baghdad_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Baghdad 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> baghdad_1 = ExtItems.register("baghdad_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Baghdad 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> baghdad_2 = ExtItems.register("baghdad_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Baghdad 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> baghdad_3 = ExtItems.register("baghdad_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Compass 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> compass_0 = ExtItems.register("compass_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Compass 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> compass_1 = ExtItems.register("compass_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Compass 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> compass_2 = ExtItems.register("compass_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Compass 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> compass_3 = ExtItems.register("compass_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Copia 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> copia_0 = ExtItems.register("copia_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Copia 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> copia_1 = ExtItems.register("copia_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Copia 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> copia_2 = ExtItems.register("copia_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Copia 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> copia_3 = ExtItems.register("copia_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Crystalhaniwa 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> crystalhaniwa_0 = ExtItems.register("crystalhaniwa_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Crystalhaniwa 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> crystalhaniwa_1 = ExtItems.register("crystalhaniwa_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Crystalhaniwa 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> crystalhaniwa_2 = ExtItems.register("crystalhaniwa_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Crystalhaniwa 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> crystalhaniwa_3 = ExtItems.register("crystalhaniwa_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Dirtdool 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> dirtdool_0 = ExtItems.register("dirtdool_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Dirtdool 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> dirtdool_1 = ExtItems.register("dirtdool_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Dirtdool 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> dirtdool_2 = ExtItems.register("dirtdool_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Dirtdool 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> dirtdool_3 = ExtItems.register("dirtdool_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Discocolgante 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> discocolgante_0 = ExtItems.register("discocolgante_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Discocolgante 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> discocolgante_1 = ExtItems.register("discocolgante_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Discocolgante 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> discocolgante_2 = ExtItems.register("discocolgante_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Discocolgante 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> discocolgante_3 = ExtItems.register("discocolgante_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Dreamcatcher 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> dreamcatcher_0 = ExtItems.register("dreamcatcher_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Dreamcatcher 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> dreamcatcher_1 = ExtItems.register("dreamcatcher_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Dreamcatcher 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> dreamcatcher_2 = ExtItems.register("dreamcatcher_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Dreamcatcher 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> dreamcatcher_3 = ExtItems.register("dreamcatcher_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Ether 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> ether_0 = ExtItems.register("ether_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Ether 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> ether_1 = ExtItems.register("ether_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Ether 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> ether_2 = ExtItems.register("ether_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Ether 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> ether_3 = ExtItems.register("ether_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Abydos 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_abydos_0 = ExtItems.register("exskill_abydos_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Abydos 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_abydos_1 = ExtItems.register("exskill_abydos_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Abydos 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_abydos_2 = ExtItems.register("exskill_abydos_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Abydos 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_abydos_3 = ExtItems.register("exskill_abydos_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Arius 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_arius_0 = ExtItems.register("exskill_arius_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Arius 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_arius_1 = ExtItems.register("exskill_arius_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Arius 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_arius_2 = ExtItems.register("exskill_arius_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Arius 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_arius_3 = ExtItems.register("exskill_arius_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Gehenna 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_gehenna_0 = ExtItems.register("exskill_gehenna_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Gehenna 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_gehenna_1 = ExtItems.register("exskill_gehenna_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Gehenna 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_gehenna_2 = ExtItems.register("exskill_gehenna_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Gehenna 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_gehenna_3 = ExtItems.register("exskill_gehenna_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Hyakkiyako 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_hyakkiyako_0 = ExtItems.register("exskill_hyakkiyako_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Hyakkiyako 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_hyakkiyako_1 = ExtItems.register("exskill_hyakkiyako_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Hyakkiyako 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_hyakkiyako_2 = ExtItems.register("exskill_hyakkiyako_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Hyakkiyako 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_hyakkiyako_3 = ExtItems.register("exskill_hyakkiyako_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Millennium 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_millennium_0 = ExtItems.register("exskill_millennium_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Millennium 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_millennium_1 = ExtItems.register("exskill_millennium_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Millennium 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_millennium_2 = ExtItems.register("exskill_millennium_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Millennium 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_millennium_3 = ExtItems.register("exskill_millennium_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Redwinter 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_redwinter_0 = ExtItems.register("exskill_redwinter_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Redwinter 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_redwinter_1 = ExtItems.register("exskill_redwinter_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Redwinter 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_redwinter_2 = ExtItems.register("exskill_redwinter_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Redwinter 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_redwinter_3 = ExtItems.register("exskill_redwinter_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Shanhaijing 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_shanhaijing_0 = ExtItems.register("exskill_shanhaijing_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Shanhaijing 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_shanhaijing_1 = ExtItems.register("exskill_shanhaijing_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Shanhaijing 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_shanhaijing_2 = ExtItems.register("exskill_shanhaijing_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Shanhaijing 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_shanhaijing_3 = ExtItems.register("exskill_shanhaijing_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Trinity 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_trinity_0 = ExtItems.register("exskill_trinity_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Trinity 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_trinity_1 = ExtItems.register("exskill_trinity_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Trinity 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_trinity_2 = ExtItems.register("exskill_trinity_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Trinity 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_trinity_3 = ExtItems.register("exskill_trinity_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Valkyrie 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_valkyrie_0 = ExtItems.register("exskill_valkyrie_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Valkyrie 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_valkyrie_1 = ExtItems.register("exskill_valkyrie_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Valkyrie 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_valkyrie_2 = ExtItems.register("exskill_valkyrie_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Valkyrie 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_valkyrie_3 = ExtItems.register("exskill_valkyrie_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Goldenfleece 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> goldenfleece_0 = ExtItems.register("goldenfleece_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Goldenfleece 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> goldenfleece_1 = ExtItems.register("goldenfleece_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Goldenfleece 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> goldenfleece_2 = ExtItems.register("goldenfleece_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Goldenfleece 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> goldenfleece_3 = ExtItems.register("goldenfleece_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Jamhsid 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> jamhsid_0 = ExtItems.register("jamhsid_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Jamhsid 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> jamhsid_1 = ExtItems.register("jamhsid_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Jamhsid 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> jamhsid_2 = ExtItems.register("jamhsid_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Jamhsid 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> jamhsid_3 = ExtItems.register("jamhsid_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Kikuko 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> kikuko_0 = ExtItems.register("kikuko_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Kikuko 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> kikuko_1 = ExtItems.register("kikuko_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Kikuko 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> kikuko_2 = ExtItems.register("kikuko_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Kikuko 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> kikuko_3 = ExtItems.register("kikuko_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Lokimask 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> lokimask_0 = ExtItems.register("lokimask_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Lokimask 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> lokimask_1 = ExtItems.register("lokimask_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Lokimask 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> lokimask_2 = ExtItems.register("lokimask_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Lokimask 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> lokimask_3 = ExtItems.register("lokimask_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Lyra 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> lyra_0 = ExtItems.register("lyra_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Lyra 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> lyra_1 = ExtItems.register("lyra_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Lyra 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> lyra_2 = ExtItems.register("lyra_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Lyra 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> lyra_3 = ExtItems.register("lyra_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Mandragora 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> mandragora_0 = ExtItems.register("mandragora_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Mandragora 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> mandragora_1 = ExtItems.register("mandragora_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Mandragora 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> mandragora_2 = ExtItems.register("mandragora_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Mandragora 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> mandragora_3 = ExtItems.register("mandragora_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Nebra 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> nebra_0 = ExtItems.register("nebra_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Nebra 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> nebra_1 = ExtItems.register("nebra_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Nebra 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> nebra_2 = ExtItems.register("nebra_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Nebra 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> nebra_3 = ExtItems.register("nebra_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Nimrud 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> nimrud_0 = ExtItems.register("nimrud_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Nimrud 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> nimrud_1 = ExtItems.register("nimrud_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Nimrud 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> nimrud_2 = ExtItems.register("nimrud_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Nimrud 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> nimrud_3 = ExtItems.register("nimrud_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Phaistos 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> phaistos_0 = ExtItems.register("phaistos_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Phaistos 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> phaistos_1 = ExtItems.register("phaistos_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Phaistos 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> phaistos_2 = ExtItems.register("phaistos_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Phaistos 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> phaistos_3 = ExtItems.register("phaistos_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Quimbaya 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> quimbaya_0 = ExtItems.register("quimbaya_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Quimbaya 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> quimbaya_1 = ExtItems.register("quimbaya_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Quimbaya 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> quimbaya_2 = ExtItems.register("quimbaya_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Quimbaya 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> quimbaya_3 = ExtItems.register("quimbaya_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Random 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> random_0 = ExtItems.register("random_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Random 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> random_1 = ExtItems.register("random_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Random 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> random_2 = ExtItems.register("random_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Random 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> random_3 = ExtItems.register("random_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Rocket 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> rocket_0 = ExtItems.register("rocket_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Rocket 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> rocket_1 = ExtItems.register("rocket_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Rocket 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> rocket_2 = ExtItems.register("rocket_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Rocket 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> rocket_3 = ExtItems.register("rocket_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Rohonc 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> rohonc_0 = ExtItems.register("rohonc_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Rohonc 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> rohonc_1 = ExtItems.register("rohonc_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Rohonc 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> rohonc_2 = ExtItems.register("rohonc_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Rohonc 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> rohonc_3 = ExtItems.register("rohonc_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Romandice 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> romandice_0 = ExtItems.register("romandice_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Romandice 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> romandice_1 = ExtItems.register("romandice_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Romandice 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> romandice_2 = ExtItems.register("romandice_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Romandice 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> romandice_3 = ExtItems.register("romandice_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Selection 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> selection_0 = ExtItems.register("selection_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Selection 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> selection_1 = ExtItems.register("selection_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Selection 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> selection_2 = ExtItems.register("selection_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Selection 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> selection_3 = ExtItems.register("selection_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Totempole 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> totempole_0 = ExtItems.register("totempole_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Totempole 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> totempole_1 = ExtItems.register("totempole_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Totempole 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> totempole_2 = ExtItems.register("totempole_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Totempole 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> totempole_3 = ExtItems.register("totempole_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Usedcard"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> usedcard = ExtItems.register("usedcard", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Vajra 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> vajra_0 = ExtItems.register("vajra_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Vajra 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> vajra_1 = ExtItems.register("vajra_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Vajra 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> vajra_2 = ExtItems.register("vajra_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Vajra 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> vajra_3 = ExtItems.register("vajra_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Voynich 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> voynich_0 = ExtItems.register("voynich_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Voynich 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> voynich_1 = ExtItems.register("voynich_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Voynich 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> voynich_2 = ExtItems.register("voynich_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Voynich 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> voynich_3 = ExtItems.register("voynich_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Windfire 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> windfire_0 = ExtItems.register("windfire_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Windfire 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> windfire_1 = ExtItems.register("windfire_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Windfire 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> windfire_2 = ExtItems.register("windfire_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Windfire 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> windfire_3 = ExtItems.register("windfire_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Winnistone 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> winnistone_0 = ExtItems.register("winnistone_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Winnistone 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> winnistone_1 = ExtItems.register("winnistone_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Winnistone 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> winnistone_2 = ExtItems.register("winnistone_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Winnistone 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> winnistone_3 = ExtItems.register("winnistone_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Wodam 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> wodam_1 = ExtItems.register("wodam_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Wodam 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> wodam_2 = ExtItems.register("wodam_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Wodam 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> wodam_3 = ExtItems.register("wodam_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Wodam 4"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> wodam_4 = ExtItems.register("wodam_4", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Wolfsegg 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> wolfsegg_0 = ExtItems.register("wolfsegg_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Wolfsegg 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> wolfsegg_1 = ExtItems.register("wolfsegg_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Wolfsegg 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> wolfsegg_2 = ExtItems.register("wolfsegg_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Wolfsegg 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> wolfsegg_3 = ExtItems.register("wolfsegg_3", BaCreativeTab.BA_MATERIALS);

}
