package fr.voltariuss.simpledevapi.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Classe utilitaire pour les requêtes SQL
 * 
 * @author Voltariuss
 * @version 1.5.0
 *
 */
public class SQLUtils {

	/**
	 * Envoie une requête d'ouverture d'une transaction SQL (BEGIN).
	 * 
	 * @throws SQLException
	 *             Si une erreur avec la base de données est détectée
	 */
	public static void startTransaction() throws SQLException {
		PreparedStatement query = SQLConnection.getInstance().getConnection().prepareStatement("BEGIN;");
		query.execute();
		query.close();
	}

	/**
	 * Envoie une requête de fermeture d'une transaction sans rollback (COMMIT).
	 * 
	 * @throws SQLException
	 *             Si une erreur avec la base de données est détectée
	 */
	public static void commitTransaction() throws SQLException {
		PreparedStatement query = SQLConnection.getInstance().getConnection().prepareStatement("COMMIT;");
		query.execute();
		query.close();
	}

	/**
	 * Envoie une requête de fermeture d'une transaction avec rollback
	 * (ROLLBACK).
	 * 
	 * @throws SQLException
	 *             Si une erreur avec la base de données est détectée
	 */
	public static void rollbackTransaction() throws SQLException {
		PreparedStatement query = SQLConnection.getInstance().getConnection().prepareStatement("ROLLBACK;");
		query.execute();
		query.close();
	}
}
