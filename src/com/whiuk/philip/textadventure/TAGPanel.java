/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.whiuk.philip.textadventure;

import java.awt.Panel;

/**
 *
 * @author Philip
 */
public abstract class TAGPanel extends Panel {
    /**
     * 
     */
	private static final long serialVersionUID = 3206710632478465770L;

	/**
	 * 
	 * @param string
	 */
	abstract void setErrorMessage(String string);
}
