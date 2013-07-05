/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.whiuk.philip.textadventure;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import com.whiuk.philip.textadventure.Exit.Direction;
import com.whiuk.philip.textadventure.Game.GameFileException;

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
    static Location read(final Element node) throws IOException, GameFileException {
        //TODO: Refactor deep looping
    	Location l = new Location();
    	l.id = Integer.parseInt(node.getAttribute("ID"));
    	l.name = node.getAttribute("name");

        NodeList nodes = node.getChildNodes();
        for (int n = 0; n < nodes.getLength(); n++) {
        	Node nNode = nodes.item(n);
        	if (nNode.getNodeType() == Node.ELEMENT_NODE) {
        		Element eElement = (Element) nNode;
        		NodeList cNodes;
                switch (eElement.getTagName()) {
        		    case "description":
        		        l.description =
        		            ((Text) eElement.getFirstChild()).getData();
        		        break;
        		    case "exits":
        		        cNodes = eElement.getChildNodes();
            		    for (int e = 0; e < cNodes.getLength(); e++) {
                            if (cNodes.item(e).getNodeType()
                                    == Node.ELEMENT_NODE) {
                                Element exElement = (Element) cNodes.item(e);
                                String r = exElement.getAttribute("room");
                                String d = exElement.getAttribute("direction");
                                Exit ex = new Exit(r, d);
                                l.add(ex.getDirection(), ex);
                            }
                        }
            		    break;
        		    case "items":
        		        cNodes = eElement.getChildNodes();
        		        for (int i = 0; i < cNodes.getLength(); i++) {
                            if (cNodes.item(i).getNodeType()
                                    == Node.ELEMENT_NODE) {
                                Element iElement = (Element) cNodes.item(i);
                                Item it = Item.read(iElement);
                                l.add(it.getName(), it);
                            }
                        }
        		        break;
        		    case "npcs":
                        cNodes = eElement.getChildNodes();
        		        for (int m = 0; m < cNodes.getLength(); m++) {
                            if (cNodes.item(m).getNodeType()
                                    == Node.ELEMENT_NODE) {
                                Element iElement = (Element) cNodes.item(m);
                                NPC npc = NPC.read(iElement);
                                l.add(npc.getName(), npc);
                            }
                        }
        		        break;
                    default:
                        throw new RuntimeException();
        		}
        	}
        }
        return l;
    }
    /**
     * 
     */
	private int id; 
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
    private HashMap<String, Item> items;
    /**
     * 
     */
    private HashMap<Direction, Exit> exits;
    /**
     * 
     */
    private HashMap<String, NPC> npcs;
    /**
     * 
     */
    Location() {
        exits = new HashMap<Direction, Exit>();
        items = new HashMap<String, Item>();
        npcs = new HashMap<String, NPC>();
    }
	/**
	 * 
	 * @return ID
	 */
	public int getID() {
		return id;
	}
	/**
	 * 
	 * @return Name
	 */
	public String getName() {
		return name;
	}
	/**
	 *
	 * @return Description
	 */
	public String getDescription() {
		return description;
	}
    /**
     *
     * @param direction The direction to add the exit
     * @param exit The exit to add
     */
    private void add(final Direction direction, final Exit exit) {
        exits.put(direction, exit);
    }
    /**
     * 
     * @param itemName The name of item
     * @param i The item
     */
    private void add(final String itemName, final Item i) {
        items.put(itemName, i);
    }
    /**
     * 
     * @param n Name of NPC
     * @param npc NPC to add
     */
    private void add(final String n, final NPC npc) {
		npcs.put(n, npc);
	}    
    /**
     * 
     * @param direction The direction
     * @return True if there's an exit
     */
    boolean hasExit(final Direction direction) {
        return exits.containsKey(direction);
    }
    /**
     * Retrieve the exit in a given direction.
     * @param direction The direction to get the exit for
     * @return The exit
     */
    Exit getExit(final Direction direction) {
        return exits.get(direction);
    }
    /**
     * Output all the exits.
     * @return A comma separated list of exits
     */
    String printExits() {
        Iterator<Direction> i = exits.keySet().iterator();
        String exitStr = "";
        if (i.hasNext()) {
            while (i.hasNext()) {
                exitStr +=  i.next().toString();
            }
        } else {
            exitStr = "None";
        }
        return exitStr;
    }
    /**
     * 
     * @param item Name of the item
     * @return The item that was taken
     */
    Item takeItem(final String item) {
        return items.remove(item);
    }
    /**
     * 
     * @param item The item to check for
     * @return True if the item exists, else false
     */
    boolean hasItem(final String item) {
        return items.containsKey(item);
    }
    /**
     * 
     * @param item The item to be dropped at this location
     */
    void dropItem(final Item item) {
        items.put(item.getName(), item);
    }
    /**
     * 
     * @param entity The name of the item to look for.
     * @return The item
     */
	public Item infoItem(final String entity) {
		return items.get(entity);
	}
	/**
	 * 
	 * @param npc The name of the NPC
	 * @return True if present
	 */
	public boolean hasNPC(final String npc) {
		return npcs.containsKey(npc);
	}
	/**
	 * 
	 * @param npc The name of the NPC
	 * @return The NPC
	 */
	public NPC getNPC(final String npc) {
		return npcs.get(npc);
	}

    
}
