package fr.voltariuss.simpledevapi;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Manager class for NMS
 * 
 * @author Voltariuss
 *
 */
public class NMSManager {
	
	public static final String NMS_VERSION = Bukkit.getServer().getClass().getPackage().getName().substring(23);
	
	/**
	 * Invokes the getHandle() method on the player's CraftPlayer instance to
	 * retrieve the EntityPlayer representation of the player as an Object to
	 * avoid package version change issues
	 * 
	 * @param player
	 *            the player to cast
	 * @return the NMS EnityPlayer representation of the player
	 */
	public static Object getCraftPlayer(Player player) {
		try {
			return Class.forName("org.bukkit.craftbukkit." + NMS_VERSION + ".entity.CraftPlayer").getMethod("getHandle")
					.invoke(player);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException | ClassNotFoundException e) {
			throw new Error(e);
		}
	}

	/**
	 * @param nmsClassString The name of the class to reflect
	 * @return The NMS Class corresponding to the name specified, not null
	 * @throws ClassNotFoundException If the class name is incorrect
	 */
	public static Class<?> getNMSClass(String nmsClassString) throws ClassNotFoundException {
	    String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";
	    String name = "net.minecraft.server." + version + nmsClassString;
	    Class<?> nmsClass = Class.forName(name);
	    return nmsClass;
	}
	
	/**
	 * @param player The targeted player, not null
	 * @return The player connection, not null
	 * @throws SecurityException If an error occur
	 * @throws NoSuchMethodException If an error occur
	 * @throws NoSuchFieldException If an error occur
	 * @throws IllegalArgumentException If an error occur
	 * @throws IllegalAccessException If an error occur
	 * @throws InvocationTargetException If an error occur
	 */
	public static Object getConnection(Player player) throws SecurityException, NoSuchMethodException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
	    Method getHandle = player.getClass().getMethod("getHandle");
	    Object nmsPlayer = getHandle.invoke(player);
	    Field conField = nmsPlayer.getClass().getField("playerConnection");
	    Object con = conField.get(nmsPlayer);
	    return con;
	}
}
