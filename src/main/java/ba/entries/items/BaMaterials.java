package ba.entries.items;

import fw.datagen.annotation.ItemDatagen;
import fw.datagen.annotation.LangDatagen;
import fw.datagen.annotation.Translation;
import fw.items.ExtItem;
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
	public static final DeferredItem<Item> antikythera_0 = ExtItem.register("antikythera_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Antikythera 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> antikythera_1 = ExtItem.register("antikythera_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Antikythera 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> antikythera_2 = ExtItem.register("antikythera_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Antikythera 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> antikythera_3 = ExtItem.register("antikythera_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Atlantismedal 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> atlantismedal_0 = ExtItem.register("atlantismedal_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Atlantismedal 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> atlantismedal_1 = ExtItem.register("atlantismedal_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Atlantismedal 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> atlantismedal_2 = ExtItem.register("atlantismedal_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Atlantismedal 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> atlantismedal_3 = ExtItem.register("atlantismedal_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Baghdad 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> baghdad_0 = ExtItem.register("baghdad_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Baghdad 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> baghdad_1 = ExtItem.register("baghdad_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Baghdad 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> baghdad_2 = ExtItem.register("baghdad_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Baghdad 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> baghdad_3 = ExtItem.register("baghdad_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Compass 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> compass_0 = ExtItem.register("compass_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Compass 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> compass_1 = ExtItem.register("compass_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Compass 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> compass_2 = ExtItem.register("compass_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Compass 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> compass_3 = ExtItem.register("compass_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Copia 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> copia_0 = ExtItem.register("copia_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Copia 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> copia_1 = ExtItem.register("copia_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Copia 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> copia_2 = ExtItem.register("copia_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Copia 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> copia_3 = ExtItem.register("copia_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Crystalhaniwa 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> crystalhaniwa_0 = ExtItem.register("crystalhaniwa_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Crystalhaniwa 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> crystalhaniwa_1 = ExtItem.register("crystalhaniwa_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Crystalhaniwa 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> crystalhaniwa_2 = ExtItem.register("crystalhaniwa_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Crystalhaniwa 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> crystalhaniwa_3 = ExtItem.register("crystalhaniwa_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Dirtdool 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> dirtdool_0 = ExtItem.register("dirtdool_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Dirtdool 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> dirtdool_1 = ExtItem.register("dirtdool_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Dirtdool 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> dirtdool_2 = ExtItem.register("dirtdool_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Dirtdool 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> dirtdool_3 = ExtItem.register("dirtdool_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Discocolgante 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> discocolgante_0 = ExtItem.register("discocolgante_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Discocolgante 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> discocolgante_1 = ExtItem.register("discocolgante_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Discocolgante 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> discocolgante_2 = ExtItem.register("discocolgante_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Discocolgante 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> discocolgante_3 = ExtItem.register("discocolgante_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Dreamcatcher 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> dreamcatcher_0 = ExtItem.register("dreamcatcher_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Dreamcatcher 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> dreamcatcher_1 = ExtItem.register("dreamcatcher_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Dreamcatcher 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> dreamcatcher_2 = ExtItem.register("dreamcatcher_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Dreamcatcher 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> dreamcatcher_3 = ExtItem.register("dreamcatcher_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Ether 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> ether_0 = ExtItem.register("ether_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Ether 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> ether_1 = ExtItem.register("ether_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Ether 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> ether_2 = ExtItem.register("ether_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Ether 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> ether_3 = ExtItem.register("ether_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Abydos 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_abydos_0 = ExtItem.register("exskill_abydos_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Abydos 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_abydos_1 = ExtItem.register("exskill_abydos_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Abydos 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_abydos_2 = ExtItem.register("exskill_abydos_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Abydos 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_abydos_3 = ExtItem.register("exskill_abydos_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Arius 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_arius_0 = ExtItem.register("exskill_arius_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Arius 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_arius_1 = ExtItem.register("exskill_arius_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Arius 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_arius_2 = ExtItem.register("exskill_arius_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Arius 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_arius_3 = ExtItem.register("exskill_arius_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Gehenna 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_gehenna_0 = ExtItem.register("exskill_gehenna_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Gehenna 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_gehenna_1 = ExtItem.register("exskill_gehenna_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Gehenna 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_gehenna_2 = ExtItem.register("exskill_gehenna_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Gehenna 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_gehenna_3 = ExtItem.register("exskill_gehenna_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Hyakkiyako 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_hyakkiyako_0 = ExtItem.register("exskill_hyakkiyako_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Hyakkiyako 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_hyakkiyako_1 = ExtItem.register("exskill_hyakkiyako_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Hyakkiyako 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_hyakkiyako_2 = ExtItem.register("exskill_hyakkiyako_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Hyakkiyako 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_hyakkiyako_3 = ExtItem.register("exskill_hyakkiyako_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Millennium 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_millennium_0 = ExtItem.register("exskill_millennium_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Millennium 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_millennium_1 = ExtItem.register("exskill_millennium_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Millennium 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_millennium_2 = ExtItem.register("exskill_millennium_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Millennium 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_millennium_3 = ExtItem.register("exskill_millennium_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Redwinter 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_redwinter_0 = ExtItem.register("exskill_redwinter_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Redwinter 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_redwinter_1 = ExtItem.register("exskill_redwinter_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Redwinter 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_redwinter_2 = ExtItem.register("exskill_redwinter_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Redwinter 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_redwinter_3 = ExtItem.register("exskill_redwinter_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Shanhaijing 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_shanhaijing_0 = ExtItem.register("exskill_shanhaijing_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Shanhaijing 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_shanhaijing_1 = ExtItem.register("exskill_shanhaijing_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Shanhaijing 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_shanhaijing_2 = ExtItem.register("exskill_shanhaijing_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Shanhaijing 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_shanhaijing_3 = ExtItem.register("exskill_shanhaijing_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Trinity 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_trinity_0 = ExtItem.register("exskill_trinity_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Trinity 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_trinity_1 = ExtItem.register("exskill_trinity_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Trinity 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_trinity_2 = ExtItem.register("exskill_trinity_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Trinity 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_trinity_3 = ExtItem.register("exskill_trinity_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Valkyrie 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_valkyrie_0 = ExtItem.register("exskill_valkyrie_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Valkyrie 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_valkyrie_1 = ExtItem.register("exskill_valkyrie_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Valkyrie 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_valkyrie_2 = ExtItem.register("exskill_valkyrie_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Exskill Valkyrie 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> exskill_valkyrie_3 = ExtItem.register("exskill_valkyrie_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Goldenfleece 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> goldenfleece_0 = ExtItem.register("goldenfleece_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Goldenfleece 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> goldenfleece_1 = ExtItem.register("goldenfleece_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Goldenfleece 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> goldenfleece_2 = ExtItem.register("goldenfleece_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Goldenfleece 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> goldenfleece_3 = ExtItem.register("goldenfleece_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Jamhsid 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> jamhsid_0 = ExtItem.register("jamhsid_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Jamhsid 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> jamhsid_1 = ExtItem.register("jamhsid_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Jamhsid 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> jamhsid_2 = ExtItem.register("jamhsid_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Jamhsid 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> jamhsid_3 = ExtItem.register("jamhsid_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Kikuko 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> kikuko_0 = ExtItem.register("kikuko_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Kikuko 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> kikuko_1 = ExtItem.register("kikuko_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Kikuko 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> kikuko_2 = ExtItem.register("kikuko_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Kikuko 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> kikuko_3 = ExtItem.register("kikuko_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Lokimask 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> lokimask_0 = ExtItem.register("lokimask_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Lokimask 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> lokimask_1 = ExtItem.register("lokimask_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Lokimask 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> lokimask_2 = ExtItem.register("lokimask_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Lokimask 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> lokimask_3 = ExtItem.register("lokimask_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Lyra 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> lyra_0 = ExtItem.register("lyra_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Lyra 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> lyra_1 = ExtItem.register("lyra_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Lyra 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> lyra_2 = ExtItem.register("lyra_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Lyra 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> lyra_3 = ExtItem.register("lyra_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Mandragora 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> mandragora_0 = ExtItem.register("mandragora_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Mandragora 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> mandragora_1 = ExtItem.register("mandragora_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Mandragora 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> mandragora_2 = ExtItem.register("mandragora_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Mandragora 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> mandragora_3 = ExtItem.register("mandragora_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Nebra 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> nebra_0 = ExtItem.register("nebra_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Nebra 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> nebra_1 = ExtItem.register("nebra_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Nebra 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> nebra_2 = ExtItem.register("nebra_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Nebra 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> nebra_3 = ExtItem.register("nebra_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Nimrud 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> nimrud_0 = ExtItem.register("nimrud_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Nimrud 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> nimrud_1 = ExtItem.register("nimrud_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Nimrud 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> nimrud_2 = ExtItem.register("nimrud_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Nimrud 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> nimrud_3 = ExtItem.register("nimrud_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Phaistos 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> phaistos_0 = ExtItem.register("phaistos_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Phaistos 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> phaistos_1 = ExtItem.register("phaistos_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Phaistos 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> phaistos_2 = ExtItem.register("phaistos_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Phaistos 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> phaistos_3 = ExtItem.register("phaistos_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Quimbaya 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> quimbaya_0 = ExtItem.register("quimbaya_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Quimbaya 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> quimbaya_1 = ExtItem.register("quimbaya_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Quimbaya 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> quimbaya_2 = ExtItem.register("quimbaya_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Quimbaya 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> quimbaya_3 = ExtItem.register("quimbaya_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Random 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> random_0 = ExtItem.register("random_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Random 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> random_1 = ExtItem.register("random_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Random 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> random_2 = ExtItem.register("random_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Random 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> random_3 = ExtItem.register("random_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Rocket 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> rocket_0 = ExtItem.register("rocket_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Rocket 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> rocket_1 = ExtItem.register("rocket_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Rocket 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> rocket_2 = ExtItem.register("rocket_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Rocket 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> rocket_3 = ExtItem.register("rocket_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Rohonc 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> rohonc_0 = ExtItem.register("rohonc_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Rohonc 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> rohonc_1 = ExtItem.register("rohonc_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Rohonc 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> rohonc_2 = ExtItem.register("rohonc_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Rohonc 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> rohonc_3 = ExtItem.register("rohonc_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Romandice 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> romandice_0 = ExtItem.register("romandice_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Romandice 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> romandice_1 = ExtItem.register("romandice_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Romandice 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> romandice_2 = ExtItem.register("romandice_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Romandice 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> romandice_3 = ExtItem.register("romandice_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Selection 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> selection_0 = ExtItem.register("selection_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Selection 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> selection_1 = ExtItem.register("selection_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Selection 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> selection_2 = ExtItem.register("selection_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Selection 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> selection_3 = ExtItem.register("selection_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Totempole 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> totempole_0 = ExtItem.register("totempole_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Totempole 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> totempole_1 = ExtItem.register("totempole_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Totempole 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> totempole_2 = ExtItem.register("totempole_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Totempole 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> totempole_3 = ExtItem.register("totempole_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Usedcard"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> usedcard = ExtItem.register("usedcard", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Vajra 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> vajra_0 = ExtItem.register("vajra_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Vajra 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> vajra_1 = ExtItem.register("vajra_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Vajra 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> vajra_2 = ExtItem.register("vajra_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Vajra 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> vajra_3 = ExtItem.register("vajra_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Voynich 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> voynich_0 = ExtItem.register("voynich_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Voynich 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> voynich_1 = ExtItem.register("voynich_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Voynich 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> voynich_2 = ExtItem.register("voynich_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Voynich 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> voynich_3 = ExtItem.register("voynich_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Windfire 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> windfire_0 = ExtItem.register("windfire_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Windfire 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> windfire_1 = ExtItem.register("windfire_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Windfire 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> windfire_2 = ExtItem.register("windfire_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Windfire 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> windfire_3 = ExtItem.register("windfire_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Winnistone 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> winnistone_0 = ExtItem.register("winnistone_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Winnistone 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> winnistone_1 = ExtItem.register("winnistone_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Winnistone 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> winnistone_2 = ExtItem.register("winnistone_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Winnistone 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> winnistone_3 = ExtItem.register("winnistone_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Wodam 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> wodam_1 = ExtItem.register("wodam_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Wodam 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> wodam_2 = ExtItem.register("wodam_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Wodam 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> wodam_3 = ExtItem.register("wodam_3", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Wodam 4"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> wodam_4 = ExtItem.register("wodam_4", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Wolfsegg 0"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> wolfsegg_0 = ExtItem.register("wolfsegg_0", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Wolfsegg 1"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> wolfsegg_1 = ExtItem.register("wolfsegg_1", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Wolfsegg 2"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> wolfsegg_2 = ExtItem.register("wolfsegg_2", BaCreativeTab.BA_MATERIALS);

	@LangDatagen(translations = { @Translation(locale = "en_us" , text = "Wolfsegg 3"), @Translation(locale = "zh_cn" , text = "") })
	@ItemDatagen(tex_path = resourcePath)
	public static final DeferredItem<Item> wolfsegg_3 = ExtItem.register("wolfsegg_3", BaCreativeTab.BA_MATERIALS);

}
