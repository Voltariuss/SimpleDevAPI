package fr.voltariuss.simpledevapi.items;

import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Classe utilitaire pour la création et la gestion des items
 * 
 * @author Voltariuss
 * @version 1.5.0
 *
 */
public class ItemUtils {

	/**
	 * Génère l'item correspondant aux caractéristiques spécifiées.
	 * 
	 * @param type
	 *            Le materiel de l'item, non null
	 * @param amount
	 *            La quantité d'item, non null
	 * @param damage
	 *            La durabilité de l'item, non null
	 * @param name
	 *            Le nom de l'item, peut être null
	 * @return L'item correspondant aux caractéristiques spécifiées, non null
	 */
	public static ItemStack generateItem(Material type, int amount, short damage, String name) {
		ItemStack it = new ItemStack(type, amount, damage);
		ItemMeta im = it.getItemMeta();

		if (name != null) {
			im.setDisplayName(name);
		}
		it.setItemMeta(im);
		return it;
	}

	/**
	 * Génère l'item correspondant aux caractéristiques spécifiées.
	 * 
	 * @param type
	 *            Le materiel de l'item, non null
	 * @param amount
	 *            La quantité d'item, non null
	 * @param damage
	 *            La durabilité de l'item, non null
	 * @param name
	 *            Le nom de l'item, peut être null
	 * @param lores
	 *            La description de l'item, non null
	 * @return L'item correspondant aux caractéristiques spécifiées, non null
	 */
	public static ItemStack generateItem(Material type, int amount, short damage, String name, List<String> lores) {
		ItemStack it = new ItemStack(type, amount, damage);
		ItemMeta im = it.getItemMeta();

		if (name != null) {
			im.setDisplayName(name);
		}
		im.setLore(lores);
		it.setItemMeta(im);
		return it;
	}

	/**
	 * Génère l'item correspondant aux caractéristiques spécifiées.
	 * 
	 * @param type
	 *            Le materiel de l'item, non null
	 * @param amount
	 *            La quantité d'item, non null
	 * @param damage
	 *            La durabilité de l'item, non null
	 * @param name
	 *            Le nom de l'item, peut être null
	 * @param lores
	 *            La description de l'item, non null
	 * @param enchantmentMap
	 *            La liste des enchantements de l'item, peut être null
	 * @param itemsFlag
	 *            La liste des flags de l'item, optionnel
	 * @return L'item correspondant aux caractéristiques spécifiées, non null
	 */
	public static ItemStack generateItem(Material type, int amount, short damage, String name, List<String> lores,
			Map<Enchantment, Integer> enchantmentMap, ItemFlag... itemsFlag) {
		ItemStack it = new ItemStack(type, amount, damage);
		ItemMeta im = it.getItemMeta();

		if (name != null) {
			im.setDisplayName(name);
		}
		im.setLore(lores);

		if (enchantmentMap != null) {
			for (Enchantment enchantment : enchantmentMap.keySet()) {
				im.addEnchant(enchantment, enchantmentMap.get(enchantment), true);
			}
		}
		im.addItemFlags(itemsFlag);
		it.setItemMeta(im);
		return it;
	}

	/**
	 * Vérifie si les deux items sont identiques.
	 * 
	 * @param itemStack1
	 *            Le premier item à comparer, non null
	 * @param itemStack2
	 *            Le deuxième item à comparer, non null
	 * @return True si les deux items sont égaux, false sinon
	 */
	public static boolean areEquals(ItemStack itemStack1, ItemStack itemStack2) {
		boolean areEquals = false;

		if (itemStack1 != null && itemStack2 != null) {
			// On clone le premier item et on définit sa quantité à celle du
			// deuxième item
			ItemStack itemTemp = itemStack1.clone();
			itemTemp.setAmount(itemStack2.getAmount());

			// Puis on test l'égalité
			if (itemTemp.equals(itemStack2)) {
				areEquals = true;
			}
		} else if (itemStack1 == null && itemStack2 == null) {
			areEquals = true;
		}
		return areEquals;
	}

	/**
	 * Vérifie si l'item est réparable ou non.
	 * 
	 * @param item
	 *            L'item à vérifier, non null
	 * @return True si l'item est réparable, false sinon
	 */
	public static boolean isRepairable(ItemStack item) {
		if (item != null && item.getType() != Material.AIR) {
			boolean isFound = false;
			int i = 0;

			while (!isFound && i < RepairableItem.values().length) {
				RepairableItem repairableItem = RepairableItem.values()[i];

				if (repairableItem.getType() == item.getType()) {
					isFound = true;
				}
				i++;
			}
			return isFound;
		} else {
			return false;
		}
	}
}
