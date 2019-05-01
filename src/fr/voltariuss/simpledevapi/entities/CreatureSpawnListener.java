package fr.voltariuss.simpledevapi.entities;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

/**
 * Classe de gestion de l'event d'apparition d'une créature
 * 
 * @author Voltariuss
 * @version 1.5.0
 *
 */
public class CreatureSpawnListener implements Listener {

	/**
	 * Permet de faire spawn n'importe quel mob n'importe où généré par un
	 * plugin.
	 * 
	 * @param event
	 *            Event d'apparition d'une création, non null
	 */
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public void onCustomPNJSpawn(CreatureSpawnEvent event) {
		if (event.getSpawnReason() == SpawnReason.CUSTOM) {
			event.setCancelled(false);
		}
	}
}
