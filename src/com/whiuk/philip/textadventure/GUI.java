package com.whiuk.philip.textadventure;

import java.awt.BorderLayout;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

/**
 *
 * @author Philip
 */
public class GUI extends TAGScreen implements ActionListener {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Output box rows.
     */
    private static final int OUTPUT_ROWS = 20;
    /**
     * Output box columns.
     */
    private static final int OUTPUT_COLS = 80;
    /**
     * Input field columns.
     */
    private static final int INPUT_COLS = 40;
    /**
     * 
     */
    private Game game;
    /**
     * 
     */
    private TextField input;
    /**
     * 
     */
    private TextArea output;
    /**
     * 
     */
    private URL codebase;
    /**
     * 
     */
    private TAGApplet app;

	/**
	 * Constructor.
	 * @param a Parent applet
	 * @param c Code-base.
	 */
    public GUI(final TAGApplet a, final URL c) {
    	this.app = a;
    	this.codebase = c;
        setLayout(new BorderLayout());
        input = new TextField(INPUT_COLS);
        output = new TextArea("", OUTPUT_ROWS, OUTPUT_COLS,
                TextArea.SCROLLBARS_VERTICAL_ONLY);
        output.setEditable(false);
        output.append("Welcome to the Text Adventure Game System. "
                + "Feel free to LOAD a scenario and START playing. "
                + "Type HELP at any time for a full list of available "
                + "commands.\n");
        output.append("Current Scenarios:\nAtlendor: game.xml\n");
        input.addActionListener(this);
        add(output, BorderLayout.CENTER);
        add(input, BorderLayout.SOUTH);
        input.requestFocus();
    }
    @Override
    public final void actionPerformed(final ActionEvent e) {
        if (e.getSource().equals(this.input)) {
            String inputText = this.input.getText();
            inputText = inputText.toUpperCase();
            if (!inputText.equals("")) {
                this.input.setText("");
                output.append("\n> " + inputText + "\n");
                processInput(inputText);
            }
        }
    }

    /**
     * 
     * @param i
     */
    private void processInput(final String i) {
        String[] iArr = i.split(" ", 2);
    	if (game != null) {
            if (iArr.length > 1) {
                game.processCommand(iArr[0], iArr[1]);
            } else {
                game.processCommand(iArr[0], "");
            }
        } else {
        	if (iArr.length > 1) {
        		if (iArr[0].equalsIgnoreCase("LOAD")) {
        			loadGame(iArr[1]);
        		} else if (iArr[0].equalsIgnoreCase("HELP")) {
        			CommandProcessor.screen = this;
        			CommandProcessor.processHelp(iArr[1]);
        		} else {
            		addMessageLine("Action Not Found"); 
            	}
        	} else if (iArr[0].equalsIgnoreCase("HELP")) {
    			CommandProcessor.screen = this;        		
        		CommandProcessor.processHelp("");
        	} else {
        		addMessageLine("Action Not Found"); 
        	}
        }
    }

    @Override
    final void addMessageLine(final String string) {
        output.append(string + "\n");
    }

    /**
     * 
     * @param g
     */
    final void startGame(final Game g) {
        this.game = g;
        if (game != null) {
            game.start();
        }
    }

    @Override
    final void setErrorMessage(final String string) {
        addMessageLine("SYSTEM ERROR:" + string);
    }
    /**
     * 
     * @param filename
     */
	private void loadGame(final String filename) {
    	game = Game.load(app, codebase, filename);
    	Game.setGame(game);
    	if (game != null) {
	    	game.showDescription();
    	}
	}
}
