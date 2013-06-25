/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.whiuk.philip.textadventure;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Philip
 */
class GameCanvas extends Canvas {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Canvas width.
     */
    private static final int CANVAS_WIDTH = 200;
    /**
     * Canvas height.
     */
    private static final int CANVAS_HEIGHT = 200;

    /**
     *
     */
    GameCanvas() {
        super();
        setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
    }
    
    @Override
    public void paint(final Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, getSize().width, getSize().height);
    }
    @Override
    public void update(final Graphics g) {
        paint(g);
    }
    
}
