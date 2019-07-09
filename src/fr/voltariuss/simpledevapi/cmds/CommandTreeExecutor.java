package fr.voltariuss.simpledevapi.cmds;

import java.util.Arrays;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.google.common.collect.Lists;

import fr.voltariuss.simpledevapi.MessageLevel;
import fr.voltariuss.simpledevapi.UtilsAPI;

/**
 * Classe de gestion d'un arbre d'exécution de commande<br>
 * <br>
 * <b><i>Exemple d'arbre :</i></b>
 *
 * <pre>
 * Commandes :
 * - test arg1
 * - test arg2 arg2.1
 * - test arg2 arg2.2 ...
 *
 *         -- arg1
 *        /
 * test --            -- arg2.1
 *        \          /
 *         -- arg2 --
 *                   \
 *                    -- arg2.2 -- ...
 * </pre>
 *
 * @author Voltariuss
 * @version 1.5.0
 *
 */
public class CommandTreeExecutor {

	private CommandNode root = null;

	/**
	 * Constructeur
	 *
	 * @param cmdLabel
	 *            Le label de la commande, non null
	 */
	public CommandTreeExecutor(String cmdLabel) {
		setRoot(new CommandNode(null, new CommandArgument(cmdLabel), Bukkit.getPluginCommand(cmdLabel).getDescription(),
				null, null));
	}

	/**
	 * Ajoute à l'arbre d'exécution la commande spécifiée en définissant le
	 * parant d'un argument sur le noeud associé à l'argument précédent. Voici
	 * le formalisme à créer pour chaque ajout de commande <b>à effectuer dans
	 * le constructeur de la classe Cmd</b> :
	 *
	 * <pre>
	 * <code>
	 * getCmdTreeExecutor().addSubCommand(new CommandNode(new CommandArgument("help", "?"), DESC_HELP, new DornacraftCommandExecutor() {
	 *
	 *             <span>@Override</span>
	 *             public void execute(CommandSender sender, Command cmd, String label, String[] args) {
	 *                 getCmdTreeExecutor().getRoot().sendHelpMessage(sender, label, 1);
	 *             }
	 *         }, null),
	 *         new CommandNode(new CommandArgument(CommandArgumentType.NUMBER.getCustomArgType("page"), false), "Aide sur la commande", new DornacraftCommandExecutor() {
	 *
	 *             <span>@Override</span>
	 *             public void execute(CommandSender sender, Command cmd, String label, String[] args) throws Exception {
	 *                 getCmdTreeExecutor().getRoot().sendHelpMessage(sender, label, Integer.parseInt(args[1]));
	 *             }
	 *         }, null)
	 *     );
	 * </code>
	 * </pre>
	 *
	 * <b>Détails des instructions :</b>
	 * <ul>
	 * <li><i><b>getCmdTreeExecutor() : </b>Appel de l'arbre d'exécution associé
	 * à l'instance héritante de DornacraftCommand.</i></li>
	 *
	 * <li><i><b>addSubCommand(...) : </b>Appel de la méthode permettant l'ajout
	 * d'une nouvelle commande à l'arbre d'exécution.</i></li>
	 *
	 * <li><i><b>new CommandNode(...) : </b>Créer un nouveau noeud dans l'arbre
	 * d'exécution. Chaque noeud correspond à un argument de la commande dans
	 * l'ordre spécifique.</i></li>
	 *
	 * <li><i><b>new CommandArgument(...) : </b>Définit le label principal de
	 * l'argument suivi éventuellement de ses aliases.</i></li>
	 *
	 * <li><i><b>new CommandArgument(...) avec CommandArgumentType : </b>Définit
	 * un argument de saisie en spécifiant le type d'argument attendu et s'il
	 * est obligatoire ou non</i></li>
	 *
	 * <li><i><b>new DornacraftCommandExecutor() : </b>Définit éventuellement
	 * l'exécuteur associé au noeud (obligatoire pour le dernier noeud de la
	 * commande sinon une exception sera levée)</i></li>
	 *
	 * <li><i><b>Permission spécifique à un noeud : </b>Il est possible de
	 * spécifier un argument spécifique à un noeud (attention : pas de
	 * propagation donc à le faire soit même si nécessaire).
	 * {@link CommandNode#CommandNode(CommandArgument, String, DornacraftCommandExecutor, String)}</i></li>
	 * </ul>
	 *
	 * @param cmds
	 *            La liste des noueuds à ajouter dans l'arbre d'exécution de la
	 *            commande, non null
	 */
	public void addSubCommand(CommandNode... cmds) {
		// On débute l'ajout de la nouvelle commande à partir de la racine de
		// l'arbre
		// d'exécution
		CommandNode currentNode = getRoot();
		Iterator<CommandNode> iterator = Arrays.asList(cmds).iterator();

		// On parcourt la liste des noeuds
		while (iterator.hasNext()) {
			CommandNode subCmdNode = iterator.next();
			// On tente de récupérer le noeud correspondant à celui parcouru
			// actuellement
			// dans la liste
			CommandNode cmdNode = currentNode.getChildWithTypeArg(subCmdNode.getArgument().getType().getLabel());

			// Si on ne l'a pas trouvé, on le créer
			if (cmdNode == null) {
				cmdNode = new CommandNode(currentNode,
						new CommandArgument(subCmdNode.getArgument().getType(), subCmdNode.getArgument().isRequired()),
						subCmdNode.getDescription(), subCmdNode.getExecutor(), subCmdNode.getSpecificPermission());
			}
			// Enfin, on ajoute le noeud à la liste des noeuds fils du noeud
			// courant et
			// actualise ce dernier avec ce nouveau noeud fils
			currentNode.getChilds().add(cmdNode);
			currentNode = cmdNode;
		}

		// On parcourt désormais la liste des noeuds dans l'ordre inverse afin
		// d'en
		// vérifier la cohérence au niveau des arguments obligatoires et
		// optionnels
		while (currentNode.getParent() != null) {
			if (currentNode.getArgument().isRequired() && !currentNode.getParent().getArgument().isRequired()) {
				throw new InvalidCommandException(UtilsAPI.COMMAND_CONSISTENCY_ARGUMENTS_EXCEPTION);
			}
			currentNode = currentNode.getParent();
		}
	}

	/**
	 * Récupère la commande associée et la retourne si elle existe.
	 *
	 * @param args
	 *            La liste des arguments de la commande saisis par l'émetteur,
	 *            non null
	 * @return La commande associée si elle existe, peut être null
	 */
	public CommandNode getCommand(String[] args) throws InvalidCommandException {
		// On parcourt l'arbre à partir de sa racine
		CommandNode currentNode = getRoot();
		Iterator<String> iterator = Arrays.asList(args).iterator();

		// On avance dans l'arbre tant que chaque argument parcouru correspond à
		// un
		// noeud existant et qu'il en reste à traiter
		while (iterator.hasNext() && currentNode != null) {
			String arg = iterator.next();

			if (currentNode.isLeaf()) {
				// Détection d'un nombre d'argument trop important
				throw new InvalidCommandException(currentNode);
			} else if (currentNode.isNextNodeInputArg()) {
				// Si le noeud suivant correspond à un noeud de saisie, alors on
				// récupère
				// directement le premier et unique fils du noeud courant
				currentNode = currentNode.getFirstChild();
			} else {
				// On tente de récupérer le noeud correspondant à l'argument
				// actuel
				currentNode = currentNode.getChildWithTypeArg(arg);
			}
		}
		return currentNode;
	}

	/**
	 * Tente d'exécuter la commande en vérifiant la validité des arguments de
	 * saisie et l'importance des arguments des noeuds fils du noeud à exécuter
	 * s'il ne s'agit pas d'une feuille de l'arbre d'exécution. Traite les
	 * erreurs détectées lors des vérifications si il y en a, sinon lance
	 * l'exécution de la commande.
	 *
	 * @param commandNode
	 *            Le noeud correspondant à la commande à exécuter, non null
	 * @param sender
	 *            L'émetteur de la commande, non null
	 * @param cmd
	 *            La commande Bukkit correspondante, non null
	 * @param label
	 *            Le label de la commande à exécuter, non null
	 * @param args
	 *            La liste des arguments de la commande, non null
	 * @throws Exception
	 *             Si une erreur est détectée
	 */
	public void executeCommand(CommandNode commandNode, CommandSender sender, Command cmd, String label, String[] args)
			throws Exception {
		// On vérifie si la commande est exécutable en vérifiant l'importance
		// des
		// arguments fils
		if (commandNode.isExecutable()) {
			// On inverse la liste pour débuter le parcourt à partir du dernier
			// argument
			// correspondant au noeud à exécuter
			Iterator<String> iterator = Lists.reverse(Arrays.asList(args)).iterator();
			CommandNode currentNode = commandNode;
			boolean areCheckersValid = true;

			// On parcourt les arguments en partant du noeud à exécuter en
			// vérifiant la
			// validité des données saisies dans les arguments de saisi
			while (iterator.hasNext() && areCheckersValid) {
				String arg = iterator.next();

				// On traite seulement le cas où l'argument est une saisie
				// utilisateur
				if (currentNode.getArgument().isInputArg()) {
					// On vérifie si la saisie est conforme
					areCheckersValid = currentNode.getArgument().getType().getChecker().check(arg);
				}
				currentNode = currentNode.getParent();
			}
			if (areCheckersValid) {
				commandNode.getExecutor().execute(sender, cmd, label, args);
			} else {
				UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender, UtilsAPI.COMMAND_INPUT_ARGUMENT_WRONG);
				commandNode.sendSpecificHelpMessage(sender, label);
			}
		} else if (commandNode.getArgument().isInputArg()) {
			// Si le noeud de la commande à exécuter correspond à un argument de
			// saisi,
			// alors il s'agit d'une erreur correspondant à un manque
			// d'arguments
			UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender, UtilsAPI.COMMAND_NOT_ENOUGH_ARGUMENTS);
			commandNode.sendSpecificHelpMessage(sender, label);
		} else {
			commandNode.sendHelpMessage(sender, label, 1);
		}
	}

	/**
	 * Vérifie la validité des arguments de la commande ainsi que la possession
	 * ou non des permissions nécessaires par l'émetteur de la commande. Si
	 * toutes les conditions sont remplies, alors lance la tentative d'exécution
	 * de la commande en faisant appel à la méthode
	 * {@link CommandTreeExecutor#executeCommand(CommandNode, CommandSender, Command, String, String[])}.
	 * Capte et traite toutes les erreurs non traitées.
	 *
	 * @param sender
	 *            L'émetteur de la commande, non null
	 * @param cmd
	 *            Les informations générales de la commande, non null
	 * @param label
	 *            Le label de la commande, non null
	 * @param args
	 *            Les arguments de la commande émise, non null
	 */
	public void checkCommand(CommandSender sender, Command cmd, String label, String[] args) {
		try {
			// On tente de récupérer dans l'arbre d'exécution le noeud
			// correspondant aux
			// arguments passés par l'émetteur de la commande
			CommandNode cmdNode = getCommand(args);

			if (cmdNode != null) {
				// Si la commande est valide, on vérifie la possession des
				// permissions
				// nécessaires par l'émetteur de la commande
				if (sender.hasPermission(cmdNode.getPermission(cmd)) && cmdNode.hasSpecificPermission()
						? sender.hasPermission(cmdNode.getSpecificPermission()) : true) {
					try {
						executeCommand(cmdNode, sender, cmd, label, args);
					} catch (NumberFormatException e) {
                        UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender, UtilsAPI.NUMBER_FORMAT_INVALIDE);
                    } catch (InvalidArgumentsCommandException e) {
                        UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender, UtilsAPI.COMMAND_INPUT_ARGUMENT_WRONG);
                    } catch (DornacraftCommandException e) {
                        UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender, e.getMessage());
					} catch (Exception e) {
						UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender, UtilsAPI.INTERNAL_EXCEPTION);
						e.printStackTrace();
					}
				} else {
					UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender, UtilsAPI.PERMISSION_MISSING);
				}
			} else {
				UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender, UtilsAPI.COMMAND_WRONG);
				UtilsAPI.sendSystemMessage(MessageLevel.NORMAL, sender, UtilsAPI.COMMAND_HELP_MESSAGE, label);
			}
		} catch (InvalidCommandException e) {
			UtilsAPI.sendSystemMessage(MessageLevel.ERROR, sender, UtilsAPI.COMMAND_TOO_MANY_ARGUMENTS);
			e.getNode().sendSpecificHelpMessage(sender, label);
		}
	}

	/**
	 * @return Le noeud racine de l'arbre d'exécution, non null
	 */
	public CommandNode getRoot() {
		return root;
	}

	/**
	 * Définit le noeud racine de l'arbre d'exécution.
	 *
	 * @param root
	 *            Le noeud racine de l'arbre d'exécution, non null
	 */
	private void setRoot(CommandNode root) {
		this.root = root;
	}
}
