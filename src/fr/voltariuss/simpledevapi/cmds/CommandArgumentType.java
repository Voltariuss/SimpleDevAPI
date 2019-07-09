package fr.voltariuss.simpledevapi.cmds;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.voltariuss.simpledevapi.UtilsAPI;

/**
 * Classe de gestion d'un type d'argument
 *
 * @author Voltariuss
 * @version 1.5.0
 *
 */
public class CommandArgumentType {

	/**
	 * Définit le type d'argument attendu à une chaîne de caractère. En d'autres
	 * terme, toute saisie est acceptée (type d'argument par défaut à utiliser
	 * donc).
	 */
	public static final CommandArgumentType STRING = new CommandArgumentType("saisie", new CommandArgumentChecker() {

		@Override
		public void check(String arg) {}
	});

	/**
	 * Définit le type d'argument à un nombre.
	 */
	public static final CommandArgumentType NUMBER = new CommandArgumentType("nombre", new CommandArgumentChecker() {

		@Override
		public void check(String arg) {
			Integer.parseInt(arg);
		}
	});

	/**
	 * Définit le type d'argument à un joueur connecté.
	 */
	public static final CommandArgumentType ONLINE_PLAYER = new CommandArgumentType("joueur", new CommandArgumentChecker() {

		@Override
		public void check(String arg) throws DornacraftCommandException {
            Player player = Bukkit.getPlayer(arg);

            if (player == null) {
                throw new DornacraftCommandException(UtilsAPI.PLAYER_NOT_FOUND);
            }
		}
	});

	private String label;
	private CommandArgumentChecker checker;

	/**
	 * Constructeur
	 *
	 * @param label
	 *            Le label de l'argument, non null
	 * @param checker
	 *            Le vérificateur d'argument, peut être null
	 */
	public CommandArgumentType(String label, CommandArgumentChecker checker) {
		setLabel(label);
		setChecker(checker);
	}

	/**
	 * Créer et retourne le type d'argument avec un label différent (utile pour
	 * modifier le label d'un attribut statique tel que STRING par exemple)
	 *
	 * @param label
	 *            Le nouveau label de l'argument, non null
	 * @return Le nouveau type d'argument, non null
	 */
	public CommandArgumentType getCustomArgType(String label) {
		return new CommandArgumentType(label, getChecker());
	}

	@Override
	public String toString() {
		return "Label: " + getLabel();
	}

	/**
	 * @return Le label de l'argument, non null
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Définit le label de l'argument.
	 *
	 * @param label
	 *            Le label de l'argument, non null
	 */
	private void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return Le vérificateur de l'argument, peut être null
	 */
	public CommandArgumentChecker getChecker() {
		return checker;
	}

	/**
	 * Définit le vérificateur de l'argument.
	 *
	 * @param checker
	 *            Le vérificateur de l'argument, peut être null
	 */
	private void setChecker(CommandArgumentChecker checker) {
		this.checker = checker;
	}
}
