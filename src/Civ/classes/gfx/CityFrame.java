package Civ.classes.gfx;

import Civ.classes.Coords;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;
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
    public Image coastBg, coastBgTop, coastBgRight;
    public Image topBg;
    public CityLayout cityLayout;
    public ArrayList<BuildingGfx> buildingsGfx = new ArrayList<>();
    public Coords offset;

    public int tileSize;

    public CityFrame() {
        setTitle("CivNations");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0,0, screenSize.width, screenSize.height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1000, 500));
        buildings = Toolkit.getDefaultToolkit().getImage("data/rulesets/default/cityview/roman2.png"); //Toolkit.getDefaultToolkit().getImage("data/rulesets/default/cities.png");
        bg = Toolkit.getDefaultToolkit().getImage("data/rulesets/default/cityview/grasslandbg2.jpg");
        topBg = Toolkit.getDefaultToolkit().getImage("data/rulesets/default/cityview/topbg.png");
        coastBg = Toolkit.getDefaultToolkit().getImage("data/rulesets/default/cityview/coastBg.png");
        coastBgTop = Toolkit.getDefaultToolkit().getImage("data/rulesets/default/cityview/coastbgtop.png");
        coastBgRight = Toolkit.getDefaultToolkit().getImage("data/rulesets/default/cityview/coastbgright.png");
        tileSize = 64;
        loadJsonBuildings();
        buildCityLayout();
        CityPanel cityPanel = new CityPanel(this);
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

        public Drawer d;
        public ImageObserver io;
        public Graphics g;

        CityPanel(ImageObserver io) {
            super();
            this.io = io;
        }

        @Override
        public void paintComponent(Graphics g) {
            this.g = g;
            // Important to call super class method
            super.paintComponent(g);
            paint(g);
        }

        public Coords getDrawCoords(int x, int y, double dx, double dy) {
            double cidx = y + x + dx;
            double cidy = x - y + dy;
            double mod = 1.1;

            int cx = offset.x + (int)(cidx * tileSize * mod / 2);
            int cy = offset.y + (int)(cidy * tileSize * mod / 4);

            return new Coords(cx, cy);
        }

        public Coords getDrawCoords(int x, int y)  {
            return getDrawCoords(x,y,0,0);
        }

        public Coords getDrawCoords(int x)  {
            return getDrawCoords(x,x);
        }


        public void drawRoads(Graphics g) {
            int cc = cityLayout.cityCenter;
            BuildingGfx roadx = cityLayout.getBuilding("roadv"), roady = cityLayout.getBuilding("roadh");
            for(int i = 0; i < 20; i++) {
                drawBuilding(g,roady, cc - i, cc);
                drawBuilding(g, roadx, cc, cc + i);
            }
            for(int i = 0; i < 30; i++) {
                drawBuilding(g, roady, cc + i, cc);
                drawBuilding(g,roadx, cc, cc - i);
            }
        }

        public void drawBuilding(Graphics g, BuildingGfx buildingGfx, int x, int y) {
            // Upper left corner of this building rect in source
            int sx = (int)(tileSize * buildingGfx.x);
            int sy = (int)(tileSize / 2 * buildingGfx.y);

            // upper left corner of building in destinton
            int w = (int)(tileSize * (buildingGfx.w));
            int h = (int)(tileSize / 2 * (buildingGfx.h));

            double dx = buildingGfx.dx - (buildingGfx.w - 1);
            double dy = buildingGfx.dy - (buildingGfx.h - buildingGfx.w) * 2;
            if(buildingGfx.w > 1) {
                dx++;
                dy--;
            }

            Coords drawCoords = getDrawCoords(x,y,dx,dy);
            int cx = drawCoords.x;
            int cy = drawCoords.y;



            if(buildingGfx.name.equals("aqueduct")) {
                drawAqueduct(g, x,y);
            }

            g.drawImage(buildings, cx, cy,
                    cx + w,
                    cy + h,
                    sx,
                    sy,
                    sx + w,
                    sy + h,
                    this);
        }



        public void drawAqueduct(Graphics g, int x, int y) {
            int offsetX = 5;
            BuildingGfx aqueductx = cityLayout.getBuilding("aqueductx");
            BuildingGfx aqueducth = cityLayout.getBuilding("aqueducth");
            for(int i = 0; i < 10; i++) {
                drawBuilding(g, aqueducth, -offsetX - 1 - i, y - 1 - i);
            }
            for(int i = 0; i < x + offsetX; i++) {
                drawBuilding(g, aqueductx, i - offsetX, y);
            }
        }


        public void draw(Image img, ImgDimensions dim) {
            System.out.println(dim);
            g.drawImage(img, dim.offX, dim.offY, dim.x, dim.y, null);
        }

        public void initDraw(Graphics g) {
            d = new Drawer();
            this.g = g;
            double scaleX = 1.0 * getWidth() / 1750;
            double scaleY = 1.0 *getHeight() / 900;
            float scale = (float)(scaleX > scaleY ? scaleX : scaleY); System.out.println(scale);
            d.setScale(scale).setContainer(getWidth(), getHeight());
        }

        public void paint(Graphics g) {
            initDraw(g);
            draw(bg,d.center().dim(1750, 900));

            BuildingGfx buildingGfx;
            int offsetX = (getWidth() - (int)(cityLayout.cityLayoutMatrixSize * tileSize * 1.2)) / 2;
            int offsetY = (int)(getHeight() / 1.8);
            offset = new Coords(offsetX, offsetY);

            drawRoads(g);

            draw(coastBgTop, d.left().top().dim(1100, 315, 0 + 350,80));
            draw(coastBgRight, d.dim(575, 500, (1100 - 575) + 350, 395));

            //g.drawImage(coastBg, 0,0, getWidth(), getHeight(),null);
          // g.drawImage(coastBgTop, getWidth() - 1080, 85, 1080, 335,null);
           //g.drawImage(coastBgRight, getWidth() - 700, 400, 900, getHeight(),null);

            drawBuilding(g, cityLayout.getBuilding("port"), 18, cityLayout.cityLayoutMatrixSize / 2 + 1);

            int to = cityLayout.cityLayoutMatrixSize - 1;
            for (int y = to; y >= 0; y--) {
                for (int x = 0; x <= to; x++) {

                    buildingGfx = cityLayout.getBuilding(x,  y);
                    if(buildingGfx == null) {
                        continue;
                    }

                    drawBuilding(g, buildingGfx, x,y);
                }
            }
        }
    }
}
