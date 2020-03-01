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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class Ruleset {

    public String name;

    public int tileSize;
    public int terrainTileSize;

    public Coords coastTile;

    public ArrayList<Terrain> terrain = new ArrayList<>();

    public boolean loaded = false;

    public ArrayList<String> cityTypes = new ArrayList<>();

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
}

