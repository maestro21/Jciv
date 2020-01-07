package Civ.classes;

import java.awt.*;

public class Gfx {

    public Game game;

    public Image terrain;

    Gfx(Game game) {
        this.game = game;
        loadGfx();
    }

    public void loadGfx() {
        String rulesetPath = "data/rulesets/" + game.gameOptions.ruleset;
        terrain = Toolkit.getDefaultToolkit().getImage(rulesetPath + "/terrain.png");
    }
}
