package fr.voltariuss.simpledevapi.inventories;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

/**
 * Classe de gestion d'un event activé lors d'une interaction avec un item
 * interactif dans un inventaire
 * 
 * @author Voltariuss
 * @version 1.5.0
 *
 */
public class InventoryItemInteractEvent {

	private Player player;
	private InteractiveInventory interactiveInventory;
	private ItemInteractive inventoryItem;
	private int slot;
	private ClickType clickType;

	/**
	 * Constructeur
	 * 
	 * @param player
	 *            Le joueur associé à l'event, non null
	 * @param interactiveInventory
	 *            L'inventaire interactif, non null
	 * @param inventoryItem
	 *            L'item interactif, non null
	 * @param slot
	 *            Le numéro de slot correspondant
	 * @param clickType
	 *            Le type de clic effectué lors de l'interaction, non null
	 */
	public InventoryItemInteractEvent(Player player, InteractiveInventory interactiveInventory,
			ItemInteractive inventoryItem, int slot, ClickType clickType) {
		setPlayer(player);
		setInteractiveInventory(interactiveInventory);
		setInventoryItem(inventoryItem.clone());
		setSlot(slot);
		setClickType(clickType);
	}

	/**
	 * @return Le joueur associé à l'event, non null
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Définit le joueur associé à l'event.
	 * 
	 * @param player
	 *            Le joueur à associer à l'event, non null
	 */
	private void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * @return L'inventaire interactif associé à l'event, non null
	 */
	public InteractiveInventory getInteractiveInventory() {
		return interactiveInventory;
	}

	/**
	 * Définit l'inventaire interactif associé à l'event.
	 * 
	 * @param interactiveInventory
	 *            L'inventaire interactif à associer à l'event, non null
	 */
	private void setInteractiveInventory(InteractiveInventory interactiveInventory) {
		this.interactiveInventory = interactiveInventory;
	}

	/**
	 * @return L'item interactif associé à l'event, non null
	 */
	public ItemInteractive getInventoryItem() {
		return inventoryItem;
	}

	/**
	 * Définit l'item interactif associé à l'event.
	 * 
	 * @param inventoryItem
	 *            L'item interactif à associer à l'event, non null
	 */
	private void setInventoryItem(ItemInteractive inventoryItem) {
		this.inventoryItem = inventoryItem;
	}

	/**
	 * @return L'emplacement de l'item dans l'inventaire
	 */
	public int getSlot() {
		return slot;
	}

	/**
	 * Définit l'emplacement de l'item dans l'inventaire.
	 * 
	 * @param slot
	 *            L'emplacement de l'item dans l'inventaire
	 */
	private void setSlot(int slot) {
		this.slot = slot;
	}

	/**
	 * @return Le type de clic effectué sur l'item, non null
	 */
	public ClickType getClickType() {
		return clickType;
	}

	/**
	 * Définit le type de clic effectué sur l'item.
	 * 
	 * @param clickType
	 *            Le type de clic effectué sur l'item, non null
	 */
	private void setClickType(ClickType clickType) {
		this.clickType = clickType;
	}
}
