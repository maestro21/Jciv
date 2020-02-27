import Civ.classes.Game;
import Civ.classes.GameOptions;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JCiv extends JPanel {

    GameOptions gameOptions;
    Game game;


    public void quickStart() {
        gameOptions = new GameOptions();
        gameOptions.ruleset = "default";
        gameOptions.map = "world400x200";
        game = new Game(gameOptions);
    }


    public static void main(String[] args) {
        JCiv jCiv = new JCiv();
        jCiv.quickStart();
    }







}
