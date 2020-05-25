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


        public int plusOneIf(boolean cond) {
            return cond ? 1 : 0;
        }

        public void drawRoads() {
            /*** City road network **/

            String road = settings.isRailroad() ? "romanroad" : settings.roads;
            String bigroad = road;
            String roadx = road + "v";
            String roady = road + "h";
            String xroad = road + "x";

            int toX = cityLayout.sizeB.x;
            int toY = cityLayout.sizeB.y;
            boolean roadsY, roadsX;
            for (int y = toY - 1; y >= 0; y--) {
                for (int x = 0; x < toX; x++) {
                    int dx = getDx(x);
                    int dy = getDy(y);

                    roadsY = (y != (cityLayout.centerB.y - 1) && y != (toY - 1));
                    roadsX = (x != (cityLayout.centerB.x - 1) && x != (toX - 1));

                    if (roadsX) {
                        drawBuilding(roadx, dx + 3, dy);
                        drawBuilding(roadx, dx + 3, dy + 1);

                        if (y == (cityLayout.centerB.y - 1)) {
                            drawBuilding(roadx, dx + 3, dy + 2);
                            drawBuilding(roadx, dx + 3, dy + 3);
                        }
                    }

                    if (roadsY) {
                        drawBuilding(roady, dx + 1, dy + 2);
                        drawBuilding(roady, dx + 2, dy + 2);

                        if (x == (cityLayout.centerB.x - 1)) {
                            drawBuilding(roady, dx + 3, dy + 2);
                            drawBuilding(roady, dx + 4, dy + 2);
                        }
                    }
                }
            }

            /* round road */
            if(!settings.walled) {
                int endX = cityLayout.sizeM.x + addBigRoad() - 1;
                int endY = cityLayout.sizeM.y + addBigRoad() - 2;
                for (int i = 3; i < endX; i++) {
                    drawBuilding(roady, i, 1);
                    drawBuilding(roady, i, endY);
                }
                for (int i = 2; i < endY; i++) {
                    drawBuilding(roadx, 2, i);
                    drawBuilding(roadx, endX, i);
                }
            }

            /* xroads */
            for (int y = toY - 1; y >= 0 - plusOneIf(!settings.walled); y--) {
                for (int x = -1; x < toX -  plusOneIf(settings.walled); x++) {
                    int dx = getDx(x); int dy = getDy(y);
                    roadsY = (y != (cityLayout.centerB.y - 1));
                    roadsX = (x != (cityLayout.centerB.x - 1));
                    if(roadsX && roadsY) {
                        drawBuilding( xroad, dx + 3,dy + 2);
                    }
                }
            }

            /*** BIGROAD **/

            int cx = cityLayout.centerM.x;
            int cy = cityLayout.centerM.y;
            if(isBigRoad()) {
                cy++;
                bigroad = bigroad + "big";
            }

            String bigroadx = bigroad + "v";
            String bigroady = bigroad + "h";
            String bigxroad = bigroad + "x";

            for(int i = 0; i < 25; i++) {
                drawBuilding(bigroady, cx - i, cy);
                drawBuilding(bigroadx, cx, cy + i);
            }
            for(int i = 0; i < 25; i++) {
                drawBuilding(bigroady, cx + i, cy);
                drawBuilding(bigroadx, cx, cy - i);
            }
            drawBuilding( bigroad + "x", cx, cy);

            BuildingGfx bigRoadX = cityLayout.getBuilding(bigxroad);
            for(int i = 0 ; i <= cityLayout.sizeB.x - addBigRoad(); i++) {
                int dx = getDx(i,3);
                if(isBigRoad() && road.equals("highway")) {
                    if (i >= cityLayout.centerB.x) {
                        bigRoadX.dx = 0.5;
                        bigRoadX.dy = 1.25;
                    } else {
                        bigRoadX.dx = -0.25;
                        bigRoadX.dy = 0.25;
                    }
                }
                drawBuilding(bigRoadX, dx, cy);
            }
            for(int i = 0 ; i <= cityLayout.sizeB.y - addBigRoad(); i++) {
                int dy = getDy(i,3) - (isBigRoad() ? 0 : 1);
                if(isBigRoad() && road.equals("highway")) {
                    if (i >= cityLayout.centerB.y) {
                        bigRoadX.dx = 0.75;
                        bigRoadX.dy = 0.25;
                    } else {
                        bigRoadX.dx = -0.25;
                        bigRoadX.dy = 1.25;
                    }
                }
                drawBuilding(bigxroad, cx, dy);
            }
            bigRoadX.dx = 0;
            bigRoadX.dy = isBigRoad() ? 0.75 : 0;
            drawBuilding(bigxroad, cx, cy);
        }

        public int getDx(int x){ return getDx(x,1); }
        public int getDy(int y){ return getDy(y,1); }
        public int getDx(int x, int dd) { int dx = x * 3 + 2; if(isBigRoadX(x)) dx = dx + dd; return  dx; }
        public int getDy(int y, int dd) { int dy = y * 3 + 2; if(isBigRoadY(y)) dy= dy + dd; return  dy; }
        public boolean isBigRoadX(int x) { return isBigRoad() && x >= cityLayout.centerB.x; }
        public boolean isBigRoadY(int y) { return isBigRoad() && y >= cityLayout.centerB.y; }
        public boolean isBigRoad() { return settings.size > 6; }
        public int addBigRoad() { return isBigRoad() ? 1 : 0; }
        public boolean isCenterY(int y) { return isBigRoad() && y == cityLayout.centerB.y;}
        public boolean isCenterX(int x) { return isBigRoad() && x == cityLayout.centerB.x;}

        public void drawWallsBack() {
            if(!cityLayout.walled || settings.walls.isEmpty()) {
             //   return;
            }
            int s = 1;
            int ex = cityLayout.sizeM.x; if(!cityLayout.isBigStreet()) ex--;
            int ey =  cityLayout.sizeM.y - 1; if(!cityLayout.isBigStreet()) ey--;

            int cx = cityLayout.centerM.x;
            int cy = cityLayout.centerM.y;

            boolean w4 = cityLayout.wallx4;

            BuildingGfx gatev = cityLayout.getBuilding(w4 ? "gate_v" : "gatev");
            BuildingGfx gateh = cityLayout.getBuilding(w4 ? "gate_h" : "gateh");
            BuildingGfx towerl = cityLayout.getBuilding(w4 ? "tower_l" : "tower");
            BuildingGfx towerr = cityLayout.getBuilding(w4 ? "tower_r" : "tower");
            if(cityLayout.isBigStreet()) {
                gatev.dy = -0.75;
                gatev.dx = 0.5;
                gateh.dx = 0.5;
                gateh.dy = 0.5;
            } else {
                gatev.dy = 0;
                gatev.dx = 0;
                gateh.dx = 0;
                gateh.dy = 0;
            }

            drawBuilding(w4 ? "tower_t" : "tower", 2, ey);

            for (int i = 1; i < ey; i++) {
                drawBuilding( w4 ? "wall_l" : "wallv", 2, i);
            }

            for (int i = 2; i < ex; i++) {
                drawBuilding(w4 ? "wall_t" : "wallh", i + 1, ey);
            }

            drawBuilding(gatev, 2, cy);
            drawBuilding(gateh, cx, ey);

            if(w4) {
                towerl.dx = -0.5;
                towerr.dx = 0.75;
            } else {
                towerl.dx = 0;
                towerr.dx = 0;
            }
            /*
            drawBuilding(towerl, 2,1);
            drawBuilding(towerr, ex, ey); */
        }

        public void drawWallsFront() {
            if(!cityLayout.walled || settings.walls.isEmpty()) {
              //  return;
            }
            int s = 1;
            int ex = cityLayout.sizeM.x; if(!cityLayout.isBigStreet()) ex--;
            int ey = cityLayout.sizeM.y - 1; if(!cityLayout.isBigStreet()) ey--;
            int cx = cityLayout.centerM.x;
            int cy = cityLayout.centerM.y;
            boolean w4 = cityLayout.wallx4;

            BuildingGfx gatev = cityLayout.getBuilding(w4 ? "gate_v" : "gatev");
            BuildingGfx gateh = cityLayout.getBuilding(w4 ? "gate_h" : "gateh");
            BuildingGfx wallr = cityLayout.getBuilding(w4 ? "wall_r" : "wallv");
            BuildingGfx wallb = cityLayout.getBuilding(w4 ?  "wall_b" : "wallh");
            BuildingGfx towerl = cityLayout.getBuilding(w4 ? "tower_l" : "tower");
            BuildingGfx towerr = cityLayout.getBuilding(w4 ? "tower_r" : "tower");
            for (int i = s; i < ey; i++) {
                drawBuilding( wallr, ex, i);
            }

            for (int i = 2; i < ex; i++) {
                drawBuilding(wallb, i + 1, s);
            }

            if(cityLayout.isBigStreet()) {
                gatev.dy = -0.75;
                gatev.dx = 0.5;
                gateh.dx = 0.5;
                gateh.dy = 0.5;
            } else {
                gatev.dy = 0;
                gatev.dx = 0;
                gateh.dx = 0;
                gateh.dy = 0;
            }

            drawBuilding(gatev, ex, cy);
            drawBuilding(gateh, cx, s);

            drawBuilding(towerl, 2,1);
            drawBuilding(towerr, ex, ey);
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

           // y = 0 - y;

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
                //System.out.println(buildingGfx.name);
            }

            Coords drawCoords = getDrawCoords(x,y,dx,dy);
            int cx = drawCoords.x;
            int cy = drawCoords.y;

            if(buildingGfx.name.equals(Buildings.AQUEDUCT)) {
                drawAqueduct( x,y);
            }

            Image img = gfx.get(buildingGfx.fileName);

            //drawTileRect(g,cx,cy + dh - dw / 2,dw,dw / 2);

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
            for(int i = 0; i < 10; i++) {
                drawBuilding("aqueductx", -offsetX - i, y - i);
            }
            for(int i = 0; i < x + offsetX; i++) {
                drawBuilding("aqueducth", i - offsetX, y);
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
                    int dx = getDx(x); int dy = getDy(y);
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
                drawBuilding( cityLayout.getBuilding("port"), cityLayout.sizeM.x / 2 + 14, cityLayout.sizeM.y / 2);
               // drawBuilding(g, cityLayout.getBuilding("colossus"), cityLayout.cityLayoutMatrixSize / 2 - 4, cityLayout.cityLayoutMatrixSize / 2 + 11);
               // drawBuilding(g, cityLayout.getBuilding("lighthouse"), cityLayout.cityLayoutMatrixSize / 2, cityLayout.cityLayoutMatrixSize / 2 + 12);
            } else {
                draw("topBgR", d.right().top().dim(672, 173, 0,130));
            }


            draw("topBgL", d.left().top().dim(672, 173, 0,130));


            drawWallsBack();
            drawCityLayout();
            drawWallsFront();

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
            int pop = settings.size * (settings.size + 1) / 2 * 10000;
            return formatter.format(pop);
        }

        public int getStringCenter(Graphics g, String text, Font font) {
            FontMetrics metrics = g.getFontMetrics(font);
            return metrics.stringWidth(text) / 2;
        }
    }

}
