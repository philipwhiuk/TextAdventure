package com.whiuk.philip.textadventure;

import java.util.HashMap;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 * Non player character
 * @author Philip Whitehouse
 *
 */
public class NPC {
    // Temporary State Data
    static Conversation lastConversation;
    
	private String name;
	private String description;
	private boolean isAttackable;
	private boolean isTalkable;
	private HashMap<String,Conversation> cons;
	private String defaultCon;
	/**
	 * Default constructor
	 */
	public NPC() {
		cons = new  HashMap<String,Conversation>();
	}
	/**
	 * @return Name of NPC
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return Description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @return Whether they can talk
	 */
	public boolean canTalk() {
		return isTalkable;
	}	
	/**
	 * @return Whether they can be attacked
	 */
	public boolean canAttack() {
		return isAttackable;
	}
	/**
	 * Reads NPC from node
	 * @param node Node
	 * @return NPC
	 */
	public static NPC Read(Element node) {
		NPC n = new NPC();
		n.name = node.getAttribute("name");
		n.isAttackable = Boolean.parseBoolean(node.getAttribute("isAttackable"));
		n.isTalkable = Boolean.parseBoolean(node.getAttribute("isTalkable"));
		n.defaultCon = node.getAttribute("defaultCon");    
		NodeList nodes = node.getChildNodes();
        for(int i = 0; i < nodes.getLength(); i++) {
        	Node nNode = nodes.item(i);
        	if (nNode.getNodeType() == Node.ELEMENT_NODE) {
        		Element eElement = (Element) nNode;
        		if(eElement.getTagName().equals("description")) {
        			n.description = ((Text) eElement.getFirstChild()).getData();        			
        		} else if(eElement.getTagName().equals("conversations")) {
        			for(int e = 0; e < eElement.getChildNodes().getLength(); e++) {
        				if(eElement.getChildNodes().item(e).getNodeType() == Node.ELEMENT_NODE) {
	        				Element exElement = (Element) eElement.getChildNodes().item(e);
	        				Conversation c = Conversation.Read(exElement);
	        				n.add(c.name,c);
        				}
        			}
        		}
        	}
        }
		return n;
	}
	private void add(String key, Conversation c) {
		cons.put(key, c);
	}
	/**
	 * Handle talking to the NPC.
	 * @param string Option
	 * @return Output
	 */
	public String doTalk(String string) {
	    if(isTalkable) {
    		if(lastConversation != null) {
    			if(cons.containsValue(lastConversation)) {
    				if(lastConversation.hasOption(string)) {
    					return cons.get(string).doTalk(string);
    				}
    			}
    		}
    		return cons.get(defaultCon).doTalk(string);
	    } else {
	        throw new UnsupportedOperationException();
	    }
	}
}
