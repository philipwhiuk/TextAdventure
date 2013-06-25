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

    static class InvalidDirectionGameFileException extends GameFileException {
        private static final long serialVersionUID = 1L;
        InvalidDirectionGameFileException(String message, Exception e) {
            super(message,e);
        }
    }

    enum Direction{NORTH,EAST,SOUTH,WEST,UP,DOWN}
    
    Direction direction;    
    int location;
    Exit(String location, String direction) throws GameFileException {
        this.location = Integer.parseInt(location);
        try {
            this.direction = Direction.valueOf(direction);
        } catch (IllegalArgumentException iae) {
            throw new InvalidDirectionGameFileException("Exit.Direction."+direction+" doesn't exist",iae);
        }
    }
    
}
