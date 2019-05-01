package fr.voltariuss.simpledevapi.inventories;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Classe de gestion d'un inventaire interactif
 * 
 * @author Voltariuss
 * @version 1.5.0
 *
 */
public class InteractiveInventory {

	private Inventory inventory;
	private ArrayList<ItemStack> notAllowedItems = new ArrayList<>();
	private HashMap<Integer, ItemInteractive> inventoryItemMap;
	private boolean isEditable, isReversed;

	/**
	 * Constructeur
	 * 
	 * @param inventoryItemMap
	 *            La liste des items de l'inventaire associés chacun à un numéro de
	 *            slot unique, non null
	 * @param size
	 *            La taille de l'inventaire
	 * @param title
	 *            Le titre de l'inventaire, non null
	 * @param isEditable
	 *            True si l'inventaire peut être modifié par le joueur, false sinon
	 */
	public InteractiveInventory(HashMap<Integer, ItemInteractive> inventoryItemMap, int size, String title,
			boolean isEditable) {
		this.setInventoryItemMap(inventoryItemMap);
		this.setEditable(isEditable);
		this.createInventory(size, title);
	}

	/**
	 * Constructeur d'un inventaire interactif éditable
	 * 
	 * @param inventoryItemMap
	 *            La liste des items de l'inventaire associés chacun à un numéro de
	 *            slot unique, non null
	 * @param size
	 *            La taille de l'inventaire
	 * @param title
	 *            Le titre de l'inventaire, non null
	 * @param notAllowedItems
	 *            La liste des items non-autorisés, non null
	 * @param isReversed
	 *            True si la liste des items non-autorisés doit être inversée (c'est
	 *            à dire qu'ils correspondent aux seuls items autorisés), false
	 *            sinon
	 */
	public InteractiveInventory(HashMap<Integer, ItemInteractive> inventoryItemMap, int size, String title,
			ArrayList<ItemStack> notAllowedItems, boolean isReversed) {
		this(inventoryItemMap, size, title, true);
		this.setNotAllowedItems(notAllowedItems, isReversed);
	}

	/**
	 * Créer l'inventaire interactif.
	 * 
	 * @param size
	 *            La taille de l'inventaire
	 * @param title
	 *            Le titre de l'inventaire, non null
	 */
	private void createInventory(int size, String title) {
		this.setInventory(Bukkit.createInventory(null, size, title));

		for (Integer i : this.getInventoryItemMap().keySet()) {
			ItemInteractive item = this.getInventoryItemMap().get(i);
			this.getInventory().setItem(i, item);
		}
	}

	/**
	 * Ouvre l'inventaire interactif au joueur ciblé.
	 * 
	 * @param target
	 *            Le joueur ciblé, non null
	 */
	public void openInventory(Player target) {
		if (target.getOpenInventory() != null) {
			target.closeInventory();
		}
		InventoryInteractListener.addInteractiveInventory(target, this);
		target.openInventory(this.getInventory());
	}

	/**
	 * Méthode appelée lorsqu'une interaction est effectuée avec un item de
	 * l'inventaire.<br>
	 * <br>
	 * Vérifie si l'item est interactif. Pour cela, on vérifie si l'inventaire est
	 * interactif et si c'est le cas, on retourne "vrai".<br>
	 * Sinon on regarde si l'item possède au moins un listener. Si il n'en possède
	 * pas, on retourne "faux".<br>
	 * Dans le cas où il en possède au moins un, on les exécute tous et on retourne
	 * "vrai".
	 * 
	 * @param target
	 *            Le joueur ayant interagit avec l'inventaire, non null
	 * @param slot
	 *            L'emplacement de l'item interactif
	 * @param click
	 *            Le clic effectué sur l'item, non null
	 * @return True si l'item est interactif et bloque donc l'event
	 *         {@link InventoryInteractEvent}
	 */
	public boolean onInteract(Player target, int slot, ClickType click) {
		ItemInteractive itemInteractive = this.getInventoryItemMap().containsKey(slot)
				? this.getInventoryItemMap().get(slot)
				: null;
		boolean isInteractiveItem = !this.isEditable();

		if (itemInteractive != null && (isInteractiveItem || !itemInteractive.getListeners().isEmpty())) {
			InventoryItemInteractEvent event = new InventoryItemInteractEvent(target, this, itemInteractive, slot,
					click);

			for (InventoryItemInteractListener listener : itemInteractive.getListeners()) {
				if (listener != null) {
					listener.onInventoryItemClick(event);
				}
			}
			isInteractiveItem = true;
		}
		return isInteractiveItem;
	}

	/**
	 * @return L'inventaire interactif, non null
	 */
	public Inventory getInventory() {
		return inventory;
	}

	/**
	 * D�finit l'inventaire associé à l'instance.
	 * 
	 * @param inventory
	 *            L'inventaire à associer à l'instance, non null
	 */
	private void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	/**
	 * @return La liste des items non-autorisés, non null
	 */
	public ArrayList<ItemStack> getNotAllowedItems() {
		return notAllowedItems;
	}

	/**
	 * Définit la liste des items non-autorisés.
	 * 
	 * @param notAllowedItems
	 *            La liste des items non-autorisés, peut être null
	 * @param isReversed
	 *            True si la liste des items non-autorisés doit être inversée (c'est
	 *            à dire qu'ils correspondent aux seuls items autorisés), false
	 *            sinon
	 */
	private void setNotAllowedItems(ArrayList<ItemStack> notAllowedItems, boolean isReversed) {
		if (notAllowedItems != null) {
			this.notAllowedItems = notAllowedItems;
			this.isReversed = isReversed;
		}
	}

	/**
	 * @return True si la liste des items non-autorisés doit être inversée (c'est à
	 *         dire qu'ils correspondent aux seuls items autorisés), false sinon
	 */
	public boolean isReversed() {
		return isReversed;
	}

	/**
	 * @return La liste des items de l'inventaire associés chacun à un numéro de
	 *         slot unique, non null
	 */
	public HashMap<Integer, ItemInteractive> getInventoryItemMap() {
		return inventoryItemMap;
	}

	/**
	 * Définit la liste des items associés à un emplacement unique.
	 * 
	 * @param inventoryItemMap
	 *            La liste des items de l'inventaire à associer chacun à un numéro
	 *            de slot unique, non null
	 */
	private void setInventoryItemMap(HashMap<Integer, ItemInteractive> inventoryItemMap) {
		this.inventoryItemMap = inventoryItemMap;
	}

	/**
	 * @return True si l'inventaire est éditable, false sinon
	 */
	public boolean isEditable() {
		return isEditable;
	}

	/**
	 * Définit si l'inventaire est éditable ou non.<br>
	 * <br>
	 * Si l'inventaire est éditable, cela signifie qu'il est possible de modifier
	 * les slots ne comportant pas d'items interactif correspondant à des instances
	 * de la classe {@link ItemInteractive} qui possèdent au moins un listener.
	 * 
	 * @param isEditable
	 *            True si l'inventaire est éditable, false sinon
	 */
	private void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}
}
