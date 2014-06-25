package model;

import java.io.Serializable;

/**
 * Repräsentiert den Owner eines Spielsteins
 */
public enum Player implements Serializable {
    /**
     * Kein Spieler ist an der Reihe
     */
    NONE,
    
    /**
     * Schwarzer Spieler ist am Zug
     */
    BLACK,
    
    /**
     * Weißer Spieler ist am Zug
     */
    WHITE
}
