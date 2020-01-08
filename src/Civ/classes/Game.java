package Civ.classes;

import Civ.classes.gfx.GameFrame;
import Civ.entities.Ruleset;

import javax.swing.*;

public class Game extends JPanel {

    public Map map;
    public Ruleset ruleset;
    public GameOptions gameOptions;
    public Gfx gfx;
    public GameFrame gameFrame;

    public Game(GameOptions gameOptions){
        this.gameOptions = gameOptions;
        ruleset = new Ruleset(gameOptions.ruleset);
        map = new Map(this, gameOptions.map);
        start();
    }

    public void start() {
        this.gfx = new Gfx(this);
        this.gameFrame = new GameFrame(this);
    }

}
