package Civ.classes.gfx;

import Civ.classes.Buildings;
import Civ.classes.Coords;
import Civ.classes.Game;
import Civ.entities.City;
import Civ.entities.Ruleset;
import Civ.entities.Terrain;
import Civ.entities.Tile;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ImageObserver;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CityFrame extends JFrame {


    public static void main(String[] args) {
        CityFrame cf = new CityFrame();
    }

    public String cityViewPath;

    public CityBuildingsGfxSettings settings = new CityBuildingsGfxSettings();

    public int maxSize = 20;
    public int citySize = 16;
    public CityLayout cityLayout;
    public ArrayList<BuildingGfx> buildingsGfx = new ArrayList<>();
    public Coords offset;
    public int csCounter = 0;

    public String citySet = "classic";
    public JButton rndBtn, incBtn, decBtn, walledBtn, palaceBtn, waterBtn, styleBtn, styleBtn2, ageBtn, relBtn;

    public int tileSize;

    private Double mod = 0.8;

    public Gfx gfx = new Gfx();


    CityFrame() {
        Ruleset.load("default");
        settings.randomizeCity();
        init();
    }


    CityFrame(Tile tile) {
        settings.loadCity(tile);
        init();
    }

    public void loadImg(String key){
        loadImg(key, key + ".png");
    }

    public void loadImg(String key, String src){
        gfx.load(key, cityViewPath + src);
    }


    public void init() {
        cityViewPath = "data/rulesets/" + Ruleset.name + "/cityview/";
        setTitle("CivNations");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0, 0, screenSize.width, screenSize.height);
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(1750, 900));
        setMaximumSize(new Dimension(1750, 900));
        tileSize = 64;
        CityPanel cityPanel = new CityPanel(this);
        loadGfx();
        loadBuildings();
        buildCityLayout();
        getContentPane().add(cityPanel);
        setLayout(null);
        cityPanel.setBounds(0, 50, 1750, 900);
        addButtons();
        setVisible(true);
    }

    public void loadGfx() {
        loadImg("bg","grasslandbg2.jpg");
        loadImg("topBg","leftforest.png");
        loadImg("topBgL","topbgl.png");
        loadImg("topBgR","topbgr.png");
        loadImg("coastBg","coastBg.png");
        loadImg("coastBgTop","coastbgtop.png");
        loadImg("coastBgRight","coast2r.jpg");
        loadImg("coastBgBottom","coastbgright.png");
    }

    public void addButtons() {
        rndBtn = new JButton("Randomize");
        rndBtn.setBounds(0,0,100,30);
        rndBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settings.randomizeCity();
                randomStyle();
                loadBuildings();
                buildCityLayout();
                repaint();
            }
        });


        incBtn = new JButton("+");
        incBtn.setBounds(100,0,50,30);
        incBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(settings.size < maxSize) settings.size++;
                refresh();
            }
        });

        decBtn = new JButton("-");
        decBtn.setBounds(150,0,50,30);
        decBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(settings.size > 1) settings.size--;
                refresh();
            }
        });

        palaceBtn = new JButton("Palace");
        palaceBtn.setBounds(200,0,100,30);
        palaceBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settings.palace = !settings.palace;
                refresh();
            }
        });


        walledBtn = new JButton("Wall");
        walledBtn.setBounds(300,0,75,30);
        walledBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settings.walled = !settings.walled;
                refresh();
            }
        });

        waterBtn = new JButton("Water");
        waterBtn.setBounds(375,0,75,30);
        waterBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settings.hasWater = !settings.hasWater;
                refresh();
            }
        });

        styleBtn = new JButton("Style");
        styleBtn.setBounds(450,0,75,30);
        styleBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextStyle();
                refresh();
            }
        });

        styleBtn2 = new JButton("S-");
        styleBtn2.setBounds(525,0,75,30);
        styleBtn2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lastStyle();
                refresh();
            }
        });

        ageBtn = new JButton("Age");
        ageBtn.setBounds(600,0,70,30);
        ageBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settings.nextAge();
                refresh();
            }
        });

        relBtn = new JButton("Rel");
        relBtn.setBounds(670,0,70,30);
        relBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settings.nextRel();
                refresh();
            }
        });


        add(rndBtn);
        add(incBtn);
        add(decBtn);
        add(palaceBtn);
        add(walledBtn);
        add(waterBtn);
        add(styleBtn);
        add(styleBtn2);
        add(ageBtn);
        add(relBtn);
    }

    public void refresh() {
        settings.refresh();
        buildCityLayout();
        repaint();
    }

    public void nextStyle() {
        csCounter++;
        if(csCounter >= Ruleset.buildingSets.size()) {
            csCounter = 0;
        }
        citySet =  Ruleset.buildingSets.get(csCounter);
        loadBuildings();
    }

    public void lastStyle() {
        csCounter--;
        if(csCounter < 0) {
            csCounter = Ruleset.buildingSets.size() - 1;
        }
        citySet = Ruleset.buildingSets.get(csCounter);
        loadBuildings();
    }

    public void randomStyle() {
        csCounter = (int)(Math.random() * (Ruleset.buildingSets.size() - 1));
        citySet = Ruleset.buildingSets.get(csCounter);
    }

    public void loadBuildings() {
        //citySet = citySets[csCounter];

        loadImg(citySet);
        loadImg("general");
        buildingsGfx.clear();
       // loadJsonWonders();
        loadJsonBuildings("general", true);
        loadJsonBuildings(citySet);
    }

    public void loadJsonBuildings(String name) {
        loadJsonBuildings(name, false);
    }

    public void loadJsonWonders() {
        loadJsonBuildings("wonders", true);
    }

    public void loadJsonBuildings(String name, boolean wonder) {
        JSONParser parser = new JSONParser();
        try (Reader reader = new FileReader(cityViewPath + name + ".json")) {
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
                buildingGfx.age = getStr(jsonBuilding, "age");
                buildingGfx.religion = getStr(jsonBuilding, "religion");
                buildingGfx.fileName = name;
                buildingGfx.wonder = wonder;
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
        cityLayout = new CityLayout(this);
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


        public void drawRoads() {
            int cx = cityLayout.centerM.x;
            int cy = cityLayout.centerM.y;
            String road = settings.isRailroad() ? "romanroad" : settings.roads;
            BuildingGfx roadx = cityLayout.getBuilding(road + "v"), roady = cityLayout.getBuilding(road + "h");

            for(int i = 0; i < 17; i++) {
                drawBuilding(roady, cx - i, cy);
                drawBuilding(roadx, cx, cy + i);
            }
            for(int i = 0; i < 17; i++) {
                drawBuilding(roady, cx + i, cy);
                drawBuilding(roadx, cx, cy - i);
            }
            drawBuilding( road + "x", cx, cy);
        }

        /*
            Double offsetX = 0.0;
            Double offsetY= 0.0;
            roadx.dx = offsetX;
            roadx.dy = offsetY;
            roady.dx = offsetX;
            roady.dy = offsetY;

            BuildingGfx xroad = cityLayout.getBuilding(road + "x");
            xroad.dx = offsetX;
            xroad.dy = offsetY;


            for(int i = 0; i < cityLayout.bmSize.x - 1; i++ ) {
                int d = i * 3 + 4;
                if (cityLayout.isBigStreet() && i >= cityLayout.buildingMatrixSize / 2) d++;
                for (int j = 2; j < cityLayout.cityLayoutMatrixSize.x - (cityLayout.isBigStreet() ? 1 : 2); j++) {
                    if (d != cityLayout.cityCenter.x) {
                        drawBuilding(roady, j, d);
                    }
                }
            }

            for(int i = 0; i < cityLayout.bmSize.y - 1; i++ ) {
                int d = i * 3 + 4;
                for(int j = 2; j< cityLayout.cityLayoutMatrixSize.y - (cityLayout.isBigStreet() ? 1 : 2); j++ ) {
                    if(j != cityLayout.cityCenter.y && d != cityLayout.cityCenter.y + 1) {
                        drawBuilding(roadx, d, j);
                    };
                }
                //drawBuilding(g, xroad, d, d);
            }

            Double mod = 0.8;
            roady.dy = 2;
            if(cityLayout.isBigStreet()) {
                mod = 0.85;
               if(road.equals("road")) {
                    roadx = cityLayout.getBuilding("roadbig");
                    roady = cityLayout.getBuilding("roadbig");
                    xroad = cityLayout.getBuilding("roadbig");
                }

                if(road.equals("romanroad")) {
                    roadx = cityLayout.getBuilding("romanroadbig");
                    roady = cityLayout.getBuilding("romanroadbig");
                    xroad = cityLayout.getBuilding("romanroadbig");
                }

                if(road.equals("highway")) {
                    roadx = cityLayout.getBuilding("highwaybigv");
                    roady = cityLayout.getBuilding("highwaybigh");
                    xroad = cityLayout.getBuilding("highwaybigx");
                }

                //roadx.dx++;
                roady.dy = 0.5;
               // roady.dy--;
                //mod = 1.0;
            }

            for(int i = 0; i < 17; i++) {
                drawBuilding(roady, cx - i, cy, mod);
                drawBuilding(roadx, cx, cy + i, mod);
            }
            for(int i = 0; i < 17; i++) {
                drawBuilding(roady, cx + i, cy, mod);
                drawBuilding(roadx, cx, cy - i, mod);
            }
            if(!settings.isRailroad()) {
                xroad.dx = offsetX;
                xroad.dy = offsetY;
                drawBuilding( xroad, cx, cy);
            }
        } */

        public void drawWallsBack() {
            if(!cityLayout.walled || settings.walls.isEmpty()) {
             //   return;
            }
            int s = 1;
            int ex = cityLayout.sizeM.x - 1; if(!cityLayout.isBigStreet()) ex--;
            int ey =  cityLayout.sizeM.y - 1; if(!cityLayout.isBigStreet()) ey--;

            int cx = cityLayout.centerM.x;
            int cy = cityLayout.centerM.y;

            boolean w4 = cityLayout.wallx4;

            BuildingGfx gatev = cityLayout.getBuilding(w4 ? "gate_v" : "gatev");
            BuildingGfx gateh = cityLayout.getBuilding(w4 ? "gate_h" : "gateh");
            if(cityLayout.isBigStreet()) {
                gatev.dy = -1;
                gatev.dx = 0.5;
                gateh.dx = 0.5;
            }

            drawBuilding(w4 ? "tower_t" : "tower", s, ey);

            for (int i = s; i < ey; i++) {
                drawBuilding( w4 ? "wall_l" : "wallv", s, i);
            }

            for (int i = s; i < ex; i++) {
                drawBuilding(w4 ? "wall_t" : "wallh", i + 1, ey);
            }

            drawBuilding(gatev, s, cy);
            drawBuilding(gateh, cx, ey);

            drawBuilding(w4 ? "tower_l" : "tower", s, s);
            drawBuilding(w4 ? "tower_r" : "tower", ex, ey);
        }

        public void drawWallsFront() {
            if(!cityLayout.walled || settings.walls.isEmpty()) {
              //  return;
            }
            int s = 1;
            int ex = cityLayout.sizeM.x - 1; if(!cityLayout.isBigStreet()) ex--;
            int ey = cityLayout.sizeM.y - 1; if(!cityLayout.isBigStreet()) ey--;
            int cx = cityLayout.centerM.x;
            int cy = cityLayout.centerM.y;
            boolean w4 = cityLayout.wallx4;

            BuildingGfx gatev = cityLayout.getBuilding(w4 ? "gate_v" : "gatev");
            BuildingGfx gateh = cityLayout.getBuilding(w4 ? "gate_h" : "gateh");
            if(cityLayout.isBigStreet()) {
                gatev.dy = -1;
                gatev.dx = 0.5;
                gateh.dx = 0.5;
            }

            for (int i = s; i < ey; i++) {
                drawBuilding( w4 ? "wall_r" : "wallv", ex, i);
            }

            for (int i = s; i < ex; i++) {
                drawBuilding(w4 ? "wall_b" : "wallh", i + 1, s);
            }

            drawBuilding(gatev, ex, cy);
            drawBuilding(gateh, cx, s);

            drawBuilding(w4 ? "tower_b" : "tower", ex, s);
        }


        public void drawBuilding(String name, int x, int y) {
            drawBuilding(name, x, y, 0.8);
        }

        public void drawBuilding(String name, int x, int y, Double mod) {
            BuildingGfx buildingGfx = cityLayout.getBuilding(name);
            drawBuilding(buildingGfx, x, y, mod);
        }

        public void drawBuilding( BuildingGfx buildingGfx, int x, int y) {
            drawBuilding(buildingGfx, x, y, 0.8);
        }

        public void drawBuilding(BuildingGfx buildingGfx, int x, int y, Double mod) {

            //buildingGfx =cityLayout.getBuilding("roadx");
            if(buildingGfx == null) {
                return;
            }

            //int offsetX, offsetY = getBuildingOffset(buildingGfx.name);

            // Upper left corner of this building rect in source
            int sx = (int)(tileSize * buildingGfx.x);
            int sy = (int)(tileSize / 2 * buildingGfx.y);

            // upper left corner of building in destinton
            int w = (int)(tileSize * (buildingGfx.w));
            int h = (int)(tileSize / 2 * (buildingGfx.h));

            int dw = (int)(w * mod);
            int dh = (int)(h * mod);

            double dx = buildingGfx.dx + buildingGfx.w;
            double dy = buildingGfx.dy - (buildingGfx.h - buildingGfx.w) * 2;
           /* if(buildingGfx.w > 1) {
                dx++;
                dy--;
            }

            if(buildingGfx.size > 1) {
                dx = dx - 1;
                dy = dy - 2;

                if(buildingGfx.size == 4) {
                    dy = dy - 2;
                }
            } */

            if(buildingGfx.w > 1) {
                System.out.println(buildingGfx.name);
            }

            Coords drawCoords = getDrawCoords(x,y,dx,dy);
            int cx = drawCoords.x;
            int cy = drawCoords.y;

            if(buildingGfx.name.equals(Buildings.AQUEDUCT)) {
                //drawAqueduct( x,y);
            }

            Image img = gfx.get(buildingGfx.fileName);

            drawTileRect(g,cx,cy + dh - dw / 2,dw,dw / 2);

            /**/g.drawImage(img, cx, cy,
                    cx + dw,
                    cy + dh,
                    sx,
                    sy,
                    sx + w,
                    sy + h,
                    this);/**/
        }


        public void drawTileRect(Graphics g, int sx, int sy, int w, int h) {
            Double mod = 0.8;
            int hh = h / 2;
            int hw = w / 2;
            g.drawLine(sx + hw, sy, sx + w, sy + hh);
            g.drawLine(sx + w, sy + hh, sx + hw, sy + h);
            g.drawLine(sx + hw, sy + h, sx, sy + hh);
            g.drawLine(sx, sy + hh, sx + hw, sy);
        }



        public void drawAqueduct(int x, int y) {
            int offsetX = 5;
            for(int i = 0; i < 7; i++) {
                drawBuilding("aqueductx", -offsetX - 1 - i, y - 1- i);
            }
            for(int i = 0; i < x + offsetX; i++) {
                drawBuilding("aqueducth", i - offsetX, y + 1);
            }
        }

        public void draw(String imgKey,
                                  int dx1, int dy1, int dx2, int dy2,
                                  int sx1, int sy1, int sx2, int sy2,
                                  ImageObserver observer){
            Image img = gfx.get(imgKey);
            if(img == null) return;
            g.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, this);
        }

        public void draw(String imgKey, ImgDimensions dim) {
            Image img = gfx.get(imgKey);
            if(img == null) return;
            g.drawImage(img, dim.offX, dim.offY, dim.x, dim.y, this);
        }

        public void initDraw(Graphics g) {
            d = new Drawer();
            this.g = g;
            double scaleX = 1.0 * getWidth() / 1750;
            double scaleY = 1.0 *getHeight() / 900;
            float scale = (float)(scaleX > scaleY ? scaleX : scaleY); //System.out.println(scale);
            d.setScale(scale).setContainer(getWidth(), getHeight());
        }

        public void drawCityLayout() {
            int toX = cityLayout.sizeB.x;
            int toY = cityLayout.sizeB.y;
            for (int y = toY; y >= 0; y--) {
                for (int x = 0; x < toX; x++) {
                    BuildingGfx buildingGfx = cityLayout.getBuilding(x,  y);
                    if(buildingGfx == null) {
                        continue;
                    }
                    int dx = x * 3 + 2;
                    int dy = y * 3 + 2;
                    drawBuilding( buildingGfx, dx,dy);
                }
            }
        }

        public void paint(Graphics g) {
            initDraw(g);
            draw("bg",d.center().dim(1750, 900));

            int offsetX = (getWidth() - (int)(cityLayout.sizeM.x * tileSize * mod)) / 2;
            int offsetY = (int)(getHeight() / 1.8);
            offset = new Coords(offsetX, offsetY);

            drawRoads();


            if(settings.hasWater) {
                draw("coastBgTop", d.left().top().dim(1100, 315, 500, 75));
                draw("coastBgBottom", d.dim(575, 500, (1100 - 575) + 500, 375));
                draw("coastBgRight", d.dim(430, 800, 1099 + 500, 88));
                drawBuilding( cityLayout.getBuilding("port"), cityLayout.sizeM.x / 2 + 12, cityLayout.sizeM.y / 2 + 1);
               // drawBuilding(g, cityLayout.getBuilding("colossus"), cityLayout.cityLayoutMatrixSize / 2 - 4, cityLayout.cityLayoutMatrixSize / 2 + 11);
               // drawBuilding(g, cityLayout.getBuilding("lighthouse"), cityLayout.cityLayoutMatrixSize / 2, cityLayout.cityLayoutMatrixSize / 2 + 12);
            } else {
                draw("topBgR", d.right().top().dim(672, 173, 0,130));
            }


            draw("topBgL", d.left().top().dim(672, 173, 0,130));


            //drawWallsBack();
            drawCityLayout();
            //drawWallsFront();

            drawCityInfo();
        }



        private void drawCityInfo() {
            Font font = new Font("Serif", Font.BOLD, 50);
            g.setFont(font);
            int x = getWidth() / 2 - getStringCenter(g, settings.name, font) - 32;
            g.drawString(settings.name, x, 50);

            /*
            if(game != null) {
                Game.gfx.drawFlag(g, city.getPlayer(), x - 50, 20, io, 44, 30);
                game.gfx.drawFlag(g, city.getPlayer(), x + getStringCenter(g, cityName, font) * 2 + 10, 20, io, 44, 30);
            }*/

            font = new Font("SansSerif", Font.BOLD, 16);
            g.setFont(font);
            g.drawString(settings.size + " District(s)", 10, 20);

            String text = "(Pop. " + getPopulation() + ")";
            x = getWidth() / 2 - getStringCenter(g, text, font) - 32;
            g.drawString(text, x, 80);
        }

        private String getPopulation() {
            DecimalFormat formatter = new DecimalFormat("#,###");
            int pop = citySize * (citySize + 1) / 2 * 10000;
            return formatter.format(pop);
        }


        public int getStringCenter(Graphics g, String text, Font font) {
            FontMetrics metrics = g.getFontMetrics(font);
            return metrics.stringWidth(text) / 2;
        }
    }

}
