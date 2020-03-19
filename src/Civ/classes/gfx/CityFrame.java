package Civ.classes.gfx;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

public class CityFrame extends JFrame {


    public static void main(String[] args) {
        CityFrame cf = new CityFrame();
    }

    public Image buildings;
    public Image bg;
    public CityLayout cityLayout;
    public ArrayList<BuildingGfx> buildingsGfx = new ArrayList<>();

    public int tileSize;

    public CityFrame() {
        setTitle("CivNations");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0,0, screenSize.width, screenSize.height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(screenSize.width, screenSize.height));
        buildings = Toolkit.getDefaultToolkit().getImage("data/rulesets/default/cityview/roman2.png"); //Toolkit.getDefaultToolkit().getImage("data/rulesets/default/cities.png");
        bg = Toolkit.getDefaultToolkit().getImage("data/rulesets/default/cityview/grasslandbg.jpg");
        tileSize = 64;
        loadJsonBuildings();
        buildCityLayout();
        CityPanel cityPanel = new CityPanel();
        getContentPane().add(cityPanel);
        setVisible(true);
    }

    public void loadJsonBuildings() {
        JSONParser parser = new JSONParser();
        try (Reader reader = new FileReader("data/rulesets/default/cityview/roman2.json")) {
            JSONObject json = (JSONObject) parser.parse(reader);
            JSONArray jsonBuildings = (JSONArray)json.get("buildings");
            for(int i = 0; i < jsonBuildings.size(); i++) {
                JSONObject jsonBuilding = (JSONObject)jsonBuildings.get(i);
                BuildingGfx buildingGfx = new BuildingGfx();
                buildingGfx.name = getStr(jsonBuilding,"name");
                buildingGfx.symbol = getStr(jsonBuilding, "symbol");
                buildingGfx.x = getFloat(jsonBuilding ,"x");
                buildingGfx.y = getFloat(jsonBuilding ,"y");
                buildingGfx.w = getFloat(jsonBuilding ,"w");
                buildingGfx.h = getFloat(jsonBuilding ,"h");
                buildingGfx.dx = getFloat(jsonBuilding ,"dx");
                buildingGfx.dy = getFloat(jsonBuilding ,"dy");
                buildingGfx.size = getInt(jsonBuilding, "size");
                buildingsGfx.add(buildingGfx);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public String getStr(JSONObject obj, String name) {
        return obj.get(name) != null ? obj.get(name).toString() : "";
    }

    public int getInt(JSONObject obj, String name) {
        return str2int(getStr(obj,name));
    }

    public int str2int(String str) {
        return str.isEmpty() ? 0 : Integer.parseInt(str);
    }

    public float getFloat(JSONObject obj, String name) {
        return str2float(getStr(obj,name));
    }

    public float str2float(String str) {
        return str.isEmpty() ? 0 : Float.parseFloat(str);
    }


    public void buildCityLayout() {
        String[] buildings = new String[]{ "palace", "colosseum", "circus","barracks", "granary", "marketplace", "temple", "library", "amphitheater", "aqueduct", };
        cityLayout = new CityLayout(14, buildings, true, buildingsGfx);
    }


    public class CityPanel extends JPanel {
        @Override
        public void paintComponent(Graphics g) {
            // Important to call super class method
            super.paintComponent(g);
            paint(g);
        }


        public void paint(Graphics g) {
/*          int dx = (this.getWidth() - bg.getWidth(null)) / 2;
            int dy = (this.getHeight() - bg.getHeight(null)) / 2;
            g.translate(this.getWidth() / 2, this.getHeight() / 2);
            g.translate(-bg.getWidth(null) / 2, -bg.getHeight(null) / 2); */
            g.drawImage(bg, 0, 0, getWidth(), getHeight(),null);

            int offsetX = (getWidth() - (cityLayout.cityLayoutMatrixSize * tileSize)) / 2;
            int offsetY = getHeight()  / 2;
           // offsetX = 0 ;//offsetY = 0;
            BuildingGfx buildingGfx;

            for (int y = cityLayout.cityLayoutMatrixSize - 1; y >= 0; y--) {
                for (int x = 0; x < cityLayout.cityLayoutMatrixSize; x++) {

                    buildingGfx = cityLayout.getBuilding(x,  y);
                    if(buildingGfx == null) {
                        continue;
                    }

                    // Upper left corner of this building rect in source
                    int sx = (int)(tileSize * buildingGfx.x);
                    int sy = (int)(tileSize / 2 * buildingGfx.y);

                    // upper left corner of building in destinton
                    int w = (int)(tileSize * (buildingGfx.w));
                    int h = (int)(tileSize / 2 * (buildingGfx.h));
                    int dx =  (int)(buildingGfx.w - 1);

                    double cidx = y + x - dx  + buildingGfx.dx;
                    double cidy = x - y  + buildingGfx.dy;


                    if(buildingGfx.w > 1) {
                        cidx++;
                        cidy--;
                    }

                    int zPlus =  (int)(buildingGfx.h - buildingGfx.w) * tileSize / 2;
                    int cx = offsetX + (int)(cidx * tileSize / 2);
                    int cy = offsetY + (int)(cidy * tileSize / 4) - zPlus;


                    g.drawImage(buildings, cx, cy,
                            cx + w,
                            cy + h,
                            sx,
                            sy,
                            sx + w,
                            sy + h,
                            this);
                }

            /*
            int offsetX = 0;// (getWidth() - (15 * tileSize));
            int offsetY = (int)((7.5 * tileSize) * 1.2);

            System.out.println(getWidth() +  " - "  + (20 * tileSize) + " = " + offsetX);

            for (int x = 0; x < 30; x++) {
                for (int y = 0; y < 30; y++) {
                    // Upper left corner of this terrain rect
                    int px = tileSize;
                    int py = tileSize;


                    int cx = offsetX + ((y + x) * tileSize / 2);
                    int cy = offsetY + ((x - y) * tileSize / 4); // - zPlus


                    g.drawImage(buildings, cx, cy,
                            cx + tileSize,
                            cy + tileSize,
                            py,
                            px,
                            py + tileSize,
                            px + tileSize,
                            this);
                } */
            }
        }
    }
}
