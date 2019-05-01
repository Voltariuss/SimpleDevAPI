package fr.voltariuss.simpledevapi.cmds;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringJoiner;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.google.common.collect.Lists;

import fr.voltariuss.simpledevapi.MessageLevel;
import fr.voltariuss.simpledevapi.UtilsAPI;

/**
 * Classe de gestion d'un noeud de l'arbre d'exécution de commandes
 * 
 * @author Voltariuss
 * @version 1.5.0
 *
 */
public class CommandNode {

	private CommandNode parent;
	private CommandArgument argument;
	private String description;
	private DornacraftCommandExecutor executor;
	private String specificPermission;
	private ArrayList<CommandNode> childs = new ArrayList<>();

	public static final int NB_ELEM_PER_HELP_PAGE = 5;

	/////
	// CONSTRUCTEURS
	/////

	/**
	 * Constructeur final d'un noeud
	 * 
	 * @param parent
	 *            Le noeud parent, peut être null
	 * @param argument
	 *            L'argument à associer avec le noeud de la commande, non null
	 * @param description
	 *            La description de la commande, non null
	 * @param executor
	 *            L'exécuteur du noeud, peut être null
	 * @param specificPermission
	 *            La permission spécifique applicable à ce noeud uniquement,
	 *            peut être null
	 */
	CommandNode(CommandNode parent, CommandArgument argument, String description, DornacraftCommandExecutor executor,
			String specificPermission) {
		setParent(parent);
		setArgument(argument);
		setDescription(description);
		setExecutor(executor);
		setSpecificPermission(specificPermission);
	}

	/**
	 * Constructeur d'un noeud
	 * 
	 * @param argument
	 *            L'argument à associer avec le noeud de la commande, non null
	 * @param description
	 *            La description de la commande, non null
	 * @param executor
	 *            L'exécuteur du noeud, peut être null
	 * @param specificPermission
	 *            La permission spécifique applicable à ce noeud uniquement,
	 *            peut être null
	 */
	public CommandNode(CommandArgument argument, String description, DornacraftCommandExecutor executor,
			String specificPermission) {
		this(null, argument, description, executor, specificPermission);
	}

	/**
	 * Constructeur d'un noeud simple
	 * 
	 * @param argument
	 *            L'argument à associer avec le noeud de la commande, non null
	 * @param description
	 *            La description de la commande, non null
	 */
	public CommandNode(CommandArgument argument, String description) {
		this(argument, description, null, null);
	}

	/////
	// NOEUD : Méthodes relatives au noeud
	/////

	/**
	 * Permet de savoir si le noeud est une feuille de l'arbre ou non.
	 * 
	 * @return True si le noeud n'a pas de fils, false sinon
	 */
	public boolean isLeaf() {
		return getChilds().isEmpty();
	}

	/**
	 * Vérifie si le noeud fils correspond à un argument de saisie ou non.
	 * 
	 * @return True si le noeud fils correspond à un argument de saisie, false
	 *         sinon
	 */
	public boolean isNextNodeInputArg() {
		return !getChilds().isEmpty() && getFirstChild().getArgument().isInputArg();
	}

	/**
	 * Vérifie si le noeud est exécutable ou non.
	 * 
	 * @return True si la noeud est exécutable, false sinon
	 */
	public boolean isExecutable() {
		return getExecutor() != null;
	}

	/**
	 * Récupère et retourne la liste des arguments de la commande excluant
	 * l'argument racine.
	 * 
	 * @return La liste des arguments de la commande excluant l'argument racine,
	 *         non null
	 */
	public List<String> getCommandArgs() {
		List<String> args = new ArrayList<>();
		CommandNode currentNode = this;

		// On parcours l'ensemble des noeuds parents en commençant par le noeud
		// courant
		while (currentNode.getParent() != null) {
			args.add(currentNode.getArgument().getType().getLabel());
			currentNode = currentNode.getParent();
		}
		return Lists.reverse(args);
	}

	/**
	 * Détermine et retourne la permission du noeud de la commande.<br>
	 * <br>
	 * <b>Méthode pour déterminer la permission d'une commande :</b>
	 * <ol>
	 * <li>On récupère la permission globale de la commande (ligne "permission"
	 * de la commande dans le fichier plugin.yml)</li>
	 * <li>Pour chaque argument n'étant pas une saisie, on incrémente la
	 * permission par cet argument</li>
	 * </ol>
	 * <b><i>Exemple :</i></b><br>
	 * Permission de la commande "test" pour le plugin Dornacraft-API dans le
	 * plugin.yml : dornacraft.api.test<br>
	 * Permission de la commande "test arg1 &lt;string&gt; arg2" :
	 * dornacraft.api.test.arg1.arg2
	 * 
	 * @param command
	 *            La commande Bukkit associée au noeud, non null
	 * @return La permission associée au noeud de la commande, non null
	 */
	public String getPermission(Command command) {
		ArrayList<String> args = new ArrayList<>();
		CommandNode currentNode = this;

		// On parcours l'ensemble des noeuds parents en commençant par le noeud
		// courant
		while (currentNode.getParent() != null) {
			if (!currentNode.getArgument().isInputArg()) {
				args.add(currentNode.getArgument().getType().getLabel());
			}
			currentNode = currentNode.getParent();
		}
		// On construit la permission à partir des arguments récupérés
		StringBuilder sb = new StringBuilder(command.getPermission());

		for (int i = args.size() - 1; i >= 0; i--) {
			sb.append("." + args.get(i));
		}
		return sb.toString();
	}

	/**
	 * Vérifie si le noeud possède une permission spécifique ou non.
	 * 
	 * @return True si le noeud possède une permission spécifique, false sinon
	 */
	public boolean hasSpecificPermission() {
		return getSpecificPermission() != null;
	}

	/**
	 * Récupère et retourne la racine de l'arbre d'exécution associé au noeud
	 * courrant.
	 * 
	 * @return La racine de l'arbre d'exécution associée au noeud courrant, non
	 *         null
	 */
	public CommandNode getRoot() {
		return getRootWorker(this);
	}

	/**
	 * Worker récursif de la méthode {@link CommandNode#getRoot()}.
	 * 
	 * @param node
	 *            Le noeud dont le père racine doit être trouvé, non null
	 * @return Le noeud racine de l'arbre d'exécution, non null
	 */
	public CommandNode getRootWorker(CommandNode node) {
		if (node.getParent() == null) {
			return node;
		} else {
			return getRootWorker(node.getParent());
		}
	}

	@Override
	public String toString() {
		return "\n - " + getArgument() + "; Description: " + getDescription() + "; Permission: "
				+ (hasSpecificPermission() ? getSpecificPermission() : "none") + "; hasExecutor: "
				+ (getExecutor() != null);
	}

	/////
	// NOEUDS FILS : Méthodes relatives aux noeuds fils
	/////

	/**
	 * @return Le premier fils, peut être null
	 */
	public CommandNode getFirstChild() {
		return getChilds().get(0);
	}

	/**
	 * Vérifie si le noeud possède des fils ou non.
	 * 
	 * @return True si le noeud possède des fils, false sinon
	 */
	public boolean hasChild() {
		return !getChilds().isEmpty();
	}

	/**
	 * Récupère et retourne le noeud enfant correspondant au nom spécifié.
	 * 
	 * @param label
	 *            Le label du noeud de la commande à rechercher, non null
	 * @return L'enfant correspondant, peut être null
	 */
	public CommandNode getChildWithTypeArg(String label) {
		CommandNode child = null;

		if (!isLeaf()) {
			Iterator<CommandNode> iterator = getChilds().iterator();

			while (iterator.hasNext() && child == null) {
				CommandNode node = iterator.next();

				// Vérifie si le noeud ne correspond pas à un argument de saisie
				// et si il
				// correspond au label spécifié
				if (!node.getArgument().isInputArg()
						&& node.getArgument().getType().getLabel().equalsIgnoreCase(label)) {
					child = node;
				}
			}
		}
		return child;
	}

	/**
	 * Retourne le noeud fils correspondant à celui spécifié.
	 * 
	 * @param commandNode
	 *            Le noeud à rechercher parmi les fils, non null
	 * @return Le noeud correspondant à celui spécifié, peut être null
	 */
	public CommandNode getChild(CommandNode commandNode) {
		return getChildWithTypeArg(commandNode.getArgument().getType().getLabel());
	}

	/**
	 * Vérifie si le noeud possède le fils spécifié ou non.
	 * 
	 * @param commandNode
	 *            Le noeud fils à vérifier, non null
	 * @return True si le noeud spécifié est un fils, false sinon
	 */
	public boolean hasChild(CommandNode commandNode) {
		return getChildWithTypeArg(commandNode.getArgument().getType().getLabel()) != null ? true : false;
	}

	/////
	// HELP MESSAGES : Méthodes relatives à l'envoie de messages d'aide
	/////

	/**
	 * Envoie à l'émetteur de la commande un message d'aide sur celle-ci à
	 * partir du noeud courant.
	 * 
	 * @param sender
	 *            L'émetteur de la commande, non null
	 * @param label
	 *            Le label de la commande, non null
	 * @param page
	 *            Le numéro de la page d'aide à afficher, non null
	 */
	public void sendHelpMessage(CommandSender sender, String label, int page) {
		// On détermine le nombre de pages d'aide
		int nbPages = (int) Math.ceil(getChilds().size() / (double) NB_ELEM_PER_HELP_PAGE);

		// On ajuste le numéro de page si nécessaire
		if (page < 1) {
			page = 1;
		} else if (page > nbPages) {
			page = nbPages;
		}

		// Envoie du header de la page d'aide
		UtilsAPI.sendSystemMessage(MessageLevel.NORMAL, sender, UtilsAPI.COMMAND_HELP_HEADER,
				label + (nbPages > 1 ? " (" + page + "/" + nbPages + ")" : ""));

		// Envoie d'un message d'utilisation pour chaque commande de la page
		for (int i = NB_ELEM_PER_HELP_PAGE * (page - 1); i < NB_ELEM_PER_HELP_PAGE * page
				&& i < getChilds().size(); i++) {
			UtilsAPI.sendSystemMessage(MessageLevel.NORMAL, sender, "§a§l%s %s", UtilsAPI.CARAC_ARROW,
					getChilds().get(i).getUtils(label, true));
		}

		// Si il y a plusieurs pages d'aide, on envoie l'aide pour changer de
		// page
		if (nbPages > 1) {
			UtilsAPI.sendSystemMessage(MessageLevel.NORMAL, sender, UtilsAPI.COMMAND_HELP_CHANGE_PAGE, label);
		}
	}

	/**
	 * Envoie une aide spécifique à la commande saisie par l'utilisateur.
	 * 
	 * @param sender
	 *            L'émetteur de la commande, non null
	 * @param label
	 *            Le label de la commande, non null
	 */
	public void sendSpecificHelpMessage(CommandSender sender, String label) {
		UtilsAPI.sendSystemMessage(MessageLevel.NORMAL, sender, UtilsAPI.COMMAND_HELP_TRY_MESSAGE,
				getUtils(label, false));
	}

	/**
	 * Construit et retourne le message d'utilisation de la commande accompagné
	 * d'une description ou non.
	 * 
	 * @param label
	 *            Le label de la commande, non null
	 * @param withDescription
	 *            Indique si le message d'utilisation de la commande doit être
	 *            accompagné d'une description ou non, non null
	 * @return Le message d'utilisation de la commande, non null
	 */
	public String getUtils(String label, boolean withDescription) {
		// Début de construction du message d'utilisation
		StringJoiner sj = new StringJoiner(" ");
		sj.add(ChatColor.GOLD + "/" + label);

		// On récupère les arguments de la commande associée au noued courant
		Iterator<String> iterator = getCommandArgs().iterator();
		// On commence à partir du noeud racine et on remonte jusqu'au noeud
		// fils
		CommandNode currentNode = getRoot();

		// Début du parcours des noeuds parents jusqu'au noeud courant compris
		// en excluant le noeud racine
		while (iterator.hasNext()) {
			String arg = iterator.next();

			// On récupère le noeud fils correspondant suivant si il s'agit d'un
			// argument de saisie ou non
			if (currentNode.isNextNodeInputArg()) {
				currentNode = currentNode.getFirstChild();
			} else {
				currentNode = currentNode.getChildWithTypeArg(arg);
			}
			// On ajoute l'indication d'usage de l'argument dans le message
			// d'utilisation de la commande
			sj.add(getArgumentUsage(currentNode));
		}

		// On complète le message d'utilisation avec les arguments des noeuds
		// fils tant que le parcours correspond à celui d'une branche (1 noeud
		// fils uniquement)
		while (currentNode.getChilds().size() == 1) {
			currentNode = currentNode.getFirstChild();
			sj.add(getArgumentUsage(currentNode));
		}
		// On retourne le message d'utilisation avec ou sans description avec
		// indication si il s'agit de la forme finale de la commande ou non
		return sj.toString() + (!currentNode.getChilds().isEmpty() ? " §b..." : "") + ChatColor.GOLD
				+ (withDescription ? "\n  " + ChatColor.YELLOW + currentNode.getDescription() : "");
	}

	/**
	 * Construit et retourne l'indication d'usage de l'argument.
	 * 
	 * @param node
	 *            Le noeud correspondant à l'argument à traiter, non null
	 * @return L'indication d'usage de l'argument, non null
	 */
	public String getArgumentUsage(CommandNode node) {
		String usage = "";

		// On vérifie si l'argument du noeud correspond à une saisie ou non en
		// prenant en compte si il s'agit d'un argument obligatoire ou non
		if (node.getArgument().isInputArg()) {
			Object[] args = node.getArgument().isRequired() ? new Object[] { "<", ">" } : new Object[] { "[", "]" };
			usage = ChatColor.AQUA + "{0}" + node.getArgument().getType().getLabel() + "{1}";
			usage = new MessageFormat(usage).format(args);
		} else if (node.getArgument().isRequired()) {
			usage = ChatColor.GOLD + node.getArgument().getType().getLabel();
		} else {
			usage = ChatColor.YELLOW + "[" + node.getArgument().getType().getLabel() + "]";
		}
		return usage;
	}

	/////
	// GETTERS & SETTERS
	/////

	/**
	 * @return Le noeud parent, peut être null
	 */
	public CommandNode getParent() {
		return parent;
	}

	/**
	 * Définit le noeud parent.
	 * 
	 * @param parent
	 *            Le noeud parent, peut être null
	 */
	private void setParent(CommandNode parent) {
		this.parent = parent;
	}

	/**
	 * @return L'argument associé au noeud de la commande, non null
	 */
	public CommandArgument getArgument() {
		return argument;
	}

	/**
	 * Définit l'argument associé au noeud de la commande.
	 * 
	 * @param argument
	 *            L'argument à associer avec le noeud de la commande, non null
	 */
	private void setArgument(CommandArgument argument) {
		this.argument = argument;
	}

	/**
	 * @return La description de la commande, non null
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Définit la description de la commande.
	 * 
	 * @param description
	 *            La description à associer à la commande, non null
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return L'exécueteur du noueud de la commande, peut être null
	 */
	public DornacraftCommandExecutor getExecutor() {
		return executor;
	}

	/**
	 * Définit l'exécutor du noeud.
	 * 
	 * @param executor
	 *            L'executor du noueud, peut être null
	 */
	public void setExecutor(DornacraftCommandExecutor executor) {
		this.executor = executor;
	}

	/**
	 * @return La permission spécifique du noeud, peut être null
	 */
	public String getSpecificPermission() {
		return specificPermission;
	}

	/**
	 * Définit la permission spécifique du noeud.
	 * 
	 * @param specificPermission
	 *            La permission spécifique du noeud, peut être null
	 */
	private void setSpecificPermission(String specificPermission) {
		this.specificPermission = specificPermission;
	}

	/**
	 * Permet de récupérer la liste des enfants du noeud.
	 * 
	 * @return La liste des enfants du noeud, non null
	 */
	public ArrayList<CommandNode> getChilds() {
		return childs;
	}
}
