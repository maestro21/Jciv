package Civ.entities;

import Civ.classes.Coords;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

public class Ruleset {

    public static final String RELIGION_PAGANISM = "paganism";

    public static String name;

    public static int tileSize;
    public static int terrainTileSize;
    public static int flagsPerRow = 30;

    public static Coords coastTile;

    public static ArrayList<String> flags = new ArrayList<>();

    public static ArrayList<Terrain> terrain = new ArrayList<>();

    public static boolean loaded = false;

    public static ArrayList<String> colors = new ArrayList<>();

    public static ArrayList<String> religions = new ArrayList<>();

    public static ArrayList<String> ages = new ArrayList<>();

    public static ArrayList<String> cityStyles = new ArrayList<>();

    public static ArrayList<CivNation> civNations = new ArrayList<>();

    public static ArrayList<String> buildingSets = new ArrayList<String>(Arrays.asList(
      "classic", "euro"
    ));

    public Ruleset() {}

    public Ruleset(String name) {
        load(name);
    }

    public static String getStr(JSONObject obj, String name) {
        return obj.get(name) != null ? obj.get(name).toString() : "";
    }

    public static int getInt(JSONObject obj, String name) {
        return str2int(getStr(obj,name));
    }

    public static int str2int(String str) {
        return str.isEmpty() ? 0 : Integer.parseInt(str);
    }


    public static ArrayList<String> getValues(JSONObject obj, String str) {
       return new ArrayList(Arrays.asList(getStr(obj,str).split(",")));
    }

    public static Object alGet(ArrayList al, int index) {
        return (index >= al.size()) ? null : al.get(index);
    }

    public static String alGetStr(ArrayList al,int index) {
        return ((index >= al.size()) ? "" : al.get(index)).toString();
    }

    public static int alGetInt(ArrayList al,int index) {
        return alGetStr(al,index).isEmpty() ? 0 : Integer.parseInt(alGetStr(al,index).trim());
    }

    public static void load(String name) {
        Ruleset.name = name;
        JSONParser parser = new JSONParser();
        try (Reader reader = new FileReader("data/rulesets/" + name + "/ruleset.json")) {
            JSONObject jsonRulset = (JSONObject) parser.parse(reader);
            Ruleset.name = getStr(jsonRulset,"name");
            tileSize = getInt(jsonRulset,"tileSize");
            terrainTileSize = getInt(jsonRulset,"terrainTileSize");
            religions = getValues(jsonRulset,"religions");
            ages = getValues(jsonRulset,"ages");
            Collections.addAll(colors,getStr(jsonRulset,"colors").split(","));

            /** load terrain **/
            JSONArray jsonTerrain = (JSONArray)jsonRulset.get("terrain");
            for(int i = 0; i < jsonTerrain.size(); i++) {
                JSONObject jsonTerrainEl = (JSONObject)jsonTerrain.get(i);
                Terrain t = new Terrain();
                t.name = getStr(jsonTerrainEl,"name");
                t.symbol = getStr(jsonTerrainEl, "symbol");
                t.type = getStr(jsonTerrainEl ,"type");
                ArrayList<String> colors = getValues(jsonTerrainEl,"color");
                t.irrigable = getInt(jsonTerrainEl, "irrigable");
                t.mineable = getInt(jsonTerrainEl, "mineable");

                //JSONArray colors = (JSONArray)jsonTerrainEl.get("color");
                t.color = new Color(
                    alGetInt(colors,0),
                    alGetInt(colors, 1),
                    alGetInt(colors,2)
                );

                //JSONArray pos = (JSONArray)jsonTerrainEl.get("pos");
                t.pos =  new Coords(
                    getInt(jsonTerrainEl,"col"),
                    getInt(jsonTerrainEl, "row")
                );
                terrain.add(t);
            }

            /** load cityTypes **/
            JSONArray jsonCities = (JSONArray)jsonRulset.get("cities");
            Iterator iterator = jsonCities.iterator();
            while(iterator.hasNext()) {
                String cityType =  iterator.next().toString();
                cityStyles.add(cityType);
            }

            loaded = true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        loadFlags();
    }


    public static void loadFlags() {
        JSONParser parser = new JSONParser();
        try (Reader reader = new FileReader("data/rulesets/" + name + "/flags.json"))
        {
            JSONArray jsonFlags = (JSONArray) parser.parse(reader);
            if (jsonFlags != null) {
                for (int i = 0 ; i < jsonFlags.size(); i++){
                    flags.add(jsonFlags.get(i).toString());
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Terrain getTerrain(String symbol) {
        Terrain t = new Terrain();
        for(int i = 0; i < terrain.size(); i++) {
            t = terrain.get(i);

            if(t.symbol.equals(symbol) || t.name.equals(symbol)) {
                return t;
            }
        }
        return getTerrain(" ");
    }

    public static int getTerrainIdx(String symbol) {
        Terrain t = new Terrain();
        for(int i = 0; i < terrain.size(); i++) {
            t = terrain.get(i);

            if(t.symbol.equals(symbol) || t.name.equals(symbol)) {
                return i;
            }
        }
        return -1;
    }


    public static int getCityStyleIndex(String name) {
        for(int i = 0; i < cityStyles.size(); i++) {
            if(cityStyles.get(i).equals(name)) {
                return i;
            }
        }
        return 0;
    }


    public static CivNation getCivNation(String civNationName) {
        for (CivNation civNation : civNations) {
            if(civNation.getName().equals(civNationName)) {
                return civNation;
            }
        }

        CivNation civNation = loadCivNation(civNationName);
        if(civNation != null) {
            civNations.add(civNation);
        }
        return civNation;
    }

    public static CivNation loadCivNation(String civNationName) {
        JSONParser parser = new JSONParser();
        CivNation civNation = null;
        try (Reader reader = new FileReader("data/rulesets/" + name + "/nations/" + civNationName + ".json")) {
            civNation = new CivNation();
            JSONObject jsonCivNation = (JSONObject) parser.parse(reader);

            civNation.setName(getStr(jsonCivNation,"name"));
            civNation.setDescription(getStr(jsonCivNation,"description"));
            civNation.setAdj(getStr(jsonCivNation,"adj"));
            civNation.setCityStyle(getStr(jsonCivNation,"cityStyle"));
            civNation.setReligion(getStr(jsonCivNation,"religion"));
            civNation.setFlag(getStr(jsonCivNation,"flag"));

            Collections.addAll(civNation.cityNames,getStr(jsonCivNation,"cities").split(","));

            /** load nations **/
            Map nations = ((Map)jsonCivNation.get("govs"));
            if(nations != null) {
                // iterating address Map
                Iterator<Map.Entry> itr1 = nations.entrySet().iterator();
                while (itr1.hasNext()) {
                    Map.Entry pair = itr1.next();
                    JSONObject jsonNation = (JSONObject) pair.getValue();
                    Nation nation = new Nation();
                    nation.setCountryName(getStr(jsonNation, "country"));
                    nation.setFlag(getStr(jsonNation, "flag"));
                    nation.setTitle(getStr(jsonNation, "title"));
                    nation.setGovernment(getStr(jsonNation, "government"));
                    nation.setRuler(getStr(jsonNation, "ruler"));
                    civNation.getNations().add(nation);
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return civNation;
    }

    public static Coords getFlagCoords(String flagName) {
        Coords coords = null;
        int index = flags.indexOf(flagName);
        if(index > -1) {
            int x = index % flagsPerRow;
            int y = (int)Math.floor(index / flagsPerRow);
            coords = new Coords(x * 44,y * 30);
        }
        return coords;
    }


    public Color getColor(int i) {
        return new Color(Integer.parseInt(colors.get(i), 16));
    }

    /**
     * Checks if age1 has age2. I.e. hasAge('colonial', 'ancient') => true, hasAge('colonial', 'industrial') => false
     * Usage: hasAge(player.age, age)
     * @param age1
     * @param age2
     * @return
     */
    public static boolean hasAge(String age1, String age2) {
        return ages.indexOf(age1) >= ages.indexOf(age2);
    }


    public static String getBuildingStyleByCityStyle(String cityStyle) {
        switch(cityStyle) {
            case "egyptian":
            case "babylonian":
            case "muslim":
            case "indian":
                return "oriental";

            case "mediterranean":
            case "spanish":
            case "roman":
                return "classic";

            case "medieval":
            case "celtic":
            case "russian":
            case "tribal":
            case "industrial":
            case "soviet":
            case "modern":
            case "postmodern":
            case "nazi":
            case "polish":
            case "english":
            case "nordic":
                return "euro";


            case "eastern":
                return  "eastern";
        }

        return "euro";
    }
}

