/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package textadventure;

import java.applet.Applet;

/**
 *
 * @author Philip
 */
public class TAGApplet extends Applet {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;  
    /**
     * 
     */
    private TAGPanel screen;
    @Override
    public void init() {
    	screen = new GUI(this,this.getCodeBase());
        add(screen);
        setSize(getPreferredSize());
    }
    /**
     * 
     */
    public void start() {
    }
    /**
     * 
     * @return Screen
     */
    public TAGPanel getScreen() {
    	return screen;
    }
}
