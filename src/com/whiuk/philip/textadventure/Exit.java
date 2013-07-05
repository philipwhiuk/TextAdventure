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
         * @param message Message
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
    private Direction direction;
    /**
     * 
     */
    private int location;
    /**
     * 
     * @param l location
     * @param d direction
     * @throws GameFileException Exception
     */
    Exit(final String l, final String d) throws GameFileException {
        this.location = Integer.parseInt(l);
        try {
            this.setDirection(Direction.valueOf(d));
        } catch (IllegalArgumentException iae) {
            throw new InvalidDirectionGameFileException(
                    "Exit.Direction." + d + " doesn't exist", iae);
        }
    }

    /**
     * @return the location
     */
    public int getLocation() {
        return location;
    }
    /**
     * @return the direction
     */
    public Direction getDirection() {
        return direction;
    }
    /**
     * @param direction the direction to set
     */
    public void setDirection(final Direction direction) {
        this.direction = direction;
    }
    
}
