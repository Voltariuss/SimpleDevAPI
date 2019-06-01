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
