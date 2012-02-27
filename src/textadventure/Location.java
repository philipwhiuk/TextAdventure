/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package textadventure;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import textadventure.Exit.Direction;
import textadventure.Game.GameFileException;
/**
 *
 * @author Philip
 */
class Location {
	/**
	 * Read converts lines into a Location object and it's data members. It reads until it finds Location_End
	 * @param node BufferedReader with a file resource to obtain the data
	 * @return The parsed Location
	 * @throws IOException Indicates a problem with accessing the file
	 * @throws GameFileException Indicates a problem with the game file itself
	 */
    static Location Read(Element node) throws IOException, GameFileException {
    	Location l = new Location();
    	l.ID = Integer.parseInt(node.getAttribute("ID"));
    	l.name = node.getAttribute("name");
    	
    	
        NodeList nodes = node.getChildNodes();
        for(int n = 0; n < nodes.getLength(); n++) {
        	Node nNode = nodes.item(n);
        	if (nNode.getNodeType() == Node.ELEMENT_NODE) {
        		Element eElement = (Element) nNode;
        		if(eElement.getTagName().equals("description")) {
        			l.description = ((Text) eElement.getFirstChild()).getData();        			
        		} else if(eElement.getTagName().equals("exits")) {
        			for(int e = 0; e < eElement.getChildNodes().getLength(); e++) {
        				if(eElement.getChildNodes().item(e).getNodeType() == Node.ELEMENT_NODE) {
	        				Element exElement = (Element) eElement.getChildNodes().item(e);
	        				Exit ex = new Exit(exElement.getAttribute("room"),exElement.getAttribute("direction"));
	        				l.add(ex.direction,ex);
        				}
        			}
        		} else if(eElement.getTagName().equals("items")) {
        			for(int i = 0; i < eElement.getChildNodes().getLength(); i++) {
        				if(eElement.getChildNodes().item(i).getNodeType() == Node.ELEMENT_NODE) {
	        				Element iElement = (Element) eElement.getChildNodes().item(i);
	        				Item it = Item.Read(iElement);
	        				l.add(it.getName(),it);
        				}
        			}
        		} else if(eElement.getTagName().equals("npcs")) {
        			for(int m = 0; m < eElement.getChildNodes().getLength(); m++) {
        				if(eElement.getChildNodes().item(m).getNodeType() == Node.ELEMENT_NODE) {
	        				Element iElement = (Element) eElement.getChildNodes().item(m);
	        				NPC npc = NPC.Read(iElement);
	        				l.add(npc.getName(),npc);
        				}
        			}
        		}
        	}
        }
        return l;
    }
	private int ID; 
    private String name; 
    private String description;
    private HashMap<String,Item> items;
    private HashMap<Direction,Exit> exits;   
    private HashMap<String,NPC> npcs;
    /**
     * 
     */
    Location() {
        exits = new HashMap<Direction,Exit>();
        items = new HashMap<String,Item>();
        npcs = new HashMap<String,NPC>();
    }
	/**
	 * 
	 * @return
	 */
	public int getID() {
		return ID;
	}
	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}    
    /**
     * 
     * @param direction
     * @param exit
     * @throws GameFileException
     */
    private void add(Direction direction, Exit exit) throws GameFileException {
        exits.put(direction,exit);
    }
    /**
     * 
     * @param itemName
     * @param i
     */
    private void add(String itemName, Item i) {
        items.put(itemName,i);
    }
    /**
     * 
     * @param name
     * @param m
     */
    private void add(String npcName, NPC npc) {
		npcs.put(npcName, npc);
	}    
    /**
     * 
     * @param direction
     * @return
     */
    boolean hasExit(Direction direction) {
        return exits.containsKey(direction);
    }
    /**
     * 
     * @param direction
     * @return
     */
    Exit getExit(Direction direction) {
        return exits.get(direction);
    }
    /**
     * 
     * @return A comma separated list of exits
     */
    String printExits() {
        Iterator<Direction> i = exits.keySet().iterator();
        String exitStr = "";
        if(i.hasNext()) {
            while(i.hasNext()) {
                exitStr+= i.next().toString();
            }
        } else {
            exitStr = "None";
        }
        return exitStr;
    }
    /**
     * 
     * @param item
     * @return The item that was taken
     */
    Item takeItem(String item) {    	
        return items.remove(item);
    }
    /**
     * 
     * @param item
     * @return True if the item exists, else false
     */
    boolean hasItem(String item) {
        return items.containsKey(item);
    }
    /**
     * 
     * @param item The item to be dropped at this location
     */
    void dropItem(Item item) {
        items.put(item.getName(), item);
    }
    /**
     * 
     * @param entity
     * @return
     */
	public Item infoItem(String entity) {
		return items.get(entity);
	}
	public boolean hasNPC(String npc) {
		return npcs.containsKey(npc);
	}
	public NPC getNPC(String npc) {
		return npcs.get(npc);
	}

    
}
