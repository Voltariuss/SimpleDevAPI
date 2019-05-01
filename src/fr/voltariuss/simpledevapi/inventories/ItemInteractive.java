package fr.voltariuss.simpledevapi.inventories;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.inventory.ItemStack;

/**
 * Classe de gestion des items interactifs
 * 
 * @author Voltariuss
 * @version 1.5.0
 *
 */
public class ItemInteractive extends ItemStack {

	private ArrayList<InventoryItemInteractListener> listeners = new ArrayList<>();

	/**
	 * Constructeur
	 * 
	 * @param itemStack
	 *            L'item à définir comme étant interactif, non null
	 * @param listeners
	 *            La liste des items interactifs, optionnel
	 */
	public ItemInteractive(ItemStack itemStack, InventoryItemInteractListener... listeners) {
		super(itemStack);
		this.setListeners(new ArrayList<>(Arrays.asList(listeners)));
	}

	/**
	 * @return La liste des listeners de l'item, non null
	 */
	public ArrayList<InventoryItemInteractListener> getListeners() {
		return listeners;
	}

	/**
	 * Définit la liste des listeners de l'item.
	 * 
	 * @param listeners
	 *            La liste des listeners à associer à l'item, non null
	 */
	private void setListeners(ArrayList<InventoryItemInteractListener> listeners) {
		this.listeners = listeners;
	}

	@Override
	public ItemInteractive clone() {
		ItemInteractive item = (ItemInteractive) super.clone();
		item.setListeners(new ArrayList<>(this.getListeners()));
		return item;
	}
}
