package model;

import java.util.Iterator;

/**
 * Iteriert durch alle für den Spieler möglichen Zügen
 */
public class AllowedMovesIterator implements Iterator<BoardSquare>, Iterable<BoardSquare> {

    private Game state;

    private Iterator<BoardSquare> boardIterator;

    private BoardSquare nextLegalMove;
    
    /**
     * Konstruktor
     * @param s 
     */
    public AllowedMovesIterator(Game s) {
        state = s;
        boardIterator = s.getBoard().iterator();
        findNextLegalMove();
    }
    
    /**
     * Sucht nach dem nächsten möglichen Spielzug
     */
    private void findNextLegalMove() {
        nextLegalMove = null;

        //Solange noch nicht alle Spielsteine durchlaufen wurden
        while (boardIterator.hasNext()) {
            //Nächster Spielstein
            BoardSquare sq = boardIterator.next();

            //Cardinal Iterator über den aktuellen Spielstein
            BoardCardinalIterator it = new BoardCardinalIterator(state.getBoard(), sq.getRow(), sq.getColumn());
            
            boolean ok = false;

            for (BoardSquare t : it) {
                //Wenn Cardinal Iterator und aktueller Spielstein übereinstimmen dann ist das Blödsinn und es ist natürlich kein legaler Spielzug
                if (t.getRow() == sq.getRow() && t.getColumn() == sq.getColumn()) {
                    ok = false;
                    continue;

                //Wenn der Spielstein vom Gegenspieler kontrolliert wird dann ist das schonmal ein guter Anfang
                } else if (t.getPlayer() == state.getOpponentPlayer()) {
                    ok = true;

                //Wenn der Spielstein vom aktuellen Spieler besitzt wird und der vorherige Spielstein vom Gegner kontrolliert wird, dann ist der Spielstein belegbar
                } else if (t.getPlayer() == state.getCurrentPlayer() && ok) {
                    nextLegalMove = sq;
                    return;
                } else {
                    //Nächste Richtung
                    it.advanceCardinal();
                }
            }
        }
    }

    /**
     * Gibt true zurück wenn es noch möglich erlaubten Spielzüg gibt
     * @return
     */
    @Override
    public boolean hasNext() {
        return nextLegalMove != null;
    }

    /**
     * Gibt den nächsten möglichen Spielzug zurück
     * @return 
     */
    @Override
    public BoardSquare next() {
        BoardSquare m = nextLegalMove;
        findNextLegalMove();
        return m;
    }

    /**
     * Gibt sich selbst zurück
     * @return 
     */
    @Override
    public Iterator<BoardSquare> iterator() {
        return this;
    }
}
