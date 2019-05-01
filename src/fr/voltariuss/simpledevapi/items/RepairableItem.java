package fr.voltariuss.simpledevapi.items;

import org.bukkit.Material;

/**
 * Énumération des items réparables
 * 
 * @author Voltariuss
 * @version 1.5.0
 *
 */
public enum RepairableItem {

	DIAMOND_PICKAXE(Material.DIAMOND_PICKAXE),
	DIAMOND_SWORD(Material.DIAMOND_SWORD),
	DIAMOND_SPADE(Material.DIAMOND_SPADE),
	DIAMOND_AXE(Material.DIAMOND_AXE),
	DIAMOND_HOE(Material.DIAMOND_HOE),
	DIAMOND_HELMET(Material.DIAMOND_HELMET),
	DIAMOND_CHESTPLATE(Material.DIAMOND_CHESTPLATE),
	DIAMOND_LEGGINGS(Material.DIAMOND_LEGGINGS),
	DIAMOND_BOOTS(Material.DIAMOND_BOOTS),
	IRON_PICKAXE(Material.IRON_PICKAXE),
	IRON_SWORD(Material.IRON_SWORD),
	IRON_SPADE(Material.IRON_SPADE),
	IRON_AXE(Material.IRON_AXE),
	IRON_HOE(Material.IRON_HOE),
	IRON_HELMET(Material.IRON_HELMET),
	IRON_CHESTPLATE(Material.IRON_CHESTPLATE),
	IRON_LEGGINGS(Material.IRON_LEGGINGS),
	IRON_BOOTS(Material.IRON_BOOTS),
	GOLD_PICKAXE(Material.GOLD_PICKAXE),
	GOLD_SWORD(Material.GOLD_SWORD),
	GOLD_SPADE(Material.GOLD_SPADE),
	GOLD_AXE(Material.GOLD_AXE),
	GOLD_HOE(Material.GOLD_HOE),
	GOLD_HELMET(Material.GOLD_HELMET),
	GOLD_CHESTPLATE(Material.GOLD_CHESTPLATE),
	GOLD_LEGGINGS(Material.GOLD_LEGGINGS),
	GOLD_BOOTS(Material.GOLD_BOOTS),
	STONE_PICKAXE(Material.STONE_PICKAXE),
	STONE_SWORD(Material.STONE_SWORD),
	STONE_SPADE(Material.STONE_SPADE),
	STONE_AXE(Material.STONE_AXE),
	STONE_HOE(Material.STONE_HOE),
	CHAINMAIL_HELMET(Material.CHAINMAIL_HELMET),
	CHAINMAIL_CHESTPLATE(Material.CHAINMAIL_CHESTPLATE),
	CHAINMAIL_LEGGINGS(Material.CHAINMAIL_LEGGINGS),
	CHAINMAIL_BOOTS(Material.CHAINMAIL_BOOTS),
	WOOD_PICKAXE(Material.WOOD_PICKAXE),
	WOOD_SWORD(Material.WOOD_SWORD),
	WOOD_SPADE(Material.WOOD_SPADE),
	WOOD_AXE(Material.WOOD_AXE),
	WOOD_HOE(Material.WOOD_HOE),
	LEATHER_HELMET(Material.LEATHER_HELMET),
	LEATHER_CHESTPLATE(Material.LEATHER_CHESTPLATE),
	LEATHER_LEGGINGS(Material.LEATHER_LEGGINGS),
	LEATHER_BOOTS(Material.LEATHER_BOOTS),
	FLINT_AND_STEEL(Material.FLINT_AND_STEEL),
	SHEARS(Material.SHEARS),
	BOW(Material.BOW),
	FISHING_ROD(Material.FISHING_ROD),
	ANVIL(Material.ANVIL);
	
	private Material type;

	/**
	 * Constructeur
	 * 
	 * @param type
	 *            Le type de l'item, non null
	 */
	private RepairableItem(Material type) {
		setType(type);
	}

	/**
	 * @return Le type de l'item, non null
	 */
	public Material getType() {
		return type;
	}

	/**
	 * Définit le type de l'item.
	 * 
	 * @param type
	 *            Le nouveau type de l'item, non null
	 */
	private void setType(Material type) {
		this.type = type;
	}
}
