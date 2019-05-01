package fr.voltariuss.simpledevapi.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * Classe de gestion d'un exécuteur de commande
 * 
 * @author Voltariuss
 * @version 1.5.0
 *
 */
public interface DornacraftCommandExecutor {

	/**
	 * Lance l'exécution de la commande.
	 * 
	 * @param sender
	 *            L'émetteur de la commande, non null
	 * @param cmd
	 *            La commande Bukkit correspondante, non null
	 * @param label
	 *            Le label de la commande, non null
	 * @param args
	 *            La liste des arguments de la commande à exécuter, non null
	 * @throws Exception
	 *             Si une erreur est détectée
	 */
	public void execute(CommandSender sender, Command cmd, String label, String[] args) throws Exception;
}
