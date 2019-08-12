package fr.voltariuss.simpledevapi;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Classe répertoriant la liste des messages utilisés par le plugin
 * 
 * @author Voltariuss
 * @version 1.5.0
 *
 */
public class UtilsAPI {

	/**
	 * @param pluginClass
	 *            La classe principale du plugin correspondant, non null
	 * @return Le préfixe du plugin correspondant, non null
	 */
	public static String getPluginPrefix(Class<?> pluginClass) {
		return String.format("§4[§c%s§4] ", pluginClass.getSimpleName().replace("Dornacraft", "Dornacraft-"));
	}

	/**
	 * Envoie un message d'activation ou de désactivation du plugin spécifié.
	 * 
	 * @param pluginClass
	 *            La classe du plugin concerné, non null
	 * @param isEnabled
	 *            True si le plugin est activé, false sinon
	 */
	public static void sendActivationMessage(Class<?> pluginClass, boolean isEnabled) {
		String pluginName = pluginClass.getSimpleName().replace("Dornacraft", "Dornacraft-");
		String state = (isEnabled ? "§aactivé" : "§cdésactivé");
		Bukkit.getLogger()
				.fine(String.format("%s§e~ §bPlugin §6%s %s", getPluginPrefix(pluginClass), pluginName, state));
	}

	/**
	 * Envoie un message à la cible spécifiée. Remplace toutes les suites de
	 * caractères "§r" (ChatColor.RESET) par la couleur du message associé au
	 * niveau de message spécifié.
	 * 
	 * @param messageLevel
	 *            Le niveau du message, non null
	 * @param target
	 *            Le destinataire, non null
	 * @param message
	 *            Le message à envoyer, non null
	 * @param args
	 *            La liste des arguments du message, optionnel
	 */
	public static void sendSystemMessage(MessageLevel messageLevel, CommandSender target, String message,
			Object... args) {
		target.sendMessage(String.format(messageLevel.getPrefix()
				+ message.replaceAll(ChatColor.RESET.toString(), messageLevel.getMsgColor().toString()), args));
	}

	/////
	// Caractères spéciaux
	/////

	public static final String CARAC_ARROW = "➳";
	public static final String CARAC_DOUBLE_QUOTE_END = "»";

	/////
	// Préfixes
	/////

	// Préfixes de connexion et de déconnexion d'un joueur
	public static final String PLAYER_CONNECTION_PREFIX = " §6[§a+§6] §e";
	public static final String PLAYER_DISCONNECTION_PREFIX = " §6[§c-§6] §e";

	// Préfixes des messages
	public static final String PREFIX_DORNACRAFT_MESSAGE = "§7[§c§lD§4§lC§7] ";
	public static final String PREFIX_WARNING_MESSAGE = "§7[§4§l!§7] ";
	public static final String PREFIX_ERROR_MESSAGE = "§4§lErreur : ";

	/////
	// Messages
	/////

	// SQL
	public static final String SQL_CONNECTION_ATTEMPT = "§eTentative de connexion à la base de données...";
	public static final String SQL_CONNECTION_FAILED = "Connexion impossible à la base de données !";
	public static final String SQL_NO_CONNECTION = "Le plugin n'est connecté à aucune base de données. Certaines fonctionnalités ont été bridées ou désactivées.";

	// Commandes
	public static final String COMMAND_HELP_HEADER = "§6===== [§aAide - Commande /%s§6] =====";
	public static final String COMMAND_HELP_CHANGE_PAGE = "§cPour changer de page : §6/%s help §b[page]";
	public static final String COMMAND_HELP_MESSAGE = "§ePour toute aide : §6/%s help §b[page]";
	public static final String COMMAND_HELP_TRY_MESSAGE = "§eEssayez : %s";
	public static final String COMMAND_HELP_DESCRIPTION = "Aide sur les différentes commandes";

	// Inventaires
	public static final String INVENTORY_INDICATION_TO_ACCESS_MENU = "Clique pour accéder au menu";
	public static final String INVENTORY_INDICATION_TO_OPEN = "Clique pour ouvrir";
	public static final String INVENTORY_INDICATION_TO_EXIT = "Quitter le menu";
	public static final String INVENTORY_INDICATION_TO_GO_BACK = "Retour au menu précédent";
	public static final String INVENTORY_INDICATION_TO_TELEPORT = "Clique pour te téléporter";
	public static final String INVENTORY_INDICATION_TO_ACTIVATE = "Clique pour activer";

	/////
	// Messages d'erreur
	/////

	// Serveur
	public static final String CONNECTION_BLOCKED = "Connexion au serveur interrompue : une erreur interne est survenue. Veuillez réessayer."
			+ "\n\nSi le problème persiste, contactez le staff dans les plus brefs délais via notre forum §6(forum.dornacraft.fr) §cou notre discord §b(discord.dornacraft.fr)§c.";
	public static final String INTERNAL_EXCEPTION = "Une erreur interne est survenue. S'il vous plait, veuillez contacter un administrateur ou un développeur dans les plus brefs délais.";

	// Joueur
	public static final String PLAYER_UNKNOW = "Le joueur est inexistant.";
	public static final String PLAYER_NOT_FOUND = "Le joueur est introuvable (déconnecté ou inexistant).";
	public static final String PLAYER_NOT_YOURSELF = "Le joueur ciblé ne peut être vous-même.";
	public static final String PLAYER_OFFLINE = "Le joueur ciblé est déconnecté.";

	// Console
	public static final String CONSOLE_NOT_ALLOWED = "Seul un joueur connecté peut effectuer ceci.";

	// Système
	public static final String NUMBER_FORMAT_INVALIDE = "Le nombre spécifié n'est pas valide.";
	public static final String NUMBER_MUST_BE_GREATER_THAN = "Le nombre spécifié doit être strictement supérieur à %s.";
	public static final String NUMBER_MUST_BE_GREATER_OR_EQUAL_THAN = "Le nombre spécifié doit être supérieur ou égal à %s.";
	public static final String NUMBER_MUST_BE_LESS_THAN = "Le nombre spécifié doit être strictement inférieur à %s.";
	public static final String NUMBER_MUST_BE_LESS_OR_EQUAL_THAN = "Le nombre spécifié doit être inférieur ou égal à %s.";
	public static final String NUMBER_MUST_BE_NEGATIVE = "Le nombre spécifié doit être négatif.";
	public static final String NUMBER_MUST_BE_POSITIVE = "Le nombre spécifié doit être positif.";
	public static final String NUMBER_MUST_BE_STRICTLY_NEGATIVE = "Le nombre spécifié doit être strictement négatif.";
	public static final String NUMBER_MUST_BE_STRICTLY_POSITIVE = "Le nombre spécifié doit être strictement positif.";
	public static final String NUMBER_MUST_IN_INTERVAL = "Le nombre spécifié doit être compris entre %s et %s.";

	// Inventaire
	public static final String INVENTORY_FULL = "Votre inventaire est plein !";
	public static final String INVENTORY_INSUFFICIENT_ITEMS = "Quantité d'items insuffisante !";
	public static final String INVENTORY_NOT_ALLOWED_ITEM = "Cet item ne peut être placé dans cet inventaire.";

	// Commands
	public static final String COMMAND_NOT_ENOUGH_ARGUMENTS = "Pas assez d'arguments spécifiés.";
	public static final String COMMAND_TOO_MANY_ARGUMENTS = "Trop d'arguments spécifiés.";
	public static final String COMMAND_INPUT_ARGUMENT_WRONG = "Un des arguments saisis est incorrect.";
	public static final String COMMAND_WRONG = "La commande est incorrecte.";
	public static final String COMMAND_NOT_OPERATIONAL = "Commande non-opérationnelle";
	public static final String COMMAND_CONSISTENCY_ARGUMENTS_EXCEPTION = "Un argument optionnel ne peut précéder un argument obligatoire";

	// Others
	public static final String CONSOLE_NAME = "CONSOLE";
	public static final String PERMISSION_MISSING = "Vous n'avez pas la permission nécessaire.";
}
