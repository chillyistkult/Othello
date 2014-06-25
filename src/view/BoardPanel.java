package view;

import controller.Logic;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

/**
 * Spielbrett
 */
public class BoardPanel extends JPanel implements Observer {

    private int x, y;

    private Game state;

    private JButton buttons[][];

    private ImageIcon blackIcon;

    private ImageIcon whiteIcon;
    
    /**
     * Erstellt neues Spielbrett
     * @param b 
     */
    public BoardPanel(Game s, Logic c, int x, int y) {
        state = s;
        //Fügt Observer hinzu
        state.addObserver(this);
        
        buttons = new JButton[x][y];
        
        setLayout(new GridLayout(x, y));
        this.setBackground(Color.lightGray);
        
        int i, j;
        
        for (i = 0; i < x; ++i) {
            for (j = 0; j < y; ++j) {
                //Spielbrett besteht aus x mal y Buttons
                buttons[i][j] = new JButton();
                buttons[i][j].setPreferredSize(new Dimension(70, 70));
                buttons[i][j].addActionListener(new BoardButtonActionListener(c, i, j));
                add(buttons[i][j]);
            }
        }
        
        blackIcon = new ImageIcon(getClass().getResource("assets/schwarz.png"));
        whiteIcon = new ImageIcon(getClass().getResource("assets/weiß.png"));

        //Spielbrett wird initial refresht um den Startzustand herzustellen
        update(null, null);
    }

    /**
     * Aktualisiert das Spielbrett (wird vom Watcher benachrichtigt)
     * @param o
     * @param o1 
     */
    @Override
    public void update(Observable o, Object o1) {
        Board b = state.getBoard();
        
        int i, j;
        
        for (i = 0; i <b.getX(); ++i) {
            for (j = 0; j < b.getY(); ++j) {

                //Status des "Buttons" wird abgefragt
                Player p = b.get(i, j);

                //Button hat kein Icon und ist deaktiviert
                buttons[i][j].setIcon(null);
                buttons[i][j].setEnabled(false);

                //Wenn Button von Spieler gedrückt wurde
                if (p != Player.NONE) {
                    //Button wird enabled
                    buttons[i][j].setEnabled(true);
                    //entsprechendes Icon wird gesetzt
                    buttons[i][j].setIcon(p == Player.BLACK ? blackIcon : whiteIcon);
                    buttons[i][j].setBackground(Color.red);
                }
            }
        }

        //Wenn Spielzüge gemacht wurden
        if (state.getState() != Game.State.WAITING) {
            //Aktiviert Button die gedrückt werden können
            for (BoardSquare sq : new AllowedMovesIterator(state)) {
                buttons[sq.getRow()][sq.getColumn()].setEnabled(true);
            }
        }
    }
    
    /**
     * Event Listener für die Spielbrett Buttons
     */
    private class BoardButtonActionListener implements ActionListener {

        private Logic logic;

        private int row;

        private int column;

        /**
         * Konstruktor
         * @param ctrl
         * @param r
         * @param c
         */
        public BoardButtonActionListener(Logic ctrl, int r, int c) {
            logic = ctrl;
            row = r;
            column = c;
        }

        /**
         * Event listener für Spielbrett buttons
         * @param ae 
         */
        @Override
        public void actionPerformed(ActionEvent ae) {
            //Wenn auf einen Spielstein geklickt wurde der bereits vergeben ist
            if (state.getBoard().get(row, column) != Player.NONE) {
                return;
            }

            //Ansonsten neuen Stein plazieren
            logic.move(row, column);
        }
        
    }
}
