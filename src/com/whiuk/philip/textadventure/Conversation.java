package com.whiuk.philip.textadventure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * A dialogue between a player and NPC.
 * @author Philip
 *
 */
public class Conversation {
    /**
     * The conversation name.
     */
	private String name;
	/**
	 * In.
	 */
	private String in;
	/**
	 * Out.
	 */
	private String out;
	/**
	 * Options.
	 */
	private HashMap<String, String> options;
	/**
	 * Actions.
	 */
	private ArrayList<ConversationAction> actions;
	/**
	 * The NPC associated with the conversation.
	 */
	private NPC npc;
	/**
	 * Default constructor.
	 */
	public Conversation() {
		options = new HashMap<String, String>();
		actions = new ArrayList<ConversationAction>();
	}

	/**
	 * Proceed with the conversation.
	 * @param string Extra commands
	 * @return Resulting conversation output.
	 */
	public final String doTalk(final String string) {
		//TODO: Complex Conversation Actions
	    StringBuilder conversation = new StringBuilder();
	    conversation.append("Player:");
	    conversation.append(in);
	    conversation.append(System.getProperty("line.separator"));
	    conversation.append("NPC:");
	    conversation.append(out);
	    conversation.append(System.getProperty("line.separator"));

		Iterator<ConversationAction> ia = actions.iterator();
		if (ia.hasNext()) {
			while (ia.hasNext()) {
				ConversationAction a = ia.next();
				conversation.append(a.doAction());
			}
		}

		Set<Entry<String, String>> set = options.entrySet();
		Iterator<Entry<String, String>> io = set.iterator();
		if (io.hasNext()) {
		    conversation.append(System.getProperty("line.separator"));
		    conversation.append("Options:");
            conversation.append(System.getProperty("line.separator"));
			while (io.hasNext()) {
				Entry<String, String> option = io.next();
				conversation.append(option.getKey());
				conversation.append(") ");
				conversation.append(option.getValue());
				conversation.append(
				        System.getProperty("line.separator"));
			}
		}
		Game.getGame().setLastConversation(this);
		return conversation.toString();
	}

	/**
	 * Reads conversation data from XML.
	 * @param node The node storing the conversation
	 * @return The conversation.
	 */
	public static Conversation read(final Element node) {
		Conversation c = new Conversation();
		c.setName(node.getAttribute("name"));
		c.in = node.getAttribute("In");
		c.out = node.getAttribute("Out");
		NodeList nodes = node.getChildNodes();
        for (int o = 0; o < nodes.getLength(); o++) {
        	Node oNode = nodes.item(o);
        	if (oNode.getNodeType() == Node.ELEMENT_NODE) {
        		Element eElement = (Element) oNode;
        		if (eElement.getTagName().equals("option")) {
        			String key = eElement.getAttribute("value");
        			String value = eElement.getAttribute("text");
        			c.options.put(key, value);
        		}
        		if (eElement.getTagName().equals("action")) {
        			ConversationAction a = ConversationAction.read(eElement);
        			c.actions.add(a);
        		}
        	}
        }
		return c;
	}

	/**
	 * Whether a given option is present.
	 * @param option The option
	 * @return True if present
	 */
	public final boolean hasOption(final String option) {
		return options.containsKey(option);
	}

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(final String name) {
        this.name = name;
    }

}
