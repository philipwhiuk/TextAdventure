package textadventure;

import org.w3c.dom.Element;

/**
 * An action within a conversation
 * @author Philip
 *
 */
public class ConversationAction {
	private String type;
	private String name;
	private String method;

	/**
	 * Reads an action from XML.
	 * @param node Node storing the action
	 * @return Action
	 */
	public static ConversationAction Read(Element node) {
		ConversationAction ca = new ConversationAction();
		ca.type = node.getAttribute("type");
		ca.name = node.getAttribute("name");
		ca.method = node.getAttribute("method");
		return ca;
	}
	/**
	 * Performs the action
	 * @return Text output.
	 */
	public String doAction() {
		if(type.equals("quest") && Game.getGame().hasQuest(name)) {
			return Game.getGame().getQuest(name).perform(method);
		}
		return null;
	}

}
