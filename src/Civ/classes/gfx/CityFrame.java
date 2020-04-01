package Civ.classes.gfx;

import Civ.classes.Coords;
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
import java.util.ArrayList;

public class CityFrame extends JFrame {


    public static void main(String[] args) {
        CityFrame cf = new CityFrame();
    }

    public int maxSize = 30;
    public int citySize = 8;
    public Image wonders;
    public Image buildings;
    public Image bg;
    public Image coastBg, coastBgTop, coastBgRight, coastBgBottom;
    public Image topBg, topBgL, topBgR;
    public CityLayout cityLayout;
    public ArrayList<BuildingGfx> buildingsGfx = new ArrayList<>();
    public Coords offset;

    public String[] citySets = "ancient,roman2,russian_imp,ukraine".split(",");
    public int csCounter = 0;
    public String ruleset = "default";
    public String citySet = "ancient";// "roman2";
    public String cityViewPath = "";
    public JButton rndBtn, incBtn, decBtn, walledBtn, palaceBtn, waterBtn, styleBtn;

    public int tileSize;
    public boolean isWater = true;
    public boolean walled = true;
    public boolean palace = true;

    public boolean yesno() {
        return (Math.random() * 2 > 1);
    }

    public CityFrame() {
        setTitle("CivNations");
        cityViewPath = "data/rulesets/" + ruleset + "/cityview/";
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0,0, screenSize.width, screenSize.height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(1750, 900));
        setMaximumSize(new Dimension(1750, 900));
        wonders = Toolkit.getDefaultToolkit().getImage(cityViewPath + "wonders.png");
        bg = Toolkit.getDefaultToolkit().getImage(cityViewPath + "grasslandbg2.jpg");
        topBg = Toolkit.getDefaultToolkit().getImage(cityViewPath + "leftforest.png");
        topBgL = Toolkit.getDefaultToolkit().getImage(cityViewPath + "topbgl.png");
        topBgR = Toolkit.getDefaultToolkit().getImage(cityViewPath + "topbgr.png");
        coastBg = Toolkit.getDefaultToolkit().getImage(cityViewPath + "coastBg.png");
        coastBgTop = Toolkit.getDefaultToolkit().getImage(cityViewPath + "coastbgtop.png");
        coastBgRight = Toolkit.getDefaultToolkit().getImage(cityViewPath + "coast2r.jpg");
        coastBgBottom = Toolkit.getDefaultToolkit().getImage(cityViewPath + "coastbgright.png");
        tileSize = 64;
        CityPanel cityPanel = new CityPanel(this);
        loadBuildings();
        buildCityLayout();
        getContentPane().add(cityPanel);
        setLayout(null);
        cityPanel.setBounds(0,50,1750,900);

        rndBtn = new JButton("Randomize");
        rndBtn.setBounds(0,0,100,30);
        rndBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                citySize = (int)(Math.random() * maxSize);
                walled = yesno();
                isWater = yesno();
                buildCityLayout();
                repaint();
            }
        });


        incBtn = new JButton("+");
        incBtn.setBounds(100,0,50,30);
        incBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(citySize < maxSize) citySize++;
                buildCityLayout();
                repaint();
            }
        });

        decBtn = new JButton("-");
        decBtn.setBounds(150,0,50,30);
        decBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(citySize > 1) citySize--;
                buildCityLayout();
                repaint();
            }
        });

        palaceBtn = new JButton("Palace");
        palaceBtn.setBounds(200,0,100,30);
        palaceBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                palace = !palace;
                buildCityLayout();
                repaint();
            }
        });


        walledBtn = new JButton("Wall");
        walledBtn.setBounds(300,0,75,30);
        walledBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                walled = !walled;
                buildCityLayout();
                repaint();
            }
        });

        waterBtn = new JButton("Water");
        waterBtn.setBounds(375,0,75,30);
        waterBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isWater = !isWater;
                buildCityLayout();
                repaint();
            }
        });

        styleBtn = new JButton("Style");
        styleBtn.setBounds(450,0,75,30);
        styleBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextStyle();
                loadBuildings();
                buildCityLayout();
                repaint();
            }
        });


        add(rndBtn);
        add(incBtn);
        add(decBtn);
        add(palaceBtn);
        add(walledBtn);
        add(waterBtn);
        add(styleBtn);

        setVisible(true);
    }

    public void nextStyle() {
        csCounter++;
        if(csCounter >= citySets.length) {
            csCounter = 0;
        }

        citySet = citySets[csCounter];
    }

    public void loadBuildings() {
        citySet = citySets[csCounter];
        buildings = Toolkit.getDefaultToolkit().getImage(cityViewPath + citySet + ".png");
        buildingsGfx.clear();
        loadJsonWonders();
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

    public String getBuildingsByCitySet() {
        switch(this.citySet) {
            case "roman2": return "barracks,granary,marketplace,temple,library,amphitheater,aqueduct,colosseum,circus";
            case "russian_imp": return "barracks,granary,market,church,university,theater,basyl";
            case "ukraine": return "barracks,granary,market,church,university";
            default: return "barracks,granary,market,temple,library";
        }
    }


    public void buildCityLayout() {
        String buildings = getBuildingsByCitySet();
        if(palace) {
            buildings = "palace," + buildings;
        }
        String[] buildingArray = buildings.split(",");
        cityLayout = new CityLayout(citySize, buildingArray, walled, buildingsGfx);
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
            int cx = cityLayout.cityCenter.x;
            int cy = cityLayout.cityCenter.y;
            BuildingGfx roadx = cityLayout.getBuilding("roadv"), roady = cityLayout.getBuilding("roadh");
            for(int i = 0; i < 17; i++) {
                drawBuilding(g,roady, cx - i, cy);
                drawBuilding(g, roadx, cx, cy + i);
            }
            for(int i = 0; i < 17; i++) {
                drawBuilding(g, roady, cx + i, cy);
                drawBuilding(g,roadx, cx, cy - i);
            }
        }

        public void drawBuilding(Graphics g, BuildingGfx buildingGfx, int x, int y) {
            if(buildingGfx == null) {
                return;
            }

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

            Image img = buildings;
            if(buildingGfx.isWonder()) {
                img = wonders;
            }

            g.drawImage(img, cx, cy,
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
            for(int i = 0; i < 7; i++) {
                drawBuilding(g, aqueducth, -offsetX - 1 - i, y - 1 - i);
            }
            for(int i = 0; i < x + offsetX; i++) {
                drawBuilding(g, aqueductx, i - offsetX, y);
            }
        }


        public void draw(Image img, ImgDimensions dim) {
            //System.out.println(dim);
            g.drawImage(img, dim.offX, dim.offY, dim.x, dim.y, null);
        }

        public void initDraw(Graphics g) {
            d = new Drawer();
            this.g = g;
            double scaleX = 1.0 * getWidth() / 1750;
            double scaleY = 1.0 *getHeight() / 900;
            float scale = (float)(scaleX > scaleY ? scaleX : scaleY); //System.out.println(scale);
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

            draw(topBgL, d.left().top().dim(672, 173, 0,130));

            if(isWater) {
                draw(coastBgTop, d.left().top().dim(1100, 315, 500, 75));
                draw(coastBgBottom, d.dim(575, 500, (1100 - 575) + 500, 375));
                draw(coastBgRight, d.dim(430, 800, 1099 + 500, 88));
                drawBuilding(g, cityLayout.getBuilding("port"), cityLayout.cityLayoutMatrixSize / 2 + 12, cityLayout.cityLayoutMatrixSize / 2 + 1);
            } else {
                draw(topBgR, d.right().top().dim(672, 173, 0,130));
            }

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
