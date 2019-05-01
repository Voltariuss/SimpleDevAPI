package fr.voltariuss.simpledevapi.inventories;

/**
 * Classe de gestion d'un listener d'items interactifs d'un inventaire
 * 
 * @author Voltariuss
 * @version 1.5.0
 *
 */
public interface InventoryItemInteractListener {

	/**
	 * Méthode appelée lorsqu'une interaction est effectuée avec un item interactif.
	 * 
	 * @param event
	 *            L'event associé avec l'item en question, non null
	 */
	public abstract void onInventoryItemClick(InventoryItemInteractEvent event);
}
