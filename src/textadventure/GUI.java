/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package textadventure;

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
public class GUI extends TAGPanel implements ActionListener {     
    private static final long serialVersionUID = 1L;

    private Game game;
    private TextField input;
    private TextArea output;
	private URL codebase;
	private TAGApplet app;
    
	/**
	 * Constructor.
	 * @param app Parent applet
	 * @param codebase Code-base.
	 */
    public GUI(TAGApplet app, URL codebase) {
    	this.app = app;
    	this.codebase = codebase;
        setLayout(new BorderLayout());
        input = new TextField(40);
        output = new TextArea("",20,80,TextArea.SCROLLBARS_VERTICAL_ONLY);
        output.setEditable(false);
        output.append("Welcome to the Text Adventure Game System. Feel free to LOAD a scenario and START playing. Type HELP at any time for a full list of available commands.\n");
        output.append("Current Scenarios:\nAtlendor: game.xml\n");
        input.addActionListener(this);
        add(output,BorderLayout.CENTER);        
        add(input,BorderLayout.SOUTH);
        input.requestFocus();        
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(this.input)) {
            String input = this.input.getText();
            input = input.toUpperCase();
            if(input != "") {
                this.input.setText("");
                output.append("\n> "+input+"\n");
                processInput(input);
            }
        }
    }

    private void processInput(String input) {
        String[] iArr = input.split(" ",2);   
    	if(game != null) {     	
            if(iArr.length > 1) {
                game.processCommand(iArr[0],iArr[1]);
            }
            else {
                game.processCommand(iArr[0],"");
            }
        } else {
        	if(iArr.length > 1) {
        		if(iArr[0].equalsIgnoreCase("LOAD")) {
        			loadGame(iArr[1]);
        		}
        		else if(iArr[0].equalsIgnoreCase("HELP")) {
        			CommandProcessor.gui = this;
        			CommandProcessor.processHelp(iArr[1]);
        		} else {
            		addMessageLine("Action Not Found"); 
            	}
        	} else if(iArr[0].equalsIgnoreCase("HELP")) {
    			CommandProcessor.gui = this;        		
        		CommandProcessor.processHelp("");
        	} else {
        		addMessageLine("Action Not Found"); 
        	}
        }
    }

    void addMessageLine(String string) {
        output.append(string+"\n");
    }

    void startGame(Game game) {
        this.game = game;
        if(game != null) {
            game.start();
        }
    }

    @Override
    void setErrorMessage(String string) {
        addMessageLine("SYSTEM ERROR:"+string);
    }
	private void loadGame(String filename) {
    	game = Game.Load(app,codebase,filename);
    	Game.setGame(game);
    	if(game != null) {
	    	game.showDescription();
    	}
	}
}
