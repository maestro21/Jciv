package Civ.classes;

import Civ.classes.gfx.GamePanel;
import Civ.entities.Ruleset;

import javax.swing.*;

public class Game extends JPanel {

    public Map map;
    public Ruleset ruleset;
    public GameOptions gameOptions;
    public Gfx gfx;
    public GamePanel gamePanel;

    public Game(GameOptions gameOptions){
        this.gameOptions = gameOptions;
        ruleset = new Ruleset(gameOptions.ruleset);
        map = new Map(this, gameOptions.map);
        start();
    }

    public void start() {
        this.gfx = new Gfx(this);
        this.gamePanel = new GamePanel(this);
    }

}
