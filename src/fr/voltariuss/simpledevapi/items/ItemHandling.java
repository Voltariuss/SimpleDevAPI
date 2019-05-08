package fr.voltariuss.simpledevapi.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Classe de gestion des échanges d'items dans les inventaires
 * 
 * @author Voltariuss
 * @version 1.5.0
 *
 */
public class ItemHandling {

	/**
	 * Ajoute les items spécifiés dans l'inventaire du joueur ciblé et émet un
	 * son.
	 * 
	 * @param player
	 *            Le joueur ciblé, non null
	 * @param itemStacks
	 *            La liste des items à ajouter à l'inventaire du joueur, non
	 *            vide
	 * @throws InventoryFullException
	 *             Si l'ensemble des slots de l'inventaire ne peuvent stocker la
	 *             totalité des items
	 */
	public static void addItem(Player player, ItemStack... itemStacks) throws InventoryFullException {
		addItem(player.getInventory(), itemStacks);
		player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 1);
	}

	/**
	 * Retire les items spécifiés de l'inventaire du joueur ciblé et émet un
	 * son.
	 * 
	 * @param player
	 *            Le joueur ciblé, non null
	 * @param itemStacks
	 *            La liste des items à retirer de l'inventaire du joueur, non
	 *            vide
	 * @throws InventoryInsufficientItemException
	 *             S'il n'y a pas suffisament d'items dans l'inventaire du
	 *             joueur ciblé
	 */
	public static void removeItem(Player player, ItemStack... itemStacks) throws InventoryInsufficientItemException {
		removeItem(player.getInventory(), itemStacks);
		player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 1);
	}

	/**
	 * Ajoute les items spécifiés dans l'inventaire ciblé.
	 * 
	 * @param inventory
	 *            L'inventaire ciblé, non null
	 * @param itemStacks
	 *            La liste des items à ajouter à l'inventaire du joueur, non
	 *            vide
	 * @throws InventoryFullException
	 *             Si l'ensemble des slots de l'inventaire ne peuvent stocker la
	 *             totalité des items
	 */
	public static void addItem(Inventory inventory, ItemStack... itemStacks) throws InventoryFullException {
		ArrayList<ItemStack> temporaryInventory = new ArrayList<>(Arrays.asList(inventory.getContents()));
		int nbFreeSlots = getNbFreeSlots(inventory);

		// On parcourt l'ensemble des items à ajouter
		for (ItemStack itemStack : itemStacks) {
			int amount = itemStack.getAmount();

			if (amount > 0) {
				int amountSameItems = 0;
				int nbSlotItemOccupied = 0;

				// On parcourt la liste des items contenus dans l'inventaire
				for (ItemStack item : temporaryInventory) {
					if (item != null) {
						int savedAmountItem = item.getAmount();
						item.setAmount(amount);

						if (item.equals(itemStack)) {
							// Actualise le nombre d'items dans l'inventaire et
							// le nombre de slots occupés
							amountSameItems += savedAmountItem;
							nbSlotItemOccupied++;
						}
						item.setAmount(savedAmountItem);
					}
				}
				// On détermine la quantité finale à ajouter dans les slots
				// libres
				int maxItemsInOccuperedSlots = itemStack.getMaxStackSize() * nbSlotItemOccupied;
				int amountItemsCountable = maxItemsInOccuperedSlots - amountSameItems;
				int amountFinal = (amount - amountItemsCountable);

				// On détermine le nombre de slots à occuper
				int nbSlotsItemNeeded = amountFinal <= 0 ? 0 : (amountFinal / itemStack.getMaxStackSize() + 1);

				if (nbSlotsItemNeeded > 0) {
					// On vérifie qu'il y a suffisament de slots libres pour
					// ajouter les items
					if (nbFreeSlots >= nbSlotsItemNeeded) {
						nbFreeSlots -= nbSlotsItemNeeded;
						temporaryInventory.add(itemStack);
					} else {
						throw new InventoryFullException();
					}
				}
			}
		}
		// Une fois l'ajout de tous les items dans l'inventaire temporaire, on
		// transfère
		// son contenu à l'inventaire réel
		inventory.addItem(itemStacks);
	}

	/**
	 * Retire les items spécifiés de l'inventaire ciblé.
	 * 
	 * @param inventory
	 *            L'inventaire ciblé, non null
	 * @param itemStacks
	 *            La liste des items à retirer de l'inventaire, non null
	 * @throws InventoryInsufficientItemException
	 *             Si l'inventaire ne contient pas la totalité des items
	 *             spécifiés
	 */
	public static void removeItem(Inventory inventory, ItemStack... itemStacks)
			throws InventoryInsufficientItemException {
		ArrayList<ItemStack> temporaryInventoryContent = new ArrayList<>(Arrays.asList(inventory.getContents()));

		// On test de retirer la totalité des items spécifiés à la copie de
		// l'inventaire ciblé
		for (ItemStack itemToRemove : itemStacks) {
			int amountToRemove = itemToRemove.getAmount();

			if (amountToRemove > 0) {
				// On récupère le nombre d'items du même type présent dans
				// l'inventaire
				int amountItemsFound = 0;

				for (ItemStack inventoryItem : temporaryInventoryContent) {
					if (inventoryItem != null && ItemUtils.areEquals(inventoryItem, itemToRemove)) {
						amountItemsFound += inventoryItem.getAmount();
					}
				}

				// Si il y a suffisament d'items dans l'inventaire, on peut
				// retirer la quantité
				// d'item correspondante
				if (amountItemsFound >= amountToRemove) {
					temporaryInventoryContent.remove(itemToRemove);
				} else {
					throw new InventoryInsufficientItemException();
				}
			}
		}
		// Le contenu de l'inventaire respecte les conditions, on peut donc lui
		// retirer
		// les items correspondants
		inventory.removeItem(itemStacks);
	}

	/**
	 * Échange les items spécifiés du premier inventaire avec les items
	 * spécifiés du deuxième inventaire.
	 * 
	 * @param firstInventory
	 *            Le premier inventaire ciblé pour l'échange, non null
	 * @param firstItemStacks
	 *            La liste des items du premier inventaire à transférer vers le
	 *            deuxième inventaire, non null
	 * @param secondInventory
	 *            Le deuxième inventaire ciblé pour l'échange, non null
	 * @param secondItemStacks
	 *            La liste des items du deuxième inventaire à transférer vers le
	 *            premier inventaire, non null
	 * @throws InventoryInsufficientItemException
	 *             Si l'un des inventaires ne contient pas la totalité des items
	 *             à envoyer
	 * @throws InventoryFullException
	 *             Si l'ensemble des slots de l'un des inventaires ne peuvent
	 *             stocker la totalité des items à recevoir
	 */
	public static void tradeItem(Inventory firstInventory, List<ItemStack> firstItemStacks, Inventory secondInventory,
			List<ItemStack> secondItemStacks) throws InventoryInsufficientItemException, InventoryFullException {
		ArrayList<ItemStack> savedFirstInventoryContent = new ArrayList<>();
		ArrayList<ItemStack> savedSecondInventoryContent = new ArrayList<>();

		for (ItemStack itemStack : firstInventory.getContents()) {
			savedFirstInventoryContent.add(itemStack.clone());
		}
		for (ItemStack itemStack : secondInventory.getContents()) {
			savedSecondInventoryContent.add(itemStack.clone());
		}
		removeItem(firstInventory, firstItemStacks.toArray(new ItemStack[0]));
		removeItem(secondInventory, secondItemStacks.toArray(new ItemStack[0]));
		addItem(firstInventory, secondItemStacks.toArray(new ItemStack[0]));
		addItem(secondInventory, firstItemStacks.toArray(new ItemStack[0]));
	}

	/**
	 * Détermine et retourne le nombre de slots libres dans l'inventaire.
	 * 
	 * @param inventory
	 *            L'inventaire ciblé, non null
	 * @return Le nombre de slots libres dans l'inventaire
	 */
	public static int getNbFreeSlots(Inventory inventory) {
		int nbAvailableSlots = 0;

		for (ItemStack item : Arrays.asList(inventory.getContents())) {
			if (item == null) {
				nbAvailableSlots++;
			}
		}
		return nbAvailableSlots;
	}
}
