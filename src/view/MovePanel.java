package view;

import controller.Logic;
import model.AllowedMovesIterator;
import model.BoardSquare;
import model.Game;
import model.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

/**
 * Statusanzeige
 */
public class MovePanel extends JPanel implements Observer {

    private Game state;

    private Logic logic;

    private JLabel playerLabel;

    private JLabel pointsLabel;

    private JButton passButton;

    private ImageIcon blackIcon;

    private ImageIcon whiteIcon;

    /**
     * Konstruktor
     *
     * @param s
     * @param c
     */
    public MovePanel(Game s, Logic c) {
        state = s;
        state.addObserver(this);

        logic = c;

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setBackground(Color.lightGray);

        playerLabel = new JLabel();
        add(playerLabel);

        pointsLabel = new JLabel();
        add(pointsLabel);

        add(Box.createGlue());

        passButton = new JButton("Aussetzen");
        passButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                logic.pass();
            }
        });
        passButton.setEnabled(false);
        add(passButton);

        blackIcon = new ImageIcon(getClass().getResource("assets/schwarz.png"));
        whiteIcon = new ImageIcon(getClass().getResource("assets/weiß.png"));

        update(null, null);
    }

    /**
     * Wenn sich etwas am Spielbrett ändert wird der Status aktualisiert
     *
     * @param o
     * @param o1
     */
    @Override
    public void update(Observable o, Object o1) {
        playerLabel.setIcon(null);
        playerLabel.setText(null);
        pointsLabel.setText(null);
        passButton.setEnabled(false);

        int blackScore = 0, whiteScore = 0;

        for (BoardSquare sq : state.getBoard()) {
            if (sq.getPlayer() == Player.BLACK) {
                ++blackScore;
            } else if (sq.getPlayer() == Player.WHITE) {
                ++whiteScore;
            }
        }

        switch (state.getState()) {
            case ENDED:
                playerLabel.setText("Schwarz: " + blackScore +
                        ". Weiß: " + whiteScore + ". " +
                        (blackScore != whiteScore
                                ? (
                                (blackScore > whiteScore && blackScore != whiteScore ? "Schwarz" : "Weiß") +
                                        " gewinnt.")
                                : ""));
                break;
            case INTERACTIVE:
                Player p = state.getCurrentPlayer();

                if (p != Player.NONE) {
                    playerLabel.setIcon(p == Player.BLACK ? blackIcon : whiteIcon);
                    pointsLabel.setText("   Schwarz: " + blackScore + " Weiß: " + whiteScore);
                }

                playerLabel.requestFocus();

                if (!new AllowedMovesIterator(state).hasNext()) {
                    if (new AllowedMovesIterator(new Game(state.getBoard().getX(), state.getBoard().getY(), state, state.getOpponentPlayer())).hasNext()) {
                        passButton.setEnabled(true);

                    } else {
                        logic.endGame();
                    }
                }
                break;
        }
    }
}
