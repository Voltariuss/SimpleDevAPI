package fr.voltariuss.simpledevapi;

import org.bukkit.ChatColor;

/**
 * Classe de gestion d'un niveau de message
 * 
 * @author Voltariuss
 * @version 1.5.0
 *
 */
public enum MessageLevel {

	INFO(UtilsAPI.PREFIX_DORNACRAFT_MESSAGE, ChatColor.YELLOW), 
	SUCCESS(UtilsAPI.PREFIX_DORNACRAFT_MESSAGE, ChatColor.GREEN), 
	FAILURE(UtilsAPI.PREFIX_DORNACRAFT_MESSAGE, ChatColor.RED), 
	WARNING(UtilsAPI.PREFIX_WARNING_MESSAGE, ChatColor.RED), 
	ERROR(UtilsAPI.PREFIX_ERROR_MESSAGE, ChatColor.RED), 
	NORMAL("", ChatColor.RESET);

	private String prefix;
	private ChatColor msgColor;

	/**
	 * Constructeur
	 * 
	 * @param prefix
	 *            Le préfixe associé au niveau de message, non null
	 * @param msgColor
	 *            La couleur du message associée au niveau de message, non null
	 */
	private MessageLevel(String prefix, ChatColor msgColor) {
		setPrefix(prefix);
		setMsgColor(msgColor);
	}

	/**
	 * @return Le préfixe associé au niveau de message, non null
	 */
	public String getPrefix() {
		return prefix + getMsgColor();
	}

	/**
	 * Définit le préfixe associé au niveau de message.
	 * 
	 * @param prefix
	 *            Le préfixe associé au niveau de message, non null
	 */
	private void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * @return La couleur du message associée au niveau de message, non null
	 */
	public ChatColor getMsgColor() {
		return msgColor;
	}

	/**
	 * Définit la couleur du message associée au niveau de message, non null
	 * 
	 * @param msgColor
	 *            La couleur du message associée au niveau de message, non null
	 */
	public void setMsgColor(ChatColor msgColor) {
		this.msgColor = msgColor;
	}
}
