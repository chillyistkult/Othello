package controller;

import model.AllowedMovesIterator;
import model.BoardCardinalIterator;
import model.BoardSquare;
import model.Game;
import view.GUI;

import java.util.*;

public class Logic {

    private Game state;

    private GUI view;

    /**
     * Wichtungen für die Computer AI
     * http://www.policyalmanac.org/games/aStarTutorial.htm
     */
    private static final byte[][] scores = {
            {99, -8, 8, 6, 6, 8, -8, 99},
            {-8, -24, -4, -3, -3, -4, -24, -8},
            {8, -4, 7, 4, 4, 7, -4, 8},
            {6, -3, 4, 0, 0, 4, 3, 6},
            {6, -3, 4, 0, 0, 4, 3, 6},
            {8, -4, 7, 4, 4, 7, -4, 8},
            {-8, -24, -4, -3, -3, -4, -24, -8},
            {99, -8, 8, 6, 6, 8, -8, 99}
    };

    /**
     * Konstruktor
     *
     * @param s Einzel- oder Mehrspieler
     * @param v Spielbrettimplementierung
     */
    public Logic(Game s, GUI v) {
        state = s;
        view = v;
    }

    /**
     * Spielstein wird anhand der Koordinaten gesetzt
     *
     * @param row       x-Koordinate
     * @param column    y-Koordinate
     */
    public void move(int row, int column) {
        state.begin();

        //Stein setzen
        placeStone(row, column);
        //Gegenspieler ist an der Reihe
        state.setCurrentPlayer(state.getOpponentPlayer());

        //Wenn Mehrspielermodus
        if (state.getType() == Game.Type.TWO_PLAYER) {
            state.setState(Game.State.INTERACTIVE);
            state.end();
        //Einzelspieler
        } else if (state.getType() == Game.Type.ONE_PLAYER) {
            //Computer ist an der Reihe
            play();

        }
    }

    /**
     * Plaziert einen Stein auf dem Spielbrett
     *
     * @param row
     * @param column
     */
    private void placeStone(int row, int column) {
        Set<BoardSquare> square = new HashSet<BoardSquare>();
        BoardCardinalIterator it = new BoardCardinalIterator(state.getBoard(), row, column);

        //Eigentümer des Spielsteins wird gesetzt
        state.getBoard().set(row, column, state.getCurrentPlayer());

        //Spielfeld wird geupdatet auf Grundlage des letzten Spielzuges
        for (BoardSquare sq : it) {
            if (sq.getRow() == row && sq.getColumn() == column) {
                square.clear();

            } else if (sq.getPlayer() == state.getOpponentPlayer()) {
                square.add(sq);

            } else if (sq.getPlayer() == state.getCurrentPlayer()) {
                //Eigentümer der Spielsteine wird neu gesetzt
                for (BoardSquare t : square) {
                    state.getBoard().set(t.getRow(), t.getColumn(), state.getCurrentPlayer());
                }
                it.advanceCardinal();

            } else { // sq.getPlayer() == Player.NONE
                it.advanceCardinal();
            }
        }
    }

    /**
     * Computer AI Zug
     */
    private void play() {
        state.setState(Game.State.WAITING);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                //Möglichkeiten zum setzen der Steine
                List<BoardSquare> legalMoves = new LinkedList();

                for (BoardSquare sq : new AllowedMovesIterator(state)) {
                    legalMoves.add(sq);
                }

                //Wenn es Möglichkeiten zum setzen gibt
                if (legalMoves.size() > 0) {
                    BoardSquare max = legalMoves.get(0);

                    for (BoardSquare sq : legalMoves) {
                        //Wenn die Wichtung des aktuellen Feldes größer ist als die der ersten möglichen Platzierung
                        if (scores[sq.getRow()][sq.getColumn()] > scores[max.getRow()][max.getColumn()]) {
                            max = sq;
                        }
                    }

                    //Stein wird platziert
                    placeStone(max.getRow(), max.getColumn());
                }

                //Gegenspieler ist wieder an der Reihe
                state.setCurrentPlayer(state.getOpponentPlayer());
                state.setState(Game.State.INTERACTIVE);

                state.end();
            }
        }, 500);
    }

    /**
     * Zug wird an den Gegenspieler weitergegeben (z.B. wenn kein Zug mehr gemacht werden kann)
     */
    public void pass() {
        state.begin();

        state.setCurrentPlayer(state.getOpponentPlayer());

        if (state.getType() == Game.Type.ONE_PLAYER) {
            play();
        } else {
            state.end();
        }
    }

    /**
     * Startet ein neues Spiel
     */
    public void newGame() {
        view.showSelectGameType(new GUI.IGameTypeSelector() {
            @Override
            public void gameTypeSelected(Game.Type t) {
                state.reset(new Game(state.getBoard().getX(), state.getBoard().getY(), t));
            }
        });
    }

    /**
     * Programm wird beendet
     */
    public void quitGame() {
        System.exit(0);
    }

    /**
     * Aktuelles Spiel unterbrechen
     */
    public void endGame() {
        state.setState(Game.State.ENDED);
    }
}
