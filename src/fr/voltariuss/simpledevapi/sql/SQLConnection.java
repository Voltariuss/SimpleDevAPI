package fr.voltariuss.simpledevapi.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;

import org.bukkit.Bukkit;

import fr.dornacraft.main.DornacraftFaction;
import fr.voltariuss.simpledevapi.UtilsAPI;

/**
 * Classe de gestion d'une connexion SQL
 * 
 * @author Voltariuss
 * @version 1.5.0
 *
 */
public final class SQLConnection {

	public static final String SGBD_TYPE_ROOT = "jdbc:mysql://";

	private static volatile SQLConnection instance = null;

	private Connection connection = null;
	private String host, database, user, pwd;

	/**
	 * Constructeur
	 */
	private SQLConnection() {
		super();
	}

	/**
	 * Tente de récupérer l'unique instance de la classe. Si il n'en existe
	 * aucune, alors en créer une et la retourne.
	 * 
	 * @return L'unique instance de la classe, non null
	 */
	public static SQLConnection getInstance() {
		if (instance == null) {
			synchronized (SQLConnection.class) {
				if (instance == null) {
					instance = new SQLConnection();
				}
			}
		}
		return instance;
	}

	/**
	 * Effectue une tentative de connexion à la base de données spécifiée.
	 * 
	 * @param host
	 *            L'IP de la base de données, non null
	 * @param database
	 *            Le nom de la base de données, non null
	 * @param user
	 *            Le nom d'utilisateur, non null
	 * @param pwd
	 *            Le mot de passe, non null
	 */
	public void connect(String host, String database, String user, String pwd) {
		if (!isConnected()) {
			Bukkit.getLogger().log(Level.INFO,
					UtilsAPI.getPluginPrefix(DornacraftFaction.class) + UtilsAPI.SQL_CONNECTION_ATTEMPT);
			setHost(host);
			setDatabase(database);
			setUser(user);
			setPwd(pwd);

			try {
				setConnection(DriverManager.getConnection(SGBD_TYPE_ROOT + getHost() + "/" + getDatabase(), getUser(),
						getPwd()));
				Bukkit.getLogger().log(Level.INFO, "%s§eétat: §aconnecté",
						UtilsAPI.getPluginPrefix(DornacraftFaction.class));
			} catch (SQLException e) {
				Bukkit.getLogger().log(Level.SEVERE,
						UtilsAPI.getPluginPrefix(DornacraftFaction.class) + UtilsAPI.SQL_CONNECTION_FAILED, e);
			}
		}
	}

	/**
	 * Coupe la connexion à la base de données actuelle.
	 */
	public void disconnect() {
		if (isConnected()) {
			try {
				getConnection().close();
				Bukkit.getLogger().log(Level.INFO, "%s§eétat: §cdéconnecté",
						UtilsAPI.getPluginPrefix(DornacraftFaction.class));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Coupe la connexion à la base de données actuelle si elle est présente et
	 * effectue une nouvelle connexion.
	 */
	public void refresh() {
		try {
			if (isConnected()) {
				getConnection().close();
			}
			setConnection(
					DriverManager.getConnection(SGBD_TYPE_ROOT + getHost() + "/" + getDatabase(), getUser(), getPwd()));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return True si la connexion à une base de données est présente, false
	 *         sinon
	 */
	public boolean isConnected() {
		boolean isConnected = false;

		try {
			PreparedStatement query = getConnection().prepareStatement("SELECT 1");
			query.executeQuery();
			query.close();
			isConnected = true;
		} catch (Exception e) {
		}
		return isConnected;
	}

	/**
	 * @return La connexion SQL actuelle à la base de données enregistrée, non
	 *         null
	 */
	public Connection getConnection() {
		return connection;
	}

	/**
	 * Modifie la connexion SQL.
	 * 
	 * @param connection
	 *            Le nouvelle connexion SQL, non null
	 */
	private void setConnection(Connection connection) {
		this.connection = connection;
	}

	/**
	 * @return L'hôte de la base de données, non null
	 */
	public String getHost() {
		return host;
	}

	/**
	 * Définit l'hôte de la base de données.
	 * 
	 * @param host
	 *            Le nouvelle hôte de la base de données, non null
	 */
	private void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return La base de données actuellement ciblée, non null
	 */
	public String getDatabase() {
		return database;
	}

	/**
	 * Définit la base de données à cibler.
	 * 
	 * @param database
	 *            La nouvelle base de données ciblée, non null
	 */
	private void setDatabase(String database) {
		this.database = database;
	}

	/**
	 * @return Le nom d'utilisateur actuellement utilisé, non null
	 */
	public String getUser() {
		return user;
	}

	/**
	 * Définit le nom d'utilisateur utilisé pour la connexion à la base de
	 * données.
	 * 
	 * @param user
	 *            Le nouveau nom d'utilisateur, non null
	 */
	private void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return Le mot de passe actuellement utilisé, non null
	 */
	public String getPwd() {
		return pwd;
	}

	/**
	 * Définit le mot de passe.
	 * 
	 * @param pwd
	 *            Le nouveau mot de passe, non null
	 */
	private void setPwd(String pwd) {
		this.pwd = pwd;
	}
}
