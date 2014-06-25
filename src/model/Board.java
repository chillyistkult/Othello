package model;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Observable;

/**
 * Stellt die Spielfläche dar
 */
public class Board extends Observable implements Serializable, Iterable<BoardSquare> {

    private int x, y;

    /**
     * Board data
     */
    private Player data[][];
    
    /**
     * True wenn begin() aufgerufen wurde
     */
    private boolean begun;

    /**
     * Neues Spielbrett wird erstellt
     */
    public Board(int x, int y) {
        this.x = x;
        this.y = y;
        data = new Player[x][y];
        begun = false;
        reset();
    }
    
    /**
     *Spielbrett wird gecleart / zurückgesetzt
     */
    public void reset() {
        int i, j;
        
        for (i = 0; i < x; ++i) {
            for (j = 0; j < y; ++j) {
                data[i][j] = Player.NONE;
            }
        }
        
        data[(getX()/2)-1][(getY()/2)] = data[(getX()/2)][(getY()/2)-1] = Player.BLACK;
        data[(getX()/2)-1][(getY()/2)-1] = data[(getX()/2)][(getY()/2)] = Player.WHITE;
        
        setChanged();
        notifyObservers();
    }
    
    /**
     * Spielbrett wird auf einen vorherigen Stand zurückgesetzt
     * @param b
     */
    public void reset(Board b) {
        int i, j;
        
        for (i = 0; i < x; ++i) {
            for (j = 0; j < y; ++j) {
                data[i][j] = b.data[i][j];
            }
        }
        
        setChanged();
        notifyObservers();
    }
    
    /**
     * Gibt den Eigentümer des Spielsteins zurück
     * @param row
     * @param column
     * @return
     */
    public Player get(int row, int column) {
        return data[row][column];
    }
    
    /**
     * Setzt einen neuen Eigentümer für das Feld
     * Observer werden benachrichtigt damit sich auch die visuelle Repräsentation auf dem Spielfeld ändern kann
     * @param row
     * @param column
     * @param value
     */
    public void set(int row, int column, Player value) {
        Player oldValue = data[row][column];
        data[row][column] = value;
        
        if (value != oldValue) {
            setChanged();
        }
        
        if (!begun) {
            notifyObservers();
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


    @Override
    public Iterator<BoardSquare> iterator() {
        return new Iterator<BoardSquare>() {
            private int r = 0, c = 0;

            @Override
            public boolean hasNext() {
                return r < x && c < y;
            }

            @Override
            public BoardSquare next() {
                BoardSquare sq = new BoardSquare(r, c, data[r][c]);
                ++c;
                if (c >= x) {
                    ++r;
                    c = 0;
                }
                return sq;
            }
        };
    }
}
