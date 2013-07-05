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
     * Utility class.
     */
    private CommandProcessor() {
    }

    /**
     *
     */
	private static UI screen;
    /**
     *
     */
	private static Game game;
	/**
	 * @param g Game
	 * @param s GUI
	 * @param command Command to process
	 * @param more Further text
	 */
	public static void process(final Game g, final UI s, final String command, final String more) {
		screen = s;
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
            screen.addMessageLine("Action Not Found");
        }
	}

	/**
	 * Process help commands.
	 * @param input Item to get help on.
	 */
	protected static void processHelp(final String input) {
        if (input.equals("")) {
            screen.addMessageLine("A list of commands follows. "
                    + "For details on each one, type 'HELP [COMMAND]' accordingly. "
                    + "Parameters in < > are mandatory, those in [ ] are optional");
            screen.addMessageLine("HELP [COMMAND]");
            screen.addMessageLine("LOAD <FILE>");
            screen.addMessageLine("START");
            screen.addMessageLine("MOVE <COMPASS POINT>");
            screen.addMessageLine("INVENTORY, EQUIPMENT, DROP <ITEM NAME>, "
                    + "TAKE <ITEM NAME>, EQUIP <ITEM NAME>, "
                    + "UNEQUIP <ITEM NAME>, EXAMINE <ITEM NAME>, "
                    + "STATS [ITEM NAME]");
            screen.addMessageLine("TALK <NPC NAME> [OPTION], EXAMINE <NPC NAME>");
            screen.addMessageLine("QUESTS");
        } else {
            String[] iArr = input.split(" ");
            String command = iArr[0];
            switch(command) {
                case "HELP":
                    screen.addMessageLine(
                            "HELP provides a list of commands and their "
                            + "parameters along with explanations of usage");
                    break;
                case "LOAD":
                screen.addMessageLine("LOAD is used to load a scenario.");
                break;
            case "START":
                screen.addMessageLine("START is used to play a new game.");
                break;
            case "MOVE":
                screen.addMessageLine(
                        "MOVE allows the player to travel between locations. "
                        + "Note that not all connections are two-way. "
                        + "Available directions of movement are given when "
                        + "arriving at a location.");
                break;
            case "INVENTORY":
                screen.addMessageLine(
                        "INVENTORY allows the player to view any items they are carrying.");
                break;
            case "EQUIPMENT":
                screen.addMessageLine(
                        "EQUIPMENT allows the player to see any items they have equipped.");
                break;
            case "DROP":
                screen.addMessageLine(
                        "DROP allows the player to place an item at a location.");
                break;
            case "TAKE":
                screen.addMessageLine(
                        "TAKE allows the player to pickup an item from a location.");
                break;
            case "EQUIP":
                screen.addMessageLine("EQUIP allows the player to wield/wear an "
                        + "item that they are carrying. Any existing equipped "
                        + "item in the same slot will be unequipped automatically.");
                break;
            case "UNEQUIP":
                screen.addMessageLine("UNEQUIP allows the player to remove an item "
                        + "that they have equipped and carry it instead.");
                break;
            case "STATS":
                screen.addMessageLine("STATS allows the player to examine their "
                        + "overall defensive/offensive capabilities as well per item.");
                break;
            case "EXAMINE":
                screen.addMessageLine("EXAMINE allows the player to "
                        + "view a description of the NPC or item.");
                break;
            case "TALK":
                screen.addMessageLine("TALK allows the player to communicate"
                        + "with an NPC. When in a conversation they "
                        + "may be able to choose from multiple options "
                        + "to proceed with the conversation - these "
                        + "can be selected by the optional [OPTION] "
                        + "parameter.");
                break;
            case "QUESTS":
                screen.addMessageLine("QUESTS allows the player to view a "
                        + "list of quests they have been told about, "
                        + "started and completed.");
                break;
            default:
                throw new UnsupportedOperationException(command);
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
            	screen.addMessageLine(i.next().getValue().getTitle());
            }
        } else {
            screen.addMessageLine("None");
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
            screen.addMessageLine("Picked up: " + i.getTitle());
        } else {
        	screen.addMessageLine("No such item: " + item);
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
        	screen.addMessageLine("Dropped: " + i.getTitle());
        } else {
        	screen.addMessageLine("No such item: " + item);
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
                game.move(game.getLocations().get(
                        game.getLocation().getExit(d).getLocation()));
            } else {
                screen.addMessageLine("Unable to move that way");
            }
        } catch (IllegalArgumentException iae) {
            screen.addMessageLine("Direction Not Found");
        }
    }
    /**
     * Process an examine command.
     * @param item Item to examine
     */
    private static void processExamine(final String item) {
    	if (game.getLocation().hasItem(item)) {
    		screen.addMessageLine(game.getLocation().infoItem(item).getDescription());
    	} else if (game.getInventory().containsKey(item)) {
    		screen.addMessageLine(game.getInventory().get(item).getDescription());
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
    			screen.addMessageLine("Equipped: " + i.getTitle());
    		} else {
    			screen.addMessageLine("That item can't be equipped.");
    		}
    	} else {
    		screen.addMessageLine("Not carrying: " + item);
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
    		screen.addMessageLine("Unequipped: " + i.getTitle());
    	} else {
    		screen.addMessageLine("No item was equipped in: " + slot);
    	}
    }
    /**
     * 
     * @param more Further text
     */
    private static void processEquipment(final String more) {
    	Iterator<Entry<String, Item>> i = game.getEquipment().entrySet().iterator();
    	if (i.hasNext()) {
            while (i.hasNext()) {
            	Entry<String, Item> e = i.next();
                screen.addMessageLine(e.getKey() + ": " + e.getValue().getTitle());
            }
        } else {
            screen.addMessageLine("None");
        }
    }
    /**
     * 
     * @param more Further text
     */
    private static void processTalk(final String more) {
    	String[] split = more.split(" ", 2);
    	if (game.getLocation().hasNPC(split[0])) {
    		if (split.length > 1) {
    			screen.addMessageLine(game.getLocation().getNPC(split[0]).doTalk(split[1]));
    		} else {
    			screen.addMessageLine(game.getLocation().getNPC(split[0]).doTalk(""));
    		}
    	} else {
    		screen.addMessageLine("That NPC does not exist here.");
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
     * @param more Further text
     */
    private static void processQuests(final String more) {
    	Iterator<Entry<Quest, Status>> i = game.getPlayerQuests().entrySet().iterator();
    	if (i.hasNext()) {
            while (i.hasNext()) {
            	Entry<Quest, Status> e = i.next();
                screen.addMessageLine(e.getKey().getTitle() + ": " + e.getValue().toString());
            }
        } else {
            screen.addMessageLine("None");
        }
    }
    /**
     * 
     * @param more Further text
     */
    private static void processStats(final String more) {
        //TODO: Refactor deep looping
    	if (more.equals("")) {
	    	HashMap<String, Integer> statistics = new HashMap<String, Integer>();
	    	Iterator<Entry<String, Item>> i = game.getEquipment().entrySet().iterator();
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
	    	Iterator<Entry<String, Integer>> k = statistics.entrySet().iterator();
	    	if (k.hasNext()) {
	    		while (k.hasNext()) {
	    			Entry<String, Integer> s = k.next();
	    			screen.addMessageLine(s.getKey() + ": " + s.getValue());
	    		}
	    	} else {
	            screen.addMessageLine("No offensive/defensive bonuses");
	        }
    	}
	}

    /**
     * 
     * @param ui
     */
    public static void setScreen(final UI ui) {
        screen = ui;
    }
}
