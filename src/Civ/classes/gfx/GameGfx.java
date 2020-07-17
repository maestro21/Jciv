package Civ.classes.gfx;

import Civ.classes.Coords;
import Civ.classes.Game;
import Civ.entities.Player;
import Civ.entities.Ruleset;

import java.awt.*;
import java.awt.image.ImageObserver;

public class GameGfx extends Gfx {

    public Game game;

    public String rulesetPath = "data/rulesets/" + Game.gameOptions.ruleset + "/";

    public GameGfx(Game game) {
        this.game = game;
        loadGfx();
    }

    public void loadGfx() {
        loadImg("terrain");
        loadImg("cities");
        loadImg("flags");
    }

    public void loadImg(String key){
        load(key, rulesetPath + key + ".png");
    }


    public void drawFlag(Graphics g, Player player, int x, int y, ImageObserver io) {
        if(player == null || player.getCivNation() == null || player.getCivNation().getFlag().isEmpty()) {
            return;
        }
        drawFlag(g, player.getCivNation().getFlag(), x, y, io);
    }

    public void drawFlag(Graphics g, String flag, int x, int y, ImageObserver io) {
        drawFlag(g, flag, x, y, io, 30, 20);
    }

    public void drawFlag(Graphics g, Player player, int x, int y, ImageObserver io, int fx, int fy) {
        if(player == null || player.getCivNation() == null || player.getCivNation().getFlag().isEmpty()) {
            return;
        }
        drawFlag(g, player.getCivNation().getFlag(), x, y, io, fx, fy);
    }

    public void drawFlag(Graphics g, String flag, int x, int y, ImageObserver io, int fx, int fy) {
        Coords flagCoords = Ruleset.getFlagCoords(flag);
        if(flagCoords == null) return;
        g.drawImage(get("flags"), x, y,
                x + fx,
                y + fy,
                flagCoords.x,
                flagCoords.y,
                flagCoords.x + 44,
                flagCoords.y + 30,
                io);
    }


    int ShiftNorth(int p, int distance) {
        return (p - distance);
    }
    int ShiftSouth(int p, int distance) {
        return (p + distance);
    }
    int ShiftEast(int p, int distance) {
        return (p + distance);
    }
    int ShiftWest(int p, int distance) {
        return (p - distance);
    }

    public void drawOutlineText(Graphics g, String text, int x, int y, Color textColor, Color borderColor) {
        g.setColor(borderColor);
        g.drawString(text, ShiftWest(x, 1), ShiftNorth(y, 1));
        g.drawString(text, ShiftWest(x, 1), ShiftSouth(y, 1));
        g.drawString(text, ShiftEast(x, 1), ShiftNorth(y, 1));
        g.drawString(text, ShiftEast(x, 1), ShiftSouth(y, 1));
        g.setColor(textColor);
        g.drawString(text, x, y);
    }

    public int getStringCenter(Graphics g, String text, Font font) {
        FontMetrics metrics = g.getFontMetrics(font);
        return metrics.stringWidth(text) / 2;
    }
}
