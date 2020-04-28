package Civ.classes;

import Civ.entities.Player;

import java.awt.*;
import java.awt.image.ImageObserver;

public class Gfx {

    public Game game;

    public Image terrain, cities, flags;

    Gfx(Game game) {
        this.game = game;
        loadGfx();
    }

    public void loadGfx() {
        String rulesetPath = "data/rulesets/" + game.gameOptions.ruleset;
        terrain = Toolkit.getDefaultToolkit().getImage(rulesetPath + "/terrain.png");
        cities = Toolkit.getDefaultToolkit().getImage(rulesetPath + "/cities.png");
        flags = Toolkit.getDefaultToolkit().getImage(rulesetPath + "/flags.png");
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
        Coords flagCoords = game.ruleset.getFlagCoords(flag);
        if(flagCoords == null) return;
        g.drawImage(flags, x, y,
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
