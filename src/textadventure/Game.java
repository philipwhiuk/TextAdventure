/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package textadventure;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import textadventure.Quest.Status;

/**
 *
 * @author Philip
 */
public class Game {
    protected static class GameFileException extends Exception {
        private static final long serialVersionUID = 1L;
        GameFileException(String message, Exception e) {
            super(message,e);
        }        
    }
    protected static String fileVersion = "1.0";
	private static Game game;
	public static Game getGame() {
		return game;
	}
	public static void setGame(Game _game) {
		game = _game;
	}	
    protected static Game Load(TAGApplet app, URL url, String filename) {
    	InputStream is = Game.class.getResourceAsStream(filename);
    	if(is != null) {  
	    	try {
		        GUI gui = (GUI) app.getScreen();
		        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		        Document doc = dBuilder.parse(is);
		        doc.getDocumentElement().normalize();
		        Element gNode = doc.getDocumentElement();
		        if(gNode.getAttribute("version").equalsIgnoreCase(Game.fileVersion)) {
		        	Game game = Read(gui,gNode);
			        gui.addMessageLine("Loaded: "+game.name);
			        return game;		        	
		        }
	    	} catch (Exception e) {
	    		e.printStackTrace();
	    	}
    	}
        return null;
    }
    private static Game Read(GUI gui,Element gNode) throws IOException, GameFileException {
        Game g = new Game(gui);
        g.start_location = Integer.parseInt(gNode.getAttribute("startLocation"));
        g.name = gNode.getAttribute("name");
        
        NodeList nodes = gNode.getChildNodes();
        for(int n=0; n < nodes.getLength(); n++) {
        	Node nNode = nodes.item(n);
        	if (nNode.getNodeType() == Node.ELEMENT_NODE) {
        		Element eElement = (Element) nNode;
        		if(eElement.getTagName().equals("description")) {
        			g.description = ((Text) eElement.getFirstChild()).getData();        			
        		} else if(eElement.getTagName().equals("startText")) { 
        			g.start_text = ((Text) eElement.getFirstChild()).getData();  
        		} else if(eElement.getTagName().equals("locations")) {
        			NodeList lList = eElement.getChildNodes();
        			for(int lNode = 0; lNode < lList.getLength(); lNode++) {
        				if(lList.item(lNode).getNodeType() == Node.ELEMENT_NODE) {
        					Location l = Location.Read((Element) lList.item(lNode)); 
        					g.add(l.getID(),l);
        				}
        			}
        		} else if(eElement.getTagName().equals("quests")) {
        			NodeList lList = eElement.getChildNodes();
        			for(int lNode = 0; lNode < lList.getLength(); lNode++) {
        				if(lList.item(lNode).getNodeType() == Node.ELEMENT_NODE) {
        					Quest q = Quest.Read((Element) lList.item(lNode)); 
        					g.add(q.getName(),q);
        				}
        			}
        		}
        	}
        }
        return g;
    }	




	private GUI gui;            
    // Game Data;
	private String name;
	private String description;    
	private int start_location;        
	private String start_text;    
	private HashMap<String, Quest> questList;    
	private HashMap<Integer,Location> locations;    
	

	// Game State Data
	private boolean started = false;   


	
	// Player Data
    private HashMap<String,Item> inventory;
    private HashMap<String,Item> equipment;
    private HashMap<Quest,Quest.Status> quests;
	private Location location;
    
    protected Game(GUI gui) {
        this.gui = gui;
        equipment = new HashMap<String,Item>();
        inventory = new HashMap<String,Item>();
        locations = new HashMap<Integer,Location>();
        quests = new HashMap<Quest,Quest.Status>();
        questList = new HashMap<String,Quest>();
    }
    protected void processCommand(String command, String more) {
    	if(started) {
    		CommandProcessor.process(this,gui, command,more);
        } else if(command.equals("START")) {
        	start();
        } else if(command.equals("HELP")) {
        	CommandProcessor.processHelp(more);
        } else {
            gui.addMessageLine("Action Not Found");                
        }
    }    
    protected void start() {
        if(locations.get(start_location) != null) {
        	gui.addMessageLine(start_text);    	
        	move(locations.get(start_location));
        	started = true;
        }
        else {
            gui.addMessageLine("No Start Point!");
        }
    }    
    protected void move(Location l) {
        this.location = l;
        gui.addMessageLine(location.getName());
        gui.addMessageLine(location.getDescription());
        gui.addMessageLine("Exits: "+location.printExits());
    }
    private void add(int ID, Location l) {
        locations.put(ID,l);
    }   
    private void add(String name, Quest q) {
		questList.put(name,q);
	}
    protected void setGUI(GUI gui) {
        this.gui = gui;
    }
	public void showDescription() {
		gui.addMessageLine(description);
	}
	public HashMap<String,Item> getInventory() {
		return inventory;
	}
	public boolean hasQuest(String quest) {
		return questList.containsKey(quest);
	}
	public Location getLocation() {
		return location;
	}
	public HashMap<String, Item> getEquipment() {
		return equipment;
	}
	public HashMap<Integer, Location> getLocations() {
		return locations;
	}
	public Quest getQuest(String quest) {
		return questList.get(quest);
	}
	public Quest.Status getPlayerQuestStatus(Quest quest) {
		return quests.get(quest);
	}
	public void setPlayerQuestStatus(Quest quest, Quest.Status status) {
		quests.put(quest, status);
	}
	public HashMap<Quest, Status> getPlayerQuests() {
		return quests;
	}

}
