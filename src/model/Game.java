package model;

import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * Schnittstelle zwischen Frontend View und Backend Logik
 * Die Klasse ist ein Watcher und wird vom BoardPanel benachrichtigt wenn Änderungen eintreten
 * Ebenso ist die Klasse ein Observer und benachrichtig das BoardPanel bei Änderung
 */
public class Game extends Observable implements Serializable, Observer {

    private Player currentPlayer;
    
    // Datenrepräsentation Spielbrett
    private Board board;

    public enum State {

        //Spiel wartet auf Userinput
        INTERACTIVE,

        //Spiel wurde beendet
        ENDED,

        //Spiel wartet auf AI
        WAITING
    }

    private State state;

    public enum Type {
        //Einzelspieler (gegen den Computer)
        ONE_PLAYER,

        //Mehrspieler
        TWO_PLAYER
    }

    private Type type;

    private boolean begun;
    
    /**
     * Ein neues Spiel wird initialisiert
     * @param x
     * @param y
     */
    public Game(int x, int y) {
        board = new Board(x, y);
        board.addObserver(this);
        begun = false;
        reset();
    }
    
    /**
     * Spiel wird zurückgesetzt
     * @param x
     * @param y
     * @param s
     */
    public Game(int x, int y, Game s) {
        this(x, y);
        reset(s);
    }
    
    /**
     * Spiel wird initialisiert
     * @param x
     * @param y
     * @param t Spieltyp
     */
    public Game(int x, int y, Type t) {
        this(x, y);
        reset();
        type = t;
    }
    
    /**
     * Spiel wird geladen und Spieler gesetzt
     */
    public Game(int x, int y, Game s, Player newCurrentPlayer) {
        this(x, y, s);
        currentPlayer = newCurrentPlayer;
    }

    /**
     * Benutzervariablen werden gesetzt
     */
    public void reset() {
        //Welcher Spieler fängt an?
        currentPlayer = Player.BLACK;
        //In welchem Status befindet sich das Spiel zu Anfang?
        state = State.INTERACTIVE;
        //Einzelspieler oder Mehrspieler
        type = Type.TWO_PLAYER;
        board.reset();
    }
    
    /**
     * Benutzervariablen werden aus einem bestimmten Zustand zurückgesetzt
     * @param s 
     */
    public void reset(Game s) {
        currentPlayer = s.currentPlayer;
        state = s.state;
        type = s.type;
        board.reset(s.board);
    }
    
    /**
     * Gibt das Spielbrett zurück
     * @return
     */
    public Board getBoard() {

        return board;
    }
    
    /**
     * Gibt den aktuellen Spieler zurück
     * @return 
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Setzt den aktuellen Spieler
     * @param p 
     */
    public void setCurrentPlayer(Player p) {
        if (p == currentPlayer) {
            return;
        }
        
        currentPlayer = p;
        setChanged();

        //Wenn Spiel noch nicht begonnen hat werden initial die Observer benachrichtigt, damit auf dem Spielbrett der richtige Spieler gesetzt werden kann
        if (!begun) {
            notifyObservers();
        }
    }
    
    /**
     * Gibt den Gegenspieler zurück
     * @return 
     */
    public Player getOpponentPlayer() {
        if (currentPlayer == Player.NONE) {
            return Player.NONE;
        }
        
        return currentPlayer == Player.BLACK ? Player.WHITE : Player.BLACK;
    }

    /**
     * Observer der auf Veränderungen des Spielbretts wartet
     * @param o
     * @param o1 
     */
    @Override
    public void update(Observable o, Object o1) {
        setChanged();
        
        if (!begun) {
            notifyObservers();
        }
    }
    
    /**
     * Begin not notifying observers
     */
    public void begin() {
        begun = true;
    }
    
    /**
     * Schaltet die Observerfunktion ab
     */
    public void end() {
        begun = false;
        notifyObservers();
    }
    
    /**
     * Spielstatus wird zurückgegeben
     * @return
     */
    public State getState() {
        return state;
    }
    
    /**
     * Spielstatus wird gesetzt
     */
    public void setState(State s) {
        if (s != state) {
            setChanged();
        }
        
        state = s;
        
        if (!begun) {
            notifyObservers();
        }
    }
    
    /**
     * Spieltyp wird zurückgegeben
     * @return 
     */
    public Type getType() {
        return type;
    }


}
