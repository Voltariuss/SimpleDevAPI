package fr.voltariuss.simpledevapi;

import java.util.Arrays;
import java.util.HashMap;

import org.apache.commons.lang.text.StrSubstitutor;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Classe de gestion des fichiers de configuration
 *
 * @author Voltariuss
 * @version 1.5.1
 *
 */
public class ConfigManager {

    /**
     * Récupère et retourne le message du fichier de configuration associé au plugin
     * correspondant à la classe spécifiée selon le chemin passé en argument de la
     * fonction.
     *
     * @param pluginClass La classe du plugin ayant le fichier de configuration à
     *                    lire, non null
     * @param path        Le chemin vers le message à récupérer, non null
     * @return Le message récupéré du fichier de configuration spécifié au chemin
     *         indiqué
     */
    private static String getMessage(Class<? extends JavaPlugin> pluginClass, String path) {
        return JavaPlugin.getPlugin(pluginClass).getConfig().getString(path);
    }

    /**
     * Récupère et retourne le message du fichier de configuration associé au plugin
     * correspondant à la classe spécifiée selon le chemin passé en argument de la
     * fonction étant relatif à celui des messages de la commande spécifiée.
     *
     * @param pluginClass La classe du plugin ayant le fichier de configuration à
     *                    lire, non null
     * @param cmdLabel    Le nom de la commande associée, non null
     * @param subPath     Le chemin vers le message à récupérer relatif à celui de
     *                    la commande spécifiée, non null
     * @return Le message récupéré du fichier de configuration spécifié au chemin
     *         indiqué
     */
    public static String getCommandMessage(Class<? extends JavaPlugin> pluginClass, String cmdLabel, String subPath) {
        return getMessage(pluginClass, "messages.commands." + cmdLabel + '.' + subPath);
    }

    /**
     * Récupère et retourne le message du fichier de configuration associé au plugin
     * correspondant à la classe spécifiée selon le chemin passé en argument de la
     * fonction étant relatif à celui des messages de la commande spécifiée. Les
     * valeurs entres "{}" sont remplacées par celles spécifiées sous la forme d'une
     * Map.
     *
     * @param pluginClass La classe du plugin ayant le fichier de configuration à
     *                    lire, non null
     * @param cmdLabel    Le nom de la commande associée, non null
     * @param subPath     Le chemin vers le message à récupérer relatif à celui de
     *                    la commande spécifiée, non null
     * @param values      La liste des valeurs à remplacer dans le message récupéré,
     *                    non null
     * @return Le message récupéré du fichier de configuration spécifié au chemin
     *         indiqué
     */
    public static String getCommandMessage(Class<? extends JavaPlugin> pluginClass, String cmdLabel, String subPath,
            HashMap<String, String> values) {
        String configMessage = getCommandMessage(pluginClass, cmdLabel, subPath);
        return StrSubstitutor.replace(configMessage, values, "{", "}");
    }

    public static String getDatabaseInfo(Class<? extends JavaPlugin> pluginClass, String info) {
        return JavaPlugin.getPlugin(pluginClass).getConfig()
                .getString(String.join(".", Arrays.asList("database", info)));
    }
}
