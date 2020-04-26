package Civ.entities;

import java.util.ArrayList;

public class CivNation {

    private String flag;

    private String name;

    private String adj;

    private String description;

    private String cityStyle;

    private String religion;

    public ArrayList<String> cityNames = new ArrayList<>();

    private ArrayList<Nation> nations = new ArrayList<>();

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdj() {
        return adj;
    }

    public void setAdj(String adj) {
        this.adj = adj;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCityStyle() {
        return cityStyle;
    }

    public void setCityStyle(String cityStyle) {
        this.cityStyle = cityStyle;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public ArrayList<String> getCityNames() {
        return cityNames;
    }

    public void setCityNames(ArrayList<String> cityNames) {
        this.cityNames = cityNames;
    }

    public ArrayList<Nation> getNations() {
        return nations;
    }

    public void setNations(ArrayList<Nation> nations) {
        this.nations = nations;
    }
}
