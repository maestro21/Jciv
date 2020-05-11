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
    public JButton rndBtn, incBtn, decBtn, walledBtn, palaceBtn, waterBtn, styleBtn, styleBtn2, ageBtn;

    public int tileSize;

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
                settings.palace = !settings.palace;
                buildCityLayout();
                repaint();
            }
        });


        walledBtn = new JButton("Wall");
        walledBtn.setBounds(300,0,75,30);
        walledBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settings.walled = !settings.walled;
                buildCityLayout();
                repaint();
            }
        });

        waterBtn = new JButton("Water");
        waterBtn.setBounds(375,0,75,30);
        waterBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settings.hasWater = !settings.hasWater;
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

        styleBtn2 = new JButton("S-");
        styleBtn2.setBounds(525,0,75,30);
        styleBtn2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lastStyle();
                loadBuildings();
                buildCityLayout();
                repaint();
            }
        });

        ageBtn = new JButton("Age");
        ageBtn.setBounds(600,0,50,30);
        ageBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settings.nextAge();
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
        add(styleBtn2);
        add(ageBtn);
    }

    public void nextStyle() {
        csCounter++;
        if(csCounter >= Ruleset.buildingSets.size()) {
            csCounter = 0;
        }
        citySet =  Ruleset.buildingSets.get(csCounter);
    }

    public void lastStyle() {
        csCounter--;
        if(csCounter < 0) {
            csCounter = Ruleset.buildingSets.size() - 1;
        }
        citySet = Ruleset.buildingSets.get(csCounter);
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
        cityLayout = new CityLayout(settings, buildingsGfx);
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
            BuildingGfx roadx = cityLayout.getBuilding(settings.isRailroad() ? "railroad" :  "roadv"), roady = cityLayout.getBuilding("roadh");
            roadx.dx = -0.15;
            roadx.dy = -0.15;
            roady.dx = -0.15;
            roady.dy = -0.15;
            for(int i = 0; i < 17; i++) {
                drawBuilding(g,roady, cx - i, cy);
                drawBuilding(g, roadx, cx, cy + i);
            }
            for(int i = 0; i < 17; i++) {
                drawBuilding(g, roady, cx + i, cy);
                drawBuilding(g,roadx, cx, cy - i);
            }
            if(!settings.isRailroad()) {
                BuildingGfx xroad = cityLayout.getBuilding("roadx");
                xroad.dx = -0.15;
                xroad.dy = -0.15;
                drawBuilding(g, xroad, cx, cy);
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

            if(buildingGfx.name.equals(Buildings.AQUEDUCT)) {
                drawAqueduct(g, x,y);
            }

            Image img = gfx.get(buildingGfx.fileName);

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
            draw(gfx.get("bg"),d.center().dim(1750, 900));

            BuildingGfx buildingGfx;
            int offsetX = (getWidth() - (int)(cityLayout.cityLayoutMatrixSize * tileSize * 1.2)) / 2;
            int offsetY = (int)(getHeight() / 1.8);
            offset = new Coords(offsetX, offsetY);

            drawRoads(g);


            if(settings.hasWater) {
                draw(gfx.get("coastBgTop"), d.left().top().dim(1100, 315, 500, 75));
                draw(gfx.get("coastBgBottom"), d.dim(575, 500, (1100 - 575) + 500, 375));
                draw(gfx.get("coastBgRight"), d.dim(430, 800, 1099 + 500, 88));
                drawBuilding(g, cityLayout.getBuilding("port"), cityLayout.cityLayoutMatrixSize / 2 + 12, cityLayout.cityLayoutMatrixSize / 2 + 1);
               // drawBuilding(g, cityLayout.getBuilding("colossus"), cityLayout.cityLayoutMatrixSize / 2 - 4, cityLayout.cityLayoutMatrixSize / 2 + 11);
               // drawBuilding(g, cityLayout.getBuilding("lighthouse"), cityLayout.cityLayoutMatrixSize / 2, cityLayout.cityLayoutMatrixSize / 2 + 12);
            } else {
                draw(gfx.get("topBgR"), d.right().top().dim(672, 173, 0,130));
            }


            draw(gfx.get("topBgL"), d.left().top().dim(672, 173, 0,130));

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
            drawCityInfo(g);
        }



        private void drawCityInfo(Graphics g) {
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
