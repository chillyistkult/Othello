package model;

import java.util.Iterator;

/**
 * Iteriert über das Spielbrett in alle Richtungen ausgehend von einem gegebenen Spielstein
 */
public class BoardCardinalIterator implements Iterator<BoardSquare>, Iterable<BoardSquare> {

    private Board board;

    private int startRow;

    private int startColumn;

    private int currentRow;

    private int currentColumn;

    private int currentCardinal;

    private boolean justAdvanced;
    
    /**
     * Richtungen
     */
    private static final int cardinals[][] = {
        { -1, 0 }, // Noden
        { -1, 1 }, // Nord-Osten
        { 0, 1 }, // Osten
        { 1, 1 }, // Süd-Osten
        { 1, 0 }, // Süden
        { 1, -1 }, // Süd-Westen
        { 0, -1 }, // Westen
        { -1, -1 } // Nord-Westen
    };
    
    /**
     * Neuer Richtungsiterator
     * @param b Spielbrett
     * @param r Row
     * @param c Column
     */
    public BoardCardinalIterator(Board b, int r, int c) {
        board = b;
        startRow = currentRow = r;
        startColumn = currentColumn = c;
        currentCardinal = 0;
    }

    /**
     * true wenn es ein weiteren Spielstein gibt
     * @return true or false
     */
    @Override
    public boolean hasNext() {
        //Wenn aktuelle Zeile größer 0 und kleiner der X-Dimension des Spielbrettes ist
        return currentRow >= 0 && currentRow < board.getX() &&
                //Wenn aktuelle Spalte größer 0 und kleiner der y-Dimension des Spielbrettes ist
                currentColumn >= 0 && currentColumn < board.getY() &&
                //Aktuelle Richtung ist kleiner als die maximale Anzahl möglicher Richtungen
                currentCardinal < cardinals.length;
    }

    /**
     * Gibt den nächsten Spielstein zurück
     * @return 
     */
    @Override
    public BoardSquare next() {
        BoardSquare ret = new BoardSquare(currentRow, currentColumn, board.get(currentRow, currentColumn));
        
        currentRow += cardinals[currentCardinal][0];
        currentColumn += cardinals[currentCardinal][1];
        
        justAdvanced = false;
        
        if (currentRow < 0 || currentRow >= board.getX() || currentColumn < 0 || currentColumn >= board.getY()) {
            currentRow = startRow;
            currentColumn = startColumn;
            ++currentCardinal;
            justAdvanced = true;
        }
        
        return ret;
    }

    /**
     * Gibt sich selbst zurück (BoardCarinalIterator)
     * @return 
     */
    @Override
    public Iterator<BoardSquare> iterator() {
        return this;
    }
    
    /**
     * Nächste Richtung
     */
    public void advanceCardinal() {
        if (!justAdvanced) {
            currentRow = startRow;
            currentColumn = startColumn;
            ++currentCardinal;
        }
    }
}
