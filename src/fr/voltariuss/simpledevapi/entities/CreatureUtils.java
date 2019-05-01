package fr.voltariuss.simpledevapi.entities;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

/**
 * Classe utilitaire de la création d'entités vivantes
 * 
 * @author Voltariuss
 * @version 1.5.0
 *
 */
public class CreatureUtils {

	/**
	 * Spawn une créature customisée.
	 * 
	 * @param location
	 *            La postion à laquelle la créature doit apparaître, non null
	 * @param entityType
	 *            Le type de l'entité à faire apparaître, non null
	 * @return L'entité spécifiée apparut à la position indiquée, non null
	 */
	public static LivingEntity spawnCreature(Location location, EntityType entityType) {
		return (LivingEntity) location.getWorld().spawnEntity(location, entityType);
	}

	/**
	 * Spawn une créature customisée.
	 * 
	 * @param location
	 *            La postion à laquelle la créature doit apparaître, non null
	 * @param entityType
	 *            Le type de l'entité à faire apparaître, non null
	 * @param customName
	 *            Le nom de l'entité, non null
	 * @param isCustomNameVisible
	 *            True si le nom doit être visible, false sinon
	 * @return L'entité spécifiée apparut à la position indiquée, non null
	 */
	public static LivingEntity spawnCreature(Location location, EntityType entityType, String customName,
			boolean isCustomNameVisible) {
		LivingEntity entity = spawnCreature(location, entityType);
		entity.setCustomName(customName);
		entity.setCustomNameVisible(isCustomNameVisible);
		return entity;
	}
}
