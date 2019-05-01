package fr.voltariuss.simpledevapi.cmds;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Classe de gestion d'un argument de commande
 * 
 * @author Voltariuss
 * @version 1.5.0
 *
 */
public class CommandArgument {

	private CommandArgumentType type;
	private boolean isRequired;
	private ArrayList<String> alias = new ArrayList<>();

	/**
	 * Constructeur d'un argument de saisie
	 * 
	 * @param type
	 *            Le type d'argument, non null
	 * @param isRequired
	 *            True si l'argument est requis dans l'exécution de la commande,
	 *            false sinon
	 */
	public CommandArgument(CommandArgumentType type, boolean isRequired) {
		setType(type);
		setRequired(isRequired);
	}

	/**
	 * Constructeur d'un argument
	 * 
	 * @param label
	 *            Le label de l'argument, non null
	 * @param alias
	 *            La liste des aliases de l'argument, non null
	 */
	public CommandArgument(String label, String... alias) {
		this(new CommandArgumentType(label, null), true);
		getAlias().addAll(Arrays.asList(alias));
	}

	@Override
	public String toString() {
		return getType() + "; isRequired: " + isRequired() + "; Aliases: " + getAlias().toString();
	}

	/**
	 * @return True si l'argument correspond à une saisie, false sinon
	 */
	public boolean isInputArg() {
		return getType().getChecker() != null;
	}

	/**
	 * @return Le type d'argument, non null
	 */
	public CommandArgumentType getType() {
		return type;
	}

	/**
	 * Définit le type de l'argument.
	 * 
	 * @param type
	 *            Le type d'argument, non null
	 */
	private void setType(CommandArgumentType type) {
		this.type = type;
	}

	/**
	 * @return True si l'argument est requis dans l'exécution de la commande,
	 *         false sinon
	 */
	public boolean isRequired() {
		return isRequired;
	}

	/**
	 * Définit si l'argument est requis dans l'exécution de la commande ou non.
	 * 
	 * @param isRequired
	 *            True si l'argument est requis dans l'exécution de la commande,
	 *            false sinon
	 */
	private void setRequired(boolean isRequired) {
		this.isRequired = isRequired;
	}

	/**
	 * @return La liste des alias de l'argument, non null
	 */
	public ArrayList<String> getAlias() {
		return alias;
	}
}
