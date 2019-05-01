package fr.voltariuss.simpledevapi.cmds;

/**
 * Classe de gestion d'un checker d'argument
 * 
 * @author Voltariuss
 * @version 1.5.0
 *
 */
public interface CommandArgumentChecker {

	/**
	 * Vérifie la validité des données saisies pour l'argument associé.
	 * 
	 * @param arg
	 *            L'argument associé, non null
	 * @return True si les données saisies sont valides, false sinon
	 */
	public boolean check(String arg);
}
