/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.whiuk.philip.textadventure;

import java.applet.Applet;

/**
 *
 * @author Philip
 */
public class TAGApplet extends Applet implements TAGContainer {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;  
    /**
     * 
     */
    private UI screen;
    @Override
    public final void init() {
    	screen = new GUI(this, this.getCodeBase());
        add(screen);
        setSize(getPreferredSize());
    }
    /**
     * 
     */
    public void start() {
    }

    @Override
    public final UI getScreen() {
    	return screen;
    }
}
