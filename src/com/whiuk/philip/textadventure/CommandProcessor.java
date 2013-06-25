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

    /**
     *
     */
	protected static GUI gui;
    /**
     *
     */
	private static Game game;
	/**
	 * @param g Game
	 * @param g2 GUI
	 * @param command Command to process
	 * @param more Further text
	 */
	public static void process(final Game g, final GUI g2, final String command, final String more) {
		gui = g2;
		game = g;
		if (command.equals("HELP")) {
            processHelp(more);
        } else if (command.equals("MOVE")) {
            processMove(more);
        } else if (command.equals("TAKE")) {
            processTake(more);
        } else if (command.equals("DROP")) {
            processDrop(more);
        } else if (command.equals("INVENTORY")) {
        	processInventory(more);
        } else if (command.equals("EXAMINE")) {
        	processExamine(more);
        } else if (command.equals("EQUIP")) {
        	processEquip(more);
        } else if (command.equals("UNEQUIP")) {
        	processUnequip(more);
        } else if (command.equals("EQUIPMENT")) {
        	processEquipment(more);
        } else if (command.equals("TALK")) {
        	processTalk(more);
        } else if (command.equals("ATTACK")) {
        	processAttack(more);
        } else if (command.equals("QUESTS")) {
        	processQuests(more);
        } else if (command.equals("STATS") || command.equals("STATISTICS")) {
        	processStats(more);
        } else {
            gui.addMessageLine("Action Not Found");
        }
	}

	/**
	 * Process help commands.
	 * @param input Item to get help on.
	 */
	protected static void processHelp(final String input) {
        if (input.equals("")) {
            gui.addMessageLine("A list of commands follows. "
                    + "For details on each one, type 'HELP [COMMAND]' accordingly. "
                    + "Parameters in < > are mandatory, those in [ ] are optional");
            gui.addMessageLine("HELP [COMMAND]");
            gui.addMessageLine("LOAD <FILE>");
            gui.addMessageLine("START");
            gui.addMessageLine("MOVE <COMPASS POINT>");
            gui.addMessageLine("INVENTORY, EQUIPMENT, DROP <ITEM NAME>, "
                    + "TAKE <ITEM NAME>, EQUIP <ITEM NAME>, "
                    + "UNEQUIP <ITEM NAME>, EXAMINE <ITEM NAME>, "
                    + "STATS [ITEM NAME]");
            gui.addMessageLine("TALK <NPC NAME> [OPTION], EXAMINE <NPC NAME>");
            gui.addMessageLine("QUESTS");
        } else {
            String[] iArr = input.split(" ");
            String command = iArr[0];
            switch(command) {
                case "HELP":
                    gui.addMessageLine(
                            "HELP provides a list of commands and their "
                            + "parameters along with explanations of usage");
                    break;
                case "LOAD":
                gui.addMessageLine("LOAD is used to load a scenario.");
                break;
            case "START":
                gui.addMessageLine("START is used to play a new game.");
                break;
            case "MOVE":
                gui.addMessageLine(
                        "MOVE allows the player to travel between locations. "
                        + "Note that not all connections are two-way. "
                        + "Available directions of movement are given when "
                        + "arriving at a location.");
                break;
            case "INVENTORY":
                gui.addMessageLine("INVENTORY allows the player to view any items they are carrying.");
                break;
            case "EQUIPMENT":
                gui.addMessageLine("EQUIPMENT allows the player to see any items they have equipped.");
                break;
            case "DROP":
                gui.addMessageLine("DROP allows the player to place an item at a location.");
                break;
            case "TAKE":
                gui.addMessageLine("TAKE allows the player to pickup an item from a location.");
                break;
            case "EQUIP":
                gui.addMessageLine("EQUIP allows the player to wield/wear an "
                        + "item that they are carrying. Any existing equipped "
                        + "item in the same slot will be unequipped automatically.");
                break;
            case "UNEQUIP":
                gui.addMessageLine("UNEQUIP allows the player to remove an item "
                        + "that they have equipped and carry it instead.");
                break;
            case "STATS":
                gui.addMessageLine("STATS allows the player to examine their "
                        + "overall defensive/offensive capabilities as well per item.");
                break;
            case "EXAMINE":
                gui.addMessageLine("EXAMINE allows the player to "
                        + "view a description of the NPC or item.");
                break;
            case "TALK":
                gui.addMessageLine("TALK allows the player to communicate"
                        + "with an NPC. When in a conversation they "
                        + "may be able to choose from multiple options "
                        + "to proceed with the conversation - these "
                        + "can be selected by the optional [OPTION] "
                        + "parameter.");
                break;
            case "QUESTS":
                gui.addMessageLine("QUESTS allows the player to view a "
                        + "list of quests they have been told about, "
                        + "started and completed.");
                break;
            }
        }
    }
	/**
	 * Process inventory commands.
	 * @param more Further command data
	 */
    private static void processInventory(final String more) {
    	Iterator<Entry<String, Item>> i = game.getInventory().entrySet().iterator();
    	if (i.hasNext()) {
            while (i.hasNext()) {
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
    private static void processTake(final String item) {
        if (game.getLocation().hasItem(item)) {
        	Item i = game.getLocation().takeItem(item);
        	game.getInventory().put(item, i);
            gui.addMessageLine("Picked up: " + i.getTitle());
        } else {
        	gui.addMessageLine("No such item: " + item);
        }
    }
    /**
     * Process drop commands.
     * @param item Item
     */
    private static void processDrop(final String item) {
        if (game.getInventory().containsKey(item)) {
        	Item i = game.getInventory().remove(item);
        	game.getLocation().dropItem(i);
        	gui.addMessageLine("Dropped: " + i.getTitle());
        } else {
        	gui.addMessageLine("No such item: " + item);
        }
    }
    /**
     * Process move command.
     * @param direction Direction
     */
    private static void processMove(final String direction) {
        try {
            Direction d = Exit.Direction.valueOf(direction);
            if (game.getLocation().hasExit(d)) {
                game.move(game.getLocations().get(game.getLocation().getExit(d).location));
            } else {
                gui.addMessageLine("Unable to move that way");
            }
        } catch (IllegalArgumentException iae) {
            gui.addMessageLine("Direction Not Found");
        }
    }
    /**
     * Process an examine command.
     * @param item Item to examine
     */
    private static void processExamine(final String item) {
    	if (game.getLocation().hasItem(item)) {
    		gui.addMessageLine(game.getLocation().infoItem(item).getDescription());
    	} else if (game.getInventory().containsKey(item)) {
    		gui.addMessageLine(game.getInventory().get(item).getDescription());
    	}
    }
    /**
     * Process an equip command.
     * @param item Item
     */
    private static void processEquip(final String item) {
    	if (game.getInventory().containsKey(item)) {
    		Item i2 = null;
    		if (game.getInventory().get(item).canEquip()) {
    			Item i  = game.getInventory().remove(item);
    			i2 = game.getEquipment().put(i.getSlot(), i);
    			if (i2 != null) {
    				game.getInventory().put(i2.getName(), i2);
    			}
    			gui.addMessageLine("Equipped: " + i.getTitle());
    		} else {
    			gui.addMessageLine("That item can't be equipped.");
    		}
    	} else {
    		gui.addMessageLine("Not carrying: " + item);
    	}
    }
    /**
     * Process an un-equip command.
     * @param slot Slot to remove item from.
     */
    private static void processUnequip(final String slot) {
    	if (game.getEquipment().containsKey(slot) && game.getEquipment().get(slot) != null) {
    		Item i = game.getEquipment().put(slot, null);
    		game.getInventory().put(i.getName(), i);
    		gui.addMessageLine("Unequipped: " + i.getTitle());
    	} else {
    		gui.addMessageLine("No item was equipped in: " + slot);
    	}
    }
    /**
     * 
     * @param more
     */
    private static void processEquipment(final String more) {
    	Iterator<Entry<String, Item>> i = game.getEquipment().entrySet().iterator();
    	if (i.hasNext()) {
            while (i.hasNext()) {
            	Entry<String, Item> e = i.next();
                gui.addMessageLine(e.getKey() + ": " + e.getValue().getTitle());
            }
        } else {
            gui.addMessageLine("None");
        }
    }
    /**
     * 
     * @param more
     */
    private static void processTalk(final String more) {
    	String[] split = more.split(" ", 2);
    	if (game.getLocation().hasNPC(split[0])) {
    		if (split.length > 1) {
    			gui.addMessageLine(game.getLocation().getNPC(split[0]).doTalk(split[1]));
    		} else {
    			gui.addMessageLine(game.getLocation().getNPC(split[0]).doTalk(""));
    		}
    	} else {
    		gui.addMessageLine("That NPC does not exist here.");
    	}
    }
    /**
     * 
     * @param target
     */
    private static void processAttack(final String target) {
        //TODO: Process Attack
    }
    /**
     * 
     * @param more
     */
    private static void processQuests(final String more) {
    	Iterator<Entry<Quest, Status>> i = game.getPlayerQuests().entrySet().iterator();
    	if (i.hasNext()) {
            while (i.hasNext()) {
            	Entry<Quest, Status> e = i.next();
                gui.addMessageLine(e.getKey().getTitle() + ": " + e.getValue().toString());
            }
        } else {
            gui.addMessageLine("None");
        }
    }
    /**
     * 
     * @param more
     */
    private static void processStats(final String more) {
        //TODO: Refactor deep looping
    	if (more.equals("")) {
	    	HashMap<String, Integer> statistics = new HashMap<String, Integer>();
	    	Iterator<Entry<String, Item>> i = game.getEquipment().entrySet().iterator();
	    	if (i.hasNext()) {
	            while (i.hasNext()) {
	            	Item item = i.next().getValue();
	            	Iterator<Entry<String, Integer>> j = item.getStats().entrySet().iterator();
	                while (j.hasNext()) {
	                	Entry<String, Integer> stat = j.next();
	                	if (statistics.containsKey(stat.getKey())) {
	                		statistics.put(stat.getKey(), statistics.get(stat.getKey()) + stat.getValue());
	                	} else {
	                		statistics.put(stat.getKey(), stat.getValue());
	                	}
	                }
	            }
	    	}
	    	Iterator<Entry<String, Integer>> k = statistics.entrySet().iterator();
	    	if (k.hasNext()) {
	    		while (k.hasNext()) {
	    			Entry<String, Integer> s = k.next();
	    			gui.addMessageLine(s.getKey() + ": " + s.getValue());
	    		}
	    	} else {
	            gui.addMessageLine("No offensive/defensive bonuses");
	        }
    	}
	}
}
