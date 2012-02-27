/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package textadventure;

import java.io.IOException;
import java.util.HashMap;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Philip
 */
class Item {
    private static HashMap<String,Integer> statistics;
	private String description;
    private String name;
	private boolean isEquippable;
	private String slot;
	private String title;
    static Item Read(Element node) throws IOException {
        Item i = new Item();
        i.name = node.getAttribute("name");
        i.title = node.getAttribute("title");
        i.isEquippable = Boolean.parseBoolean(node.getAttribute("isEquippable"));        
        i.slot = node.getAttribute("slot");  
        NodeList nodes = node.getChildNodes();
        for(int j = 0; j < nodes.getLength(); j++) {
        	Node oNode = nodes.item(j);
        	if (oNode.getNodeType() == Node.ELEMENT_NODE) {
        		Element eElement = (Element) oNode;
        		if(eElement.getTagName().equals("description")) {
        			i.description = eElement.getChildNodes().item(0).getTextContent();
        		}
        		else if(eElement.getTagName().equals("statistics")) {
        			NamedNodeMap map = eElement.getAttributes();
        			for(int a = 0; a < map.getLength(); a++) {
        				statistics.put(map.item(a).getNodeName(),Integer.parseInt(map.item(a).getNodeValue()));
        			}
        		}
        	}
        }
        return i;
    }
    public Item() {
    	statistics = new HashMap<String,Integer>();
    }
	public String getName() {
		return name;
	}
	public String getTitle() {
		return title;
	}	
	public String getDescription() {
		return description;
	}
	public boolean canEquip() {
		return isEquippable;
	}
	public String getSlot() {
		return slot;
	}
	public HashMap<String, Integer> getStats() {
		return statistics;
	}
}
