import Civ.classes.ScreenCoords;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;

public class Map4 extends JPanel {

    /** will use gfx and ruleset **/

    public static final Color OCEAN = new Color(0, 0, 136);
    public static final Color LAKE = new Color(0,0,255);
    public static final Color GRASSLAND = new Color(0, 136, 0);
    public static final Color FOREST = new Color(32, 87, 2);
    public static final Color HILLS = new Color(136, 136, 136);
    public static final Color MOUNTAINS = new Color(68, 68, 68);
    public static final Color JUNGLE = new Color(0,255,0);

    public static final Color PLAINS = new Color(255, 174, 0);
    public static final Color DESERT = new Color(255,255,0);
    public static final Color FLOODPLAIN = new Color(255,0,255);
    public static final Color TAIGA = new Color(0,128,128);
    public static final Color SNOW = new Color(255,255,255);
    public static final Color SWAMP = new Color(0,0,0);

    public int WIDTH;
    public int HEIGHT;
    public int TILESIZE = 64;

    public HashMap tileMap = new HashMap();


    public ScreenCoords screenCoords;

    // In reality you will probably want a class here to represent a map tile,
    // which will include things like dimensions, color, properties in the
    // game world.  Keeping simple just to illustrate.
    private Color[][] terrainGrid;

    private Image terrain;

    public Map4()  {

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        terrain = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("data/ruleset/default/terrain.png"));

        JSONParser parser = new JSONParser();
        JSONObject jsonObject;
        try (Reader reader = new FileReader("data/map.json")) {
            jsonObject = (JSONObject) parser.parse(reader);
            JSONArray terrain = (JSONArray)jsonObject.get("terrain");
            String row = (String) terrain.get(0);
            HEIGHT = terrain.size();
            WIDTH = row.length();

            tileMap.put(" ", OCEAN);
            tileMap.put(".", LAKE);
            tileMap.put("g", GRASSLAND);
            tileMap.put("f", FOREST);
            tileMap.put("h", HILLS);
            tileMap.put("p", PLAINS);
            tileMap.put("m", MOUNTAINS);
            tileMap.put("j", JUNGLE);
            tileMap.put("d", DESERT);
            tileMap.put("l", FLOODPLAIN);
            tileMap.put("t", SNOW);//TAIGA);
            tileMap.put("a", SWAMP);
            tileMap.put("s", SNOW);


            this.terrainGrid = new Color[HEIGHT][WIDTH];
            for (int y = 0; y < HEIGHT; y++) {
                row = (String) terrain.get(y);
                for (int x = 0; x < row.length(); x++) {
                    String t = Character.toString(row.charAt(x));
                    System.out.print(t);
                    this.terrainGrid[y][x] = (Color) tileMap.get(t);
                }
                System.out.println();
            }
            setPreferredSize(new Dimension(screenSize.width, screenSize.height));
            screenCoords = new ScreenCoords(WIDTH,HEIGHT, TILESIZE, false);
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
                // Upper left corner of this terrain rect
                int px = x * TILESIZE;
                int py = y * TILESIZE;
                Color terrainColor = terrainGrid[dTileY][dTileX];
                g.setColor(terrainColor);
                g.fillRect(px, py, TILESIZE, TILESIZE);
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