package com.whiuk.philip.textadventure;

import org.w3c.dom.Element;

/**
 * 
 * @author Philip
 *
 */
public class Quest {
	/**
	 * 
	 * @author Philip
	 *
	 */
    public enum Status {
        /**
         * 
         */
        AVAILABLE,
        /**
         * 
         */
        STARTED,
        /**
         * 
         */
        COMPLETED;
    }
    /**
     * 
     */
	private String name;
    /**
     * 
     */
	private String title;
    /**
     * 
     */
	private String description;
	/**
	 * 
	 * @return Name
	 */
	public final String getName() {
		return name;
	}
    /**
     * 
     * @return Description
     */
	public final String getDescription() {
		return description;
	}
    /**
     * @param method Method to perform
     * @return Output
     */
	public final String perform(final String method) {
		if (method.equals("start")) {
			if (Game.getGame().getPlayerQuestStatus(this) == null
			        || Game.getGame().getPlayerQuestStatus(this)
			            == Quest.Status.AVAILABLE) {
				Game.getGame().setPlayerQuestStatus(this,
				        Quest.Status.STARTED);
				return "Started quest: " + title + "\n";
			}
		}
		return null;
	}
    /**
     * @param node Node
     * @return Quest
     */
	public static Quest read(final Element node) {
		Quest q = new Quest();
		q.name = node.getAttribute("name");
		q.title = node.getAttribute("title");
		q.description = node.getChildNodes().item(0).getTextContent();
		return q;
	}
    /**
     * 
     * @return Title
     */
	public final String getTitle() {
		return title;
	}
}
