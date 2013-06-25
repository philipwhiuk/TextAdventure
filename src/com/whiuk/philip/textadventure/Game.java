/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.whiuk.philip.textadventure;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import com.whiuk.philip.textadventure.Quest.Status;


/**
 *
 * @author Philip
 */
public class Game {
    /**
     * 
     * @author Philip
     *
     */
    protected static class GameFileException extends Exception {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;
        /**
         * 
         * @param message
         * @param e
         */
        GameFileException(final String message, final Exception e) {
            super(message, e);
        }
    }
    /**
     * 
     */
    protected static String fileVersion = "1.0";
    /**
     * 
     */
	private static Game game;
	/**
	 * @return Singleton game
	 */
	public static Game getGame() {
		return game;
	}
	/**
	 * @param g Game.
	 */
	public static void setGame(final Game g) {
		game = g;
	}
	/**
	 * 
	 * @param container
	 * @param url
	 * @param filename
	 * @return
	 */
    protected static Game load(final TAGContainer container, final URL url, final String filename) {
    	InputStream is = Game.class.getResourceAsStream(filename);
    	if (is != null) {  
	    	try {
		        TAGScreen screen = container.getScreen();
		        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		        Document doc = dBuilder.parse(is);
		        doc.getDocumentElement().normalize();
		        Element gNode = doc.getDocumentElement();
		        if (gNode.getAttribute("version").equalsIgnoreCase(Game.fileVersion)) {
		        	Game g = read(screen, gNode);
			        screen.addMessageLine("Loaded: " + g.name);
			        return g;
		        }
	    	} catch (Exception e) {
	    		e.printStackTrace();
	    	}
    	}
        return null;
    }
    /**
     * 
     * @param screen
     * @param gNode
     * @return
     * @throws IOException
     * @throws GameFileException
     */
    private static Game read(final TAGScreen screen, final Element gNode) throws IOException, GameFileException {
        Game g = new Game((GUI) screen);
        g.startLocation = Integer.parseInt(gNode.getAttribute("startLocation"));
        g.name = gNode.getAttribute("name");
        
        NodeList nodes = gNode.getChildNodes();
        for (int n = 0; n < nodes.getLength(); n++) {
        	Node nNode = nodes.item(n);
        	if (nNode.getNodeType() == Node.ELEMENT_NODE) {
        		Element eElement = (Element) nNode;
        		if (eElement.getTagName().equals("description")) {
        			g.description = ((Text) eElement.getFirstChild()).getData();        			
        		} else if (eElement.getTagName().equals("startText")) { 
        			g.startText = ((Text) eElement.getFirstChild()).getData();  
        		} else if (eElement.getTagName().equals("locations")) {
        			NodeList lList = eElement.getChildNodes();
        			for (int lNode = 0; lNode < lList.getLength(); lNode++) {
        				if (lList.item(lNode).getNodeType() == Node.ELEMENT_NODE) {
        					Location l = Location.read((Element) lList.item(lNode)); 
        					g.add(l.getID(), l);
        				}
        			}
        		} else if (eElement.getTagName().equals("quests")) {
        			NodeList lList = eElement.getChildNodes();
        			for (int lNode = 0; lNode < lList.getLength(); lNode++) {
        				if (lList.item(lNode).getNodeType() == Node.ELEMENT_NODE) {
        					Quest q = Quest.read((Element) lList.item(lNode)); 
        					g.add(q.getName(), q);
        				}
        			}
        		}
        	}
        }
        return g;
    }	

    /**
     * 
     */
    private TAGScreen gui;
    // Game Data;
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
	private int startLocation;
    /**
     * 
     */
	private String startText;
    /**
     * 
     */
	private HashMap<String, Quest> questList;
    /**
     * 
     */
	private HashMap<Integer, Location> locations;
	

	// Game State Data
    /**
     * 
     */
	private boolean started = false;


	
	// Player Data
    /**
     * 
     */
    private HashMap<String, Item> inventory;
    /**
     * 
     */
    private HashMap<String, Item> equipment;
    /**
     * 
     */
    private HashMap<Quest, Quest.Status> quests;
    /**
     * 
     */
	private Location location;

	/**
	 * 
	 * @param g
	 */
    protected Game(final GUI g) {
        this.gui = g;
        equipment = new HashMap<String, Item>();
        inventory = new HashMap<String, Item>();
        locations = new HashMap<Integer, Location>();
        quests = new HashMap<Quest, Quest.Status>();
        questList = new HashMap<String, Quest>();
    }
    /**
     * 
     * @param command
     * @param more
     */
    protected final void processCommand(final String command, final String more) {
    	if (started) {
    		CommandProcessor.process(this, gui, command, more);
        } else if (command.equals("START")) {
        	start();
        } else if (command.equals("HELP")) {
        	CommandProcessor.processHelp(more);
        } else {
            gui.addMessageLine("Action Not Found");
        }
    }
    /**
     * 
     */
    protected final void start() {
        if (locations.get(startLocation) != null) {
        	gui.addMessageLine(startText);    	
        	move(locations.get(startLocation));
        	started = true;
        } else {
            gui.addMessageLine("No Start Point!");
        }
    }
    /**
     * 
     * @param l
     */
    protected final void move(final Location l) {
        this.location = l;
        gui.addMessageLine(location.getName());
        gui.addMessageLine(location.getDescription());
        gui.addMessageLine("Exits: " + location.printExits());
    }
    /**
     * 
     * @param id identifier
     * @param l location
     */
    private void add(final int id, final Location l) {
        locations.put(id, l);
    }
    /**
     * 
     * @param n name
     * @param q quest
     */
    private void add(final String n, final Quest q) {
		questList.put(n, q);
	}
    /**
     * 
     * @param g GUI
     */
    protected final void setGUI(final GUI g) {
        this.gui = g;
    }
    /**
     * Output the game description.
     */
	public final void showDescription() {
		gui.addMessageLine(description);
	}
	/**
	 * Get the player's inventory.
	 * @return Inventory
	 */
	public final HashMap<String, Item> getInventory() {
		return inventory;
	}
	/**
	 * See if the player has progress in a given quest.
	 * @param quest Quest to check for
	 * @return True if player has progress
	 */
	public final boolean hasQuest(final String quest) {
		return questList.containsKey(quest);
	}
	/**
	 * Get the player's location.
	 * @return Location
	 */
	public final Location getLocation() {
		return location;
	}
	/**
	 * Get the player's equipment.
	 * @return Equipment
	 */
	public final HashMap<String, Item> getEquipment() {
		return equipment;
	}
	/**
	 * Get the mapping of locations.
	 * @return Mapping of location ID to locations.
	 */
	public final Map<Integer, Location> getLocations() {
		return locations;
	}
	/**
	 * Quest in the player's quest list.
	 * @param quest Name of quest
	 * @return Quest
	 */
	public final Quest getQuest(final String quest) {
		return questList.get(quest);
	}
	/**
	 * Get the status of a quest in the player's quest list.
	 * @param quest Name of quest
	 * @return status
	 */
	public final Quest.Status getPlayerQuestStatus(final Quest quest) {
		return quests.get(quest);
	}
	/**
	 * 
	 * @param quest Quest to change status of
	 * @param status Status to set to.
	 */
	public final void setPlayerQuestStatus(final Quest quest, final Quest.Status status) {
		quests.put(quest, status);
	}
	/**
	 * 
	 * @return Mapping between quest and status
	 */
	public final Map<Quest, Status> getPlayerQuests() {
		return quests;
	}

}
