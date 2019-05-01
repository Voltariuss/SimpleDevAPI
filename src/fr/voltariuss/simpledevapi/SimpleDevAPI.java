package fr.voltariuss.simpledevapi;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import fr.voltariuss.simpledevapi.entities.CreatureSpawnListener;
import fr.voltariuss.simpledevapi.inventories.InventoryInteractListener;
import fr.voltariuss.simpledevapi.sql.SQLConnection;

/**
 * Main class of the plugin
 * 
 * @author Voltariuss
 * @version 1.0.0
 *
 */
public class SimpleDevAPI extends JavaPlugin {

	@Override
	public void onEnable() {
		saveDefaultConfig();
		
		SQLConnection.getInstance().connect(this.getConfig().getString("host"), this.getConfig().getString("database"),
				this.getConfig().getString("user"), this.getConfig().getString("password"));

		Bukkit.getPluginManager().registerEvents(new InventoryInteractListener(), this);
		Bukkit.getPluginManager().registerEvents(new CreatureSpawnListener(), this);
		
		UtilsAPI.sendActivationMessage(this.getClass(), true);
	}
	
	@Override
	public void onDisable() {
		SQLConnection.getInstance().disconnect();
		UtilsAPI.sendActivationMessage(this.getClass(), false);
	}
}
