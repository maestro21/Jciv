package Civ.classes.gfx;

import Civ.classes.Coords;
import Civ.classes.Game;
import Civ.classes.ScreenCoords;
import Civ.entities.Ruleset;
import Civ.entities.Terrain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GamePanel extends JFrame {

    Game game;
    public ScreenCoords screenCoords;
    int tileSize;
    int imgTileSize;
    Dimension screenSize;

    public GamePanel(Game game) {
        this.game = game;
        init();
    }

    private void init() {
        initScreenCoords();

        setTitle("CivNations");
        setBounds(0,0, screenSize.width, screenSize.height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        this.tileSize = game.ruleset.tileSize;
        this.imgTileSize = game.ruleset.imgTileSize;
        MapPanel mapPanel = new MapPanel();

        mapPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                handleClick(x,y);
                repaint();
            }
        });


        getContentPane().add(mapPanel);
        setVisible(true);
    }

    public void initScreenCoords() {
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setPreferredSize(new Dimension(screenSize.width, screenSize.height));
        screenCoords = new ScreenCoords(this.game.map.size.x, this.game.map.size.y, this.game.ruleset.tileSize, false);
        screenCoords.setScreenSize(screenSize.width, screenSize.height);
        screenCoords.goTo(this.game.map.size.x / 2, this.game.map.size.y / 2);
    }

    public void handleClick(int x, int y) {
        screenCoords.click(x,y);
    }


    public class MapPanel extends JPanel {
        @Override
        public void paintComponent(Graphics g) {
            // Important to call super class method
            super.paintComponent(g);
            paint(g);
        }


        public void paint(Graphics g) {
            if (game.map == null) {
                return;
            }

            // Clear the board
            g.clearRect(0, 0, getWidth(), getHeight());

            drawWater(g);
            drawCoast(g);
            drawTerrain(g);
        }


        public void drawWater(Graphics g) {

        }

        public void drawCoast(Graphics g) {

        }


        public void drawTerrain(Graphics g) {

            for (int x = 0; x < screenCoords.screenSizeInTiles.x; x++) {
                for (int y = 0; y < screenCoords.screenSizeInTiles.y; y++) {
                    int dTileX = screenCoords.screenMapOffset.x + x;
                    int dTileY = screenCoords.screenMapOffset.y + y;

                    if (dTileX < 0 || dTileX > screenCoords.mapSize.x ||
                            dTileY < 0 || dTileY > screenCoords.mapSize.y) {
                        continue;
                    }

                    Terrain t = game.map.getTile(dTileY, dTileX).terrain;

                    if (t.isWater()) {
                        continue;
                    }

                    // Upper left corner of this terrain rect
                    int px = x * tileSize - 16;
                    int py = y * tileSize - 16;

                    Coords tilePos = t.pos;

                    g.drawImage(game.gfx.terrain, px, py,
                            px + imgTileSize,
                            py + imgTileSize,
                            tilePos.x * imgTileSize,
                            tilePos.y * imgTileSize,
                            (tilePos.x + 1) * imgTileSize,
                            (tilePos.y + 1) * imgTileSize,
                            this);
                }
            }
        }
    }
}
