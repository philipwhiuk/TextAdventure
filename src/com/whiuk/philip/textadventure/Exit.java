/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.whiuk.philip.textadventure;

import com.whiuk.philip.textadventure.Game.GameFileException;

/**
 *
 * @author Philip
 */
class Exit {

    /**
     * 
     * @author Philip
     *
     */
    static class InvalidDirectionGameFileException extends GameFileException {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;
        /**
         * 
         * @param message
         * @param e
         */
        InvalidDirectionGameFileException(final String message, final Exception e) {
            super(message, e);
        }
    }

    /**
     * 
     * @author Philip
     *
     */
    enum Direction {
        /**
         * 
         */
        NORTH,
        /**
         * 
         */
        EAST,
        /**
         * 
         */
        SOUTH,
        /**
         * 
         */
        WEST,
        /**
         * 
         */
        UP,
        /**
         * 
         */
        DOWN
    }


    /**
     * 
     */
    Direction direction;
    /**
     * 
     */
    int location;
    /**
     * 
     * @param l
     * @param d
     * @throws GameFileException
     */
    Exit(final String l, final String d) throws GameFileException {
        this.location = Integer.parseInt(l);
        try {
            this.direction = Direction.valueOf(d);
        } catch (IllegalArgumentException iae) {
            throw new InvalidDirectionGameFileException("Exit.Direction." + d + " doesn't exist", iae);
        }
    }
    
}
