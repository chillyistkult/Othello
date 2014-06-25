package view;

import controller.Logic;
import model.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Menu panel
 */
public class MenuPanel extends JPanel implements ActionListener {
    /**
     * Game controller
     */
    private Logic logic;

    private JButton newGameButton;

    private JButton quitGameButton;

    /**
     * Panel für Menü
     * @param s
     * @param c
     */
    public MenuPanel(Game s, Logic c) {
        logic = c;

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setBackground(Color.lightGray);
        newGameButton = new JButton("Neues Spiel");
        newGameButton.setMargin(new Insets(0, 50, 0, 50));
        newGameButton.addActionListener(this);
        add(newGameButton);

        quitGameButton = new JButton("Spiel beenden");
        quitGameButton.setMargin(new Insets(0, 50, 0, 50));
        quitGameButton.addActionListener(this);
        add(quitGameButton);
    }

    /**
     * Event listener für die Buttons
     * @param ae
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        JButton b = (JButton) ae.getSource();

        if (b == newGameButton) {
            logic.newGame();
        } else if (b == quitGameButton) {
            logic.quitGame();
        }
    }
}
