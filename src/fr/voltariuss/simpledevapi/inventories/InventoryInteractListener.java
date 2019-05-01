package fr.voltariuss.simpledevapi.inventories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.voltariuss.simpledevapi.MessageLevel;
import fr.voltariuss.simpledevapi.UtilsAPI;

/**
 * Classe de gestion des listeners des inventaires interactifs
 * 
 * @author Voltariuss
 * @version 1.5.0
 *
 */
public class InventoryInteractListener implements Listener {

	private static final HashMap<Player, InteractiveInventory> interactiveInventoryMap = new HashMap<>();

	/**
	 * @return La liste des inventaires interactifs associés chacun à un joueur,
	 *         non null
	 */
	private static HashMap<Player, InteractiveInventory> getInteractiveInventoryMap() {
		return interactiveInventoryMap;
	}

	/**
	 * Ajoute une association entre un inventaire interactif et un joueur dans
	 * la liste.
	 * 
	 * @param player
	 *            Le joueur associé à l'inventaire interactif, non null
	 * @param interactiveInventory
	 *            L'inventaire interactif, non null
	 */
	public static void addInteractiveInventory(Player player, InteractiveInventory interactiveInventory) {
		getInteractiveInventoryMap().put(player, interactiveInventory);
	}

	/**
	 * Retire l'inventaire interactif associé avec le joueur de la liste.
	 * 
	 * @param player
	 *            Le joueur associé à l'inventaire interactif, non null
	 */
	public static void removeInteractiveInventory(Player player) {
		getInteractiveInventoryMap().remove(player);
	}

	/**
	 * Vérifie si l'item spécifié est autorisé dans l'inventaire interactif ou
	 * non.
	 * 
	 * @param interactiveInventory
	 *            L'inventaire interactif correspondant, non null
	 * @param itemStack
	 *            L'item à tester, non null
	 * @return True si l'item est autorisé à être placé dans l'inventaire
	 *         spécifié, false sinon
	 */
	public static boolean isAllowedItem(InteractiveInventory interactiveInventory, ItemStack itemStack) {
		ArrayList<ItemStack> itemStacks = interactiveInventory.getNotAllowedItems();
		Iterator<ItemStack> iterator = itemStacks.iterator();
		boolean isAllowedItem = !interactiveInventory.isReversed();

		while (iterator.hasNext() && isAllowedItem == !interactiveInventory.isReversed()) {
			ItemStack it = new ItemStack(iterator.next());
			it.setAmount(itemStack.getAmount());

			if (it.equals(itemStack)) {
				isAllowedItem = !isAllowedItem;
			}
		}
		return isAllowedItem;
	}

	/**
	 * Traite les clics sur les items interactifs dans l'inventaire interactif
	 * correspondant
	 * 
	 * @param event
	 *            L'event concernant un clic dans l'inventaire en question, non
	 *            null
	 */
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onInventoryInteract(InventoryClickEvent event) {
		if (event.getInventory().getType() != InventoryType.CRAFTING) {
			Player player = (Player) event.getWhoClicked();
			ClickType clickType = event.getClick();

			if (clickType == ClickType.NUMBER_KEY) {
				event.setCancelled(true);
			} else if (getInteractiveInventoryMap().containsKey(player)) {
				InteractiveInventory interactiveInventory = getInteractiveInventoryMap().get(player);

				if (interactiveInventory.getInventory().getName().equals(event.getInventory().getName())) {
					Inventory inventory = interactiveInventory.getInventory();
					boolean isInInteractiveInventory = event.getRawSlot() < inventory.getSize()
							&& event.getRawSlot() >= 0;
					boolean isShiftClick = clickType == ClickType.SHIFT_LEFT || clickType == ClickType.SHIFT_RIGHT;

					if (isInInteractiveInventory || isShiftClick) {
						if (!interactiveInventory.isEditable()) {
							if (isInInteractiveInventory) {
								// L'événement est annulé, et on vérifie si
								// l'item est associé à un listener
								event.setCancelled(
										interactiveInventory.onInteract(player, event.getRawSlot(), event.getClick()));
							} else { // Uniquement dans le cas où il s'agit d'un
										// shift-click
								event.setCancelled(true);
							}
						} else { // On se trouve dans le cas où l'inventaire est
									// éditable, mais pouvant comporter
									// des items interagissables
							// On récupère l'item associé au clic effectué en
							// fonction des clics
							ItemStack item = isShiftClick ? event.getCurrentItem() : event.getCursor();

							if (item != null && item.getType() != Material.AIR) {
								if (!isAllowedItem(interactiveInventory, item)) {
									// Si l'item n'est pas autorisé à être placé
									// dans l'inventaire interactif
									event.setCancelled(true);
									UtilsAPI.sendSystemMessage(MessageLevel.ERROR, player,
											UtilsAPI.INVENTORY_NOT_ALLOWED_ITEM);
								} else {
									event.setCancelled(interactiveInventory.onInteract(player, event.getRawSlot(),
											event.getClick()));
								}
							} else {
								event.setCancelled(
										interactiveInventory.onInteract(player, event.getRawSlot(), event.getClick()));
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Traite les tentatives de placement de plusieurs items par pression
	 * prolongée de la touche de placement des items dans l'inventaire
	 * interactif
	 * 
	 * @param event
	 *            L'event concernant le drag d'items dans l'inventaire en
	 *            question, non null
	 */
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onDragItem(InventoryDragEvent event) {
		Player player = (Player) event.getWhoClicked();

		if (getInteractiveInventoryMap().containsKey(player)) {
			InteractiveInventory interactiveInventory = getInteractiveInventoryMap().get(player);
			ItemStack oldCursor = event.getOldCursor();

			if (oldCursor != null) {
				for (Integer i : event.getRawSlots()) {
					if (i < event.getInventory().getSize()) {
						if (!interactiveInventory.isEditable()) {
							// On annule si l'inventaire n'est pas éditable
							event.setCancelled(true);
							break;
						} else if (!isAllowedItem(interactiveInventory, oldCursor)) {
							// On annule si l'item en question n'est pas
							// autorisé dans l'inventaire
							event.setCancelled(true);
							UtilsAPI.sendSystemMessage(MessageLevel.ERROR, player, UtilsAPI.INVENTORY_NOT_ALLOWED_ITEM);
							break;
						}
					}
				}
			}
		}
	}

	/**
	 * Supprime l'inventaire interactif de la mémoire cache lorsque le joueur
	 * ferme l'inventaire
	 * 
	 * @param event
	 *            L'event correspondant à la fermeture d'un inventaire
	 *            interactif, non null
	 */
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
	public void onInventoryClose(InventoryCloseEvent event) {
		if (getInteractiveInventoryMap().containsKey(event.getPlayer())) {
			removeInteractiveInventory((Player) event.getPlayer());
		}
	}
}
