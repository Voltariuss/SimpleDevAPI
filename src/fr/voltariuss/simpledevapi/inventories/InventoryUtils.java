package fr.voltariuss.simpledevapi.inventories;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import fr.voltariuss.simpledevapi.UtilsAPI;
import fr.voltariuss.simpledevapi.items.ItemUtils;

/**
 * Classe utilitaire pour les inventaires
 * 
 * @author Voltariuss
 * @version 1.5.0
 *
 */
public class InventoryUtils {

	/**
	 * @return L'item interactif permettant au joueur de fermer l'inventaire,
	 *         non null
	 */
	public static ItemInteractive getExitItem() {
		ItemInteractive item = new ItemInteractive(ItemUtils.generateItem(Material.REDSTONE, 1, (short) 0,
				ChatColor.RED + UtilsAPI.INVENTORY_INDICATION_TO_EXIT));
		item.getListeners().add(new InventoryItemInteractListener() {

			@Override
			public void onInventoryItemClick(InventoryItemInteractEvent event) {
				event.getPlayer().closeInventory();
			}
		});
		return item;
	}

	/**
	 * @param listener
	 *            L'action à effectuer permettant de changer d'inventaire, non
	 *            null
	 * @return L'item permettant au joueur de changer de menu, non null
	 */
	public static ItemInteractive getBackItem(InventoryItemInteractListener listener) {
		ItemInteractive item = new ItemInteractive(ItemUtils.generateItem(Material.ARROW, 1, (short) 0,
				ChatColor.YELLOW + UtilsAPI.INVENTORY_INDICATION_TO_GO_BACK));
		item.getListeners().add(listener);
		return item;
	}
}
