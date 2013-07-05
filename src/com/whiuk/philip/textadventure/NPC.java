package com.whiuk.philip.textadventure;

import java.util.HashMap;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 * Non player character.
 * @author Philip Whitehouse
 *
 */
public class NPC {

    /**
     * 
     */
	private String name;
    /**
     * 
     */
	private String description;
    /**
     * 
     */
	private boolean isAttackable;
    /**
     * 
     */
	private boolean isTalkable;
    /**
     * 
     */
	private HashMap<String, Conversation> cons;
    /**
     * 
     */
	private String defaultCon;
	/**
	 * Default constructor.
	 */
	public NPC() {
		cons = new  HashMap<String, Conversation>();
	}
	/**
	 * @return Name of NPC
	 */
	public final String getName() {
		return name;
	}
	/**
	 * @return Description
	 */
	public final String getDescription() {
		return description;
	}
	/**
	 * @return Whether they can talk
	 */
	public final boolean canTalk() {
		return isTalkable;
	}
	/**
	 * @return Whether they can be attacked
	 */
	public final boolean canAttack() {
		return isAttackable;
	}
	/**
	 * Reads NPC from node.
	 * @param node Node
	 * @return NPC
	 */
	public static NPC read(final Element node) {
		NPC n = new NPC();
		n.name = node.getAttribute("name");
		n.isAttackable = Boolean.parseBoolean(
		        node.getAttribute("isAttackable"));
		n.isTalkable = Boolean.parseBoolean(
		        node.getAttribute("isTalkable"));
		n.defaultCon = node.getAttribute("defaultCon");
		NodeList nodes = node.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
        	Node nNode = nodes.item(i);
        	if (nNode.getNodeType() == Node.ELEMENT_NODE) {
        		Element eElement = (Element) nNode;
                readChild(n, eElement);
        	}
        }
		return n;
	}
	/**
	 * Read a child node.
	 * @param n NPC to read data into
	 * @param eElement Element to read from.
	 */
	private static void readChild(final NPC n, final Element eElement) {
        if (eElement.getTagName().equals("description")) {
            n.description = ((Text) eElement.getFirstChild()).getData();
        } else if (eElement.getTagName().equals("conversations")) {
            for (int e = 0; e < eElement.getChildNodes().getLength(); e++) {
                if (eElement.getChildNodes().item(e)
                        .getNodeType() == Node.ELEMENT_NODE) {
                    Element exElement = (Element) eElement.getChildNodes()
                            .item(e);
                    Conversation c = Conversation.read(exElement);
                    n.add(c.getName(), c);
                }
            }
        }
    }
    /**
	 * Add a conversation to the NPCs set.
	 * @param key Key
	 * @param c Conversation
	 */
	private void add(final String key, final Conversation c) {
		cons.put(key, c);
	}
	/**
	 * Handle talking to the NPC.
	 * @param string Option
	 * @return Output
	 */
	public final String doTalk(final String string) {
	    if (isTalkable) {
    		if (Game.getGame().getLastConversation() != null
    		        && cons.containsValue(Game.getGame()
    			        .getLastConversation())
			        && Game.getGame().getLastConversation()
				        .hasOption(string)) {
    					return cons.get(string).doTalk(string);
    		}
    		return cons.get(defaultCon).doTalk(string);
	    } else {
	        throw new UnsupportedOperationException();
	    }
	}
}
