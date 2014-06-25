package view;

import controller.Logic;
import model.Game;
import model.Game.Type;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author admin
 */
public class GUI {
    /**
     * Game state
     */
    private Game state;
    
    /**
     * Create new GUI view
     * @param s 
     */
    public GUI(Game s) {
        state = s;
    }

    public interface IGameTypeSelector {
        public void gameTypeSelected(Type t);
    }

    /**
     * Show main game window
     * @param logic
     */
    public void showMain(final Logic logic) {
        //Thread für GUI
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                //Hauptfenster wird erstellt
                MainPanel p = new MainPanel(state, logic);
                
                JFrame f = new JFrame();
                
                f.add(p);
                f.setTitle("Reversi");
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.pack();
                f.setLocationRelativeTo(null);
                f.setVisible(true);
                f.setBackground(Color.GREEN);
            }
        });
    }

    /**
     * Show select game type dialog
     */
    public void showSelectGameType(IGameTypeSelector s) {
        String[] options = new String[Game.Type.values().length];
        options[Game.Type.ONE_PLAYER.ordinal()] = "Einzelspieler";
        options[Game.Type.TWO_PLAYER.ordinal()] = "Mehrspieler";
        
        int n = JOptionPane.showOptionDialog(
            null,
            "Wähle den Spielmodus aus",
            "Spielmodus",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            (Object[]) options,
            null
        );
        
        if (n == -1) {
            n = Game.Type.ONE_PLAYER.ordinal();
        }
        
        s.gameTypeSelected(Game.Type.values()[n]);
    }
    
}
