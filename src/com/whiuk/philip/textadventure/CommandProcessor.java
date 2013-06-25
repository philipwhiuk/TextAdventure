package com.whiuk.philip.textadventure;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.whiuk.philip.textadventure.Exit.Direction;
import com.whiuk.philip.textadventure.Quest.Status;


/**
 * Processes game commands.
 * @author Philip
 * @since 1.0.0
 */
public class CommandProcessor {

	protected static GUI gui;
	private static Game game;
	/**
	 * @param _game Game
	 * @param _gui GUI
	 * @param command Command to process
	 * @param more Further text
	 */
	public static void process(Game _game, GUI _gui, String command, String more) {
		gui = _gui;
		game = _game;
		if(command.equals("HELP")) {
            processHelp(more);
        } else if(command.equals("MOVE")) {
            processMove(more);
        } else if(command.equals("TAKE")) {
            processTake(more);            
        } else if(command.equals("DROP")) {
            processDrop(more);
        } else if(command.equals("INVENTORY")) {
        	processInventory(more);
        } else if(command.equals("EXAMINE")) {
        	processExamine(more);
        } else if(command.equals("EQUIP")) {
        	processEquip(more);
        } else if(command.equals("UNEQUIP")) {
        	processUnequip(more);
        } else if(command.equals("EQUIPMENT")) {
        	processEquipment(more);
        } else if(command.equals("TALK")) {
        	processTalk(more);
        } else if(command.equals("ATTACK")) {
        	processAttack(more);
        } else if(command.equals("QUESTS")) {
        	processQuests(more);        	
        } else if(command.equals("STATS") || command.equals("STATISTICS")) {
        	processStats(more);
        } else {
            gui.addMessageLine("Action Not Found");                
        }
	}

	/**
	 * Process help commands
	 * @param input Item to get help on.
	 */
	protected static void processHelp(String input) {
        if(input.equals("")) {
            gui.addMessageLine("A list of commands follows. For details on each one, type 'HELP [COMMAND]' accordingly. " +
            		"Parameters in < > are mandatory, those in [ ] are optional");
            gui.addMessageLine("HELP [COMMAND]");
            gui.addMessageLine("LOAD <FILE>");
            gui.addMessageLine("START");
            gui.addMessageLine("MOVE <COMPASS POINT>");
            gui.addMessageLine("INVENTORY, EQUIPMENT, DROP <ITEM NAME>, TAKE <ITEM NAME>, EQUIP <ITEM NAME>, UNEQUIP <ITEM NAME>, EXAMINE <ITEM NAME>, STATS [ITEM NAME]");
            gui.addMessageLine("TALK <NPC NAME> [OPTION], EXAMINE <NPC NAME>");
            gui.addMessageLine("QUESTS");                        
        } else {
            String[] iArr = input.split(" ");
            String command = iArr[0];
            if(command.equals("HELP")) {
                gui.addMessageLine("HELP provides a list of commands and their parameters along with explanations of usage");                
            } else if(command.equals("LOAD")) {
                gui.addMessageLine("LOAD is used to load a scenario.");                
            } else if(command.equals("START")) {
                gui.addMessageLine("START is used to play a new game.");
            } else if(command.equals("MOVE")) {
                gui.addMessageLine("MOVE allows the player to travel between locations. Note that not all connections are two-way. Available directions of movement are given when arriving at a location.");
            } else if(command.equals("INVENTORY")) {
                gui.addMessageLine("INVENTORY allows the player to view any items they are carrying.");
            } else if(command.equals("EQUIPMENT")) {
                gui.addMessageLine("EQUIPMENT allows the player to see any items they have equipped.");
            } else if(command.equals("DROP")) {
                gui.addMessageLine("DROP allows the player to place an item at a location.");
            } else if(command.equals("TAKE")) {
                gui.addMessageLine("TAKE allows the player to pickup an item from a location.");
            } else if(command.equals("EQUIP")) {
                gui.addMessageLine("EQUIP allows the player to wield/wear an item that they are carrying. Any existing equipped item in the same slot will be unequipped automatically.");
            } else if(command.equals("UNEQUIP")) {
                gui.addMessageLine("UNEQUIP allows the player to remove an item that they have equipped and carry it instead.");
            } else if(command.equals("STATS")) {
                gui.addMessageLine("STATS allows the player to examine their overall defensive/offensive capabilities as well per item.");
            } else if(command.equals("EXAMINE")) {
                gui.addMessageLine("EXAMINE allows the player to view a description of the NPC or item.");
            } else if(command.equals("TALK")) {
                gui.addMessageLine("TALK allows the player to communicate with an NPC. When in a conversation they may be able to choose from multiple options to proceed with the conversation - these can be selected by the optional [OPTION] parameter.");
            } else if(command.equals("QUESTS")) {
                gui.addMessageLine("QUESTS allows the player to view a list of quests they have been told about, started and completed.");
            }
        }
    }
	/**
	 * Process inventory commands
	 * @param more Further command data
	 */
    private static void processInventory(String more) {
    	Iterator<Entry<String, Item>> i = game.getInventory().entrySet().iterator();
    	if(i.hasNext()) {
            while(i.hasNext()) {
            	gui.addMessageLine(i.next().getValue().getTitle());
            }
        } else {
            gui.addMessageLine("None");
        }    	
    }
    /**
     * Process take commands.
     * @param item Item
     */
    private static void processTake(String item) {
        if(game.getLocation().hasItem(item)) {
        	Item i = game.getLocation().takeItem(item);
        	game.getInventory().put(item, i);
            gui.addMessageLine("Picked up: "+i.getTitle());
        } else {
        	gui.addMessageLine("No such item: "+item);
        }
    }
    /**
     * Process drop commands.
     * @param item Item
     */
    private static void processDrop(String item) {
        if(game.getInventory().containsKey(item)) {
        	Item i = game.getInventory().remove(item);
        	game.getLocation().dropItem(i);
        	gui.addMessageLine("Dropped: "+i.getTitle());
        } else {
        	gui.addMessageLine("No such item: "+item);
        }
    }    
    /**
     * Process move command.
     * @param direction Direction
     */
    private static void processMove(String direction) {
        try {
            Direction d = Exit.Direction.valueOf(direction);
            if(game.getLocation().hasExit(d)) {
                game.move(game.getLocations().get(game.getLocation().getExit(d).location));
            }
            else {
                gui.addMessageLine("Unable to move that way");
            }
        } catch (IllegalArgumentException iae) {
            gui.addMessageLine("Direction Not Found");                
        }        
    }
    /**
     * Process an examine command
     * @param item Item to examine
     */
    private static void processExamine(String item) {
    	if(game.getLocation().hasItem(item)) {
    		gui.addMessageLine(game.getLocation().infoItem(item).getDescription());
    	} else if(game.getInventory().containsKey(item)) {
    		gui.addMessageLine(game.getInventory().get(item).getDescription());
    	}
    }
    /**
     * Process an equip command
     * @param item Item
     */
    private static void processEquip(String item) {
    	if(game.getInventory().containsKey(item)) {
    		Item i2 = null;
    		if(game.getInventory().get(item).canEquip()) {
    			Item i  = game.getInventory().remove(item);
    			i2 = game.getEquipment().put(i.getSlot(), i);
    			if(i2 != null) {
    				game.getInventory().put(i2.getName(), i2);
    			}
    			gui.addMessageLine("Equipped: "+i.getTitle());
    		} else {
    			gui.addMessageLine("That item can't be equipped.");
    		}
    	} else {
    		gui.addMessageLine("Not carrying: "+item);
    	}
    }
    /**
     * Process an un-equip command
     * @param slot Slot to remove item from.
     */
    private static void processUnequip(String slot) {
    	if(game.getEquipment().containsKey(slot) && game.getEquipment().get(slot) != null) {
    		Item i = game.getEquipment().put(slot, null);
    		game.getInventory().put(i.getName(), i);
    		gui.addMessageLine("Unequipped: "+i.getTitle());
    	} else {
    		gui.addMessageLine("No item was equipped in: "+slot);
    	}
    }
    private static void processEquipment(String more) {
    	Iterator<Entry<String, Item>> i = game.getEquipment().entrySet().iterator();
    	if(i.hasNext()) {
            while(i.hasNext()) {
            	Entry<String,Item> e = i.next();
                gui.addMessageLine(e.getKey()+": "+e.getValue().getTitle());
            }
        } else {
            gui.addMessageLine("None");
        }    	
    }
    private static void processTalk(String more) {
    	String[] split = more.split(" ",2);
    	if(game.getLocation().hasNPC(split[0])) {
    		if(split.length > 1) {
    			gui.addMessageLine(game.getLocation().getNPC(split[0]).doTalk(split[1]));
    		} else {
    			gui.addMessageLine(game.getLocation().getNPC(split[0]).doTalk(""));
    		}
    	} else {
    		gui.addMessageLine("That NPC does not exist here.");
    	}
    }
    private static void processAttack(String item) {
    	
    }
    private static void processQuests(String more) {
    	Iterator<Entry<Quest, Status>> i = game.getPlayerQuests().entrySet().iterator();
    	if(i.hasNext()) {
            while(i.hasNext()) {
            	Entry<Quest,Status> e = i.next();
                gui.addMessageLine(e.getKey().getTitle()+": "+e.getValue().toString());
            }
        } else {
            gui.addMessageLine("None");
        }    	
    }
    private static void processStats(String more) {
    	if(more.equals("")) {    	
	    	HashMap<String,Integer> statistics = new HashMap<String,Integer>();
	    	Iterator<Entry<String, Item>> i = game.getEquipment().entrySet().iterator();
	    	if(i.hasNext()) {
	            while(i.hasNext()) {
	            	Item item = i.next().getValue();
	            	Iterator<Entry<String, Integer>> j = item.getStats().entrySet().iterator();
	                while(j.hasNext()) {
	                	Entry<String, Integer> stat = j.next();
	                	if(statistics.containsKey(stat.getKey())) {
	                		statistics.put(stat.getKey(), statistics.get(stat.getKey())+stat.getValue());
	                	}
	                	else {
	                		statistics.put(stat.getKey(),stat.getValue());
	                	}
	                }
	            }
	    	} 
	    	Iterator<Entry<String, Integer>> k = statistics.entrySet().iterator();
	    	if(k.hasNext()) {
	    		while(k.hasNext()) {
	    			Entry<String, Integer> s = k.next();
	    			gui.addMessageLine(s.getKey()+": "+s.getValue());
	    		}
	    	} else {
	            gui.addMessageLine("No offensive/defensive bonuses");
	        } 
    	}	
	}
}
