
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Random;

public class Map2 extends JPanel {



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

    public static final int PREFERRED_GRID_SIZE_PIXELS = 9;

    public HashMap tileMap = new HashMap();

    // In reality you will probably want a class here to represent a map tile,
    // which will include things like dimensions, color, properties in the
    // game world.  Keeping simple just to illustrate.
    private Color[][] terrainGrid;

    public Map2()  {

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
            Random r = new Random();
            // Randomize the terrain
            for (int y = 0; y < HEIGHT; y++) {
                row = (String) terrain.get(y);
                for (int x = 0; x < row.length(); x++) {
                    String t = Character.toString(row.charAt(x));
                    System.out.print(t);
                    this.terrainGrid[y][x] = (Color) tileMap.get(t);
                }
                System.out.println();
            }
            int preferredWidth = WIDTH * PREFERRED_GRID_SIZE_PIXELS;
            int preferredHeight = HEIGHT * PREFERRED_GRID_SIZE_PIXELS;
            setPreferredSize(new Dimension(preferredWidth, preferredHeight));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        // Important to call super class method
        super.paintComponent(g);

        if(this.terrainGrid == null) {
            return;
        }

        // Clear the board
        g.clearRect(0, 0, getWidth(), getHeight());
        // Draw the grid
        int rectWidth = getWidth() / WIDTH;
        int rectHeight = getHeight() / HEIGHT;

        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                // Upper left corner of this terrain rect
                int x = i * rectWidth;
                int y = j * rectHeight;
                Color terrainColor = terrainGrid[j][i];
                g.setColor(terrainColor);
                g.fillRect(x, y, rectWidth, rectHeight);
            }
        }
    }

    public static void main(String[] args) {
        // http://docs.oracle.com/javase/tutorial/uiswing/concurrency/initial.html
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("Game");
                Map2 map = new Map2();
                frame.add(map);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}