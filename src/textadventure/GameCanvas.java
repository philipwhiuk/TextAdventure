/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package textadventure;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Philip
 */
class GameCanvas extends Canvas{
    private static final long serialVersionUID = 1L;

    GameCanvas() {
        super();
        setSize(200,200);
    }
    
    @Override
    public void paint(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 9, getSize().width, getSize().height);
    }
    @Override
    public void update(Graphics g) {
        paint(g);
    }
    
}
