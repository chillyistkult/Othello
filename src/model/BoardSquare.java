package model;

/**
 * Repräsentiert einen Spielstein auf dem Spielbrett
 */
public class BoardSquare {

    private int row;

    private int column;

    private Player player;

    /**
     * Initialisiert neuen Spielstein, inklusive dessen "Owner"
     *
     * @param r
     * @param c
     */
    public BoardSquare(int r, int c, Player p) {
        row = r;
        column = c;
        player = p;
    }

    public int getRow() {
        return row;
    }


    public int getColumn() {
        return column;
    }

    /**
     * Gibt den Owner des Spielsteins zurück
     *
     * @return
     */
    public Player getPlayer() {
        return player;
    }
}
