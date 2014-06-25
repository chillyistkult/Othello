import controller.Logic;
import model.Game;
import view.GUI;

import javax.swing.*;

public class Main {

    /**
     * Einstiegspunkt
     * @param args
     */
    public static void main(String[] args) {

        //Dialog Spielfeldgröße
        int dimension = 0;
        while(!(dimension >= 6 && dimension <=10)) {
             dimension = Integer.valueOf(JOptionPane.showInputDialog(new JFrame(), "Wie groß soll das Spielbrett sein (6-10)?"));
        }
        //Game Model
        Game s = new Game(dimension, dimension);
        //Frontend View
        GUI v = new GUI(s);
        //Game Logic
        Logic c = new Logic(s, v);
        v.showMain(c);
    }
}
