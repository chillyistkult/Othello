package view;

import controller.Logic;
import model.Game;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {
    /**
     * Hauptfenster
     * @param s
     * @param c 
     */
    public MainPanel(Game s, Logic c) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        //Menü
        add(new MenuPanel(s, c));
        //Spielbrett
        add(new BoardPanel(s, c, s.getBoard().getX(), s.getBoard().getY()));
        //Statusanzeige
        add(new MovePanel(s, c));
    }
}
