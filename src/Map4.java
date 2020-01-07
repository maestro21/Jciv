import Civ.classes.Coords;
import Civ.classes.ScreenCoords;
import Civ.entities.Ruleset;
import Civ.entities.Terrain;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Map4 extends JPanel {

    public int WIDTH;
    public int HEIGHT;

    public Ruleset ruleset;


    public ScreenCoords screenCoords;
    private Terrain[][] terrainGrid;

    private Image terrain;

    public Map4()  {


        ruleset = new Ruleset("default");

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        terrain = Toolkit.getDefaultToolkit().getImage("data/rulesets/default/terrain.png");

        JSONParser parser = new JSONParser();
        JSONObject jsonObject;
        try (Reader reader = new FileReader("data/maps/world210x90/map.json")) {
            jsonObject = (JSONObject) parser.parse(reader);
            JSONArray terrain = (JSONArray)jsonObject.get("terrain");
            String row = (String) terrain.get(0);
            HEIGHT = terrain.size();
            WIDTH = row.length();

            this.terrainGrid = new Terrain[HEIGHT][WIDTH];
            for (int y = 0; y < HEIGHT; y++) {
                row = (String) terrain.get(y);
                for (int x = 0; x < row.length(); x++) {
                    String t = Character.toString(row.charAt(x));
                    this.terrainGrid[y][x] = ruleset.getTerrain(t);
                }
                System.out.println();
            }
            setPreferredSize(new Dimension(screenSize.width, screenSize.height));
            screenCoords = new ScreenCoords(WIDTH,HEIGHT, ruleset.tileSize, false);
            screenCoords.setScreenSize(screenSize.width, screenSize.height);
            screenCoords.goTo(WIDTH / 2, HEIGHT / 2);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    public void paint(Graphics g) {
        System.out.println("PAINT");
        if(this.terrainGrid == null) {
            return;
        }

        // Clear the board
        g.clearRect(0, 0, getWidth(), getHeight());

        for (int x= 0; x < screenCoords.screenSizeInTiles.x; x++) {
            for (int y = 0; y < screenCoords.screenSizeInTiles.y; y++) {
                int dTileX = screenCoords.screenMapOffset.x + x;
                int dTileY = screenCoords.screenMapOffset.y + y;

                if(dTileX < 0 || dTileX > screenCoords.mapSize.x ||
                        dTileY < 0 || dTileY > screenCoords.mapSize.y) {
                    continue;
                }

                Terrain t = terrainGrid[dTileY][dTileX];

                if(t.isWater()) {
                    continue;
                }

                // Upper left corner of this terrain rect
                int px = x * ruleset.tileSize  - 16;
                int py = y * ruleset.tileSize  - 16;

                Coords tilePos = t.pos;



                g.drawImage(terrain, px, py,
                        px + ruleset.imgTileSize,
                        py + ruleset.imgTileSize,
                        tilePos.x * ruleset.imgTileSize,
                        tilePos.y * ruleset.imgTileSize,
                        tilePos.x * ruleset.imgTileSize + ruleset.imgTileSize,
                        tilePos.y * ruleset.imgTileSize + ruleset.imgTileSize,
                        this);
            }
        }
    }

    public void handleClick(int x, int y) {
        screenCoords.click(x,y);
    }


    @Override
    public void paintComponent(Graphics g) {
        // Important to call super class method
        super.paintComponent(g);
        paint(g);
    }

    public static void main(String[] args) {
        // http://docs.oracle.com/javase/tutorial/uiswing/concurrency/initial.html
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("Game");
                Map4 map = new Map4();
                frame.add(map);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);


                map.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        int x = e.getX();
                        int y = e.getY();
                        map.handleClick(x,y);
                        map.repaint();
                    }
                });
            }
        });
    }
}