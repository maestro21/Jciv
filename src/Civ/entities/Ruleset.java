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

    public String name;

    public int tileSize;
    public int terrainTileSize;
    public int flagsPerRow = 30;

    public Coords coastTile;

    public ArrayList<String> flags = new ArrayList<>();

    public ArrayList<Terrain> terrain = new ArrayList<>();

    public boolean loaded = false;

    public ArrayList<String> colors = new ArrayList<>();

    public ArrayList<String> religions = new ArrayList<>();

    public ArrayList<String> ages = new ArrayList<>();

    public ArrayList<String> cityTypes = new ArrayList<>();

    public ArrayList<CivNation> civNations = new ArrayList<>();

    public Ruleset() {}

    public Ruleset(String name) {
        this.load(name);
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


    public ArrayList<String> getValues(JSONObject obj, String str) {
       return new ArrayList(Arrays.asList(getStr(obj,str).split(",")));
    }

    public Object alGet(ArrayList al, int index) {
        return (index >= al.size()) ? null : al.get(index);
    }

    public String alGetStr(ArrayList al,int index) {
        return ((index >= al.size()) ? "" : al.get(index)).toString();
    }

    public int alGetInt(ArrayList al,int index) {
        return alGetStr(al,index).isEmpty() ? 0 : Integer.parseInt(alGetStr(al,index).trim());
    }

    public void load(String name) {
        JSONParser parser = new JSONParser();
        try (Reader reader = new FileReader("data/rulesets/" + name + "/ruleset.json")) {
            JSONObject jsonRulset = (JSONObject) parser.parse(reader);
            this.name = getStr(jsonRulset,"name");
            this.tileSize = getInt(jsonRulset,"tileSize");
            this.terrainTileSize = getInt(jsonRulset,"terrainTileSize");
            this.religions = getValues(jsonRulset,"religions");
            this.ages = getValues(jsonRulset,"ages");
            Collections.addAll(colors,getStr(jsonRulset,"colors").split(","));

            /** load terrain **/
            JSONArray jsonTerrain = (JSONArray)jsonRulset.get("terrain");
            for(int i = 0; i < jsonTerrain.size(); i++) {
                JSONObject jsonTerrainEl = (JSONObject)jsonTerrain.get(i);
                Terrain terrain = new Terrain();
                terrain.name = getStr(jsonTerrainEl,"name");
                terrain.symbol = getStr(jsonTerrainEl, "symbol");
                terrain.type = getStr(jsonTerrainEl ,"type");
                ArrayList<String> colors = getValues(jsonTerrainEl,"color");

                //JSONArray colors = (JSONArray)jsonTerrainEl.get("color");
                terrain.color = new Color(
                    alGetInt(colors,0),
                    alGetInt(colors, 1),
                    alGetInt(colors,2)
                );

                //JSONArray pos = (JSONArray)jsonTerrainEl.get("pos");
                terrain.pos =  new Coords(
                    getInt(jsonTerrainEl,"col"),
                    getInt(jsonTerrainEl, "row")
                );
                this.terrain.add(terrain);
            }

            /** load cityTypes **/
            JSONArray jsonCities = (JSONArray)jsonRulset.get("cities");
            Iterator iterator = jsonCities.iterator();
            while(iterator.hasNext()) {
                String cityType =  iterator.next().toString();
                this.cityTypes.add(cityType);
            }

            loaded = true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        loadFlags();
    }


    public void loadFlags() {
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

    public Terrain getTerrain(String symbol) {
        Terrain t = new Terrain();
        for(int i = 0; i < terrain.size(); i++) {
            t = terrain.get(i);

            if(t.symbol.equals(symbol)) {
                return t;
            }
        }
        return getTerrain(" ");
    }

    public int getCityStyleIndex(String name) {
        for(int i = 0 ; i < cityTypes.size(); i++) {
            if(cityTypes.get(i).equals(name)) {
                return i;
            }
        }
        return 0;
    }


    public CivNation getCivNation(String civNationName) {
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

    public CivNation loadCivNation(String civNationName) {
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

    public Coords getFlagCoords(String flagName) {
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
}

