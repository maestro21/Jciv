package Civ.classes.gfx;

import Civ.classes.Buildings;
import Civ.classes.Coords;
import Civ.entities.Ruleset;

import java.nio.file.Paths;
import java.util.ArrayList;

class CityLayout {

    private CityFrame cityFrame;

    private ArrayList<BuildingGfx> buildingsGfx;
    public BuildingGfx park = null;
    public BuildingGfx[][] cityLayout;
    private BuildingGfx[][] buildingMatrix;
    public ArrayList<BuildingGfx> bigBuildings = new ArrayList<>();


    ArrayList<String> buildings = new ArrayList<>();
    int buildingMatrixSize;
    int bc = 0;
    boolean walled;
    boolean wallx4;
    boolean railroad;
    String age;

    Coords sizeB;
    Coords centerB;

    Coords sizeM;
    Coords centerM;

    Double mod = 0.8;

    public boolean isBigStreet() {
        return settings.size > 5;
    }

    CityBuildingsGfxSettings settings;


    private BuildingGfx emptyBuilding = new BuildingGfx();

    public BuildingGfx getBuilding(int x, int y) {
        try {
            return buildingMatrix[y][x];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }


    public CityLayout(CityFrame cityFrame) {
        this.cityFrame = cityFrame;
        buildCityLayout(cityFrame.settings, cityFrame.buildingsGfx);
    }

    public void buildCityLayout(CityBuildingsGfxSettings settings, ArrayList<BuildingGfx> buildingsGfx) {
        this.settings = settings;
        this.walled = settings.walled && !settings.walls.isEmpty();
        this.wallx4 = settings.wallx4();
        this.age = settings.age;
        int citySize = settings.size;
        int buildingCount = settings.cityBuildings.size();
        this.buildingsGfx = buildingsGfx;
        park = getBuilding("park");

        int houseIndex = 0;
        for(int i = 0; i < citySize; i++) {
            if(i < settings.cityBuildings.size()) {
                BuildingGfx b = getBuilding(settings.cityBuildings.get(i));
                if(b != null) {
                    if(b.size == 4) {
                        bigBuildings.add(b);
                        buildingCount--;
                    } else {
                        if(b.size > 1) {
                            buildingCount += b.size - 1;
                        }
                        this.buildings.add(settings.cityBuildings.get(i));
                    }
                }
            }

            this.buildings.add("house" + getHouseIndex(citySize,i));
        }

        buildingMatrixSize = (int)Math.ceil(Math.sqrt(citySize + buildingCount));

        buildingMatrix = new BuildingGfx[buildingMatrixSize][buildingMatrixSize];
        spiralBuildingsCounterClockwise();
        //printCity();
        cleanEmptyRows();
        /*System.out.println("After row cleaning:");
        printCity();
       // cityLayout();
       // printCityLayout(); */
    }

    public int getHouseIndex(int citySize, int idx) {
        Double hIdx = citySize > 16 ? citySize / 4.0 : 4.0;
        int houseIndex = (int)Math.ceil((citySize - idx ) / hIdx);
        if(houseIndex > 4) houseIndex = 4;
        if(houseIndex < 1) houseIndex = 1;
        return houseIndex;
    }

    public void cleanEmptyRows() {
        boolean rmTop = true;
        boolean rmBottom = true;
        boolean rmLeft = true;
        boolean rmRight = true;
        int s = buildingMatrixSize - 1;
        for(int i = 0; i <= s; i++) {
            if(buildingMatrix[0][i] != null) rmTop = false;
            if(buildingMatrix[s][i] != null) rmBottom = false;
            if(buildingMatrix[i][0] != null) rmLeft = false;
            if(buildingMatrix[i][s] != null) rmRight = false;
        }

        int xFrom = rmLeft ? 1 : 0;
        int xTo = buildingMatrixSize - (rmRight ? 1 : 0);
        int yFrom = rmTop ? 1 : 0;
        int yTo = buildingMatrixSize - (rmBottom ? 1 : 0);

        BuildingGfx[][] matrix = new BuildingGfx[yTo - yFrom][xTo - xFrom];
        BuildingGfx park = getBuilding("park");

        for(int y = yFrom; y < yTo; y++){
            for(int x = xFrom ; x < xTo ; x++){
                matrix[yTo - y - 1][xTo - x - 1] = buildingMatrix[y][x] == null ? park : buildingMatrix[y][x];
            }
        }

        buildingMatrix = matrix;
        sizeB = new Coords(matrix[0].length,matrix.length);
        centerB = new Coords((int)(Math.ceil(sizeB.x / 2.0)), (int)(Math.floor(sizeB.y  / 2.0)));
        sizeM = new Coords((sizeB.x + 1) * 3, (sizeB.y + 1) * 3);
        centerM = new Coords((int)(Math.ceil(sizeB.x / 2.0)) * 3 + 2, (int)(Math.floor(sizeB.y  / 2.0)) * 3 + 1);

        sizeB.print();
        centerB.print();
        sizeM.print();
        centerM.print();
    }

    public int getCityCenter() {
        return (buildingMatrixSize%2 == 0) ? (buildingMatrixSize/2 - 1) : (buildingMatrixSize/2);
    }

    private void spiralBuildingsCounterClockwise() {
        System.out.println("\n\nCounter Clockwise elements:");

        int right = buildingMatrixSize/2;
        int left = right - 1;
        int top =  getCityCenter();
        int bottom = top + 1;

        OUTER: while (true) {

            for (int i = right; i > left; i--) {
                if(!setBuilding(top, i)) break OUTER;
            }
            right++;

            for (int i = top; i < bottom; i++) {
                if(!setBuilding(i, left)) break OUTER;
            }
            top--;

            for (int i = left; i < right; i++) {
                if(!setBuilding(bottom, i)) break OUTER;
            }
            left--;

            for (int i = bottom; i > top; i--) {
                if(!setBuilding(i, right)) break OUTER;
            }
            bottom++;
        }
    }


    /**
     * Building functions
     */

    public BuildingGfx getBuilding(String name) { if(name == null) return null;
        BuildingGfx bgfx = null, _bgfx;
        name = getBuildingGfxNameByName(name);
        for(int i = 0; i < buildingsGfx.size(); i++) {
            _bgfx = buildingsGfx.get(i);
            if(_bgfx != null && _bgfx.name.equals(name) && Ruleset.hasAge(age, _bgfx.age)) {
                if(Buildings.isReligious(name) && !settings.religion.equals(_bgfx.religion)) continue;
                if(bgfx == null || Ruleset.hasAge(_bgfx.age, bgfx.age))
                bgfx = buildingsGfx.get(i);
            }
        }
        if(bgfx == null) {
            System.out.println("Cannot find building " + name);
        }

        return bgfx;
    }


    public String getBuildingGfxNameByName(String name) {
        switch (name) {
            case "townhall":
                return "house4";

            case "temple":
                if(!settings.isPagan())
                    return "church";

            case "church":
                if(settings.isPagan())
                    return "temple";
        }



        if(name.contains("romanroadbig")) {
            name = "romanroadbig";
        } else if(name.contains("roadbig")) {
            name = "roadbig";
        }

        return name;
    }

    private void replaceBuilding(int x, int y) {
        replaceBuilding(x,y, emptyBuilding);
    }

    private void replaceBuilding(int x, int y, BuildingGfx building) {
        if(buildingMatrix[y][x] != null) {
            buildings.add(buildingMatrix[y][x].name);
        }
        buildingMatrix[y][x] = building;
    }

    private boolean setBuilding(int i, int j) {
        if (i < 0 || i >= buildingMatrixSize || j < 0 || j>= buildingMatrixSize) return false;
        if(buildingMatrix[i][j] != null) {
            return true;
        }
        if(bc < buildings.size()) {
            BuildingGfx buildingGfx =  getBuilding(buildings.get(bc));
            if(buildingGfx == null) {
                return true;
            }

            if(buildingGfx.size == 4) {
                return false;
            }

            buildingMatrix[i][j] = buildingGfx;

            if(buildingGfx.size > 1 ) {
                //printMatrix(buildingMatrix,buildingMatrixSize);
                int dj = j + 1;
                if(dj >= buildingMatrixSize) {
                    dj = j - 1;
                }
                replaceBuilding(dj,i);

                if(buildingGfx.size == 4) {
                    int di = i + 1;
                    if(di >= buildingMatrixSize) {
                        di = i - 1;
                    }
                    replaceBuilding(j,di);
                    replaceBuilding(dj,di);
                }

                //printMatrix(buildingMatrix,buildingMatrixSize);
            }
            bc++;
        }
        return true;
    }


    /**
     * Print functions
     */

    public void printCity() {
        printMatrix(buildingMatrix);
    }

    public void printCityLayout() {
        printMatrix(cityLayout);
    }

    public void printMatrix(BuildingGfx[][] matrix) {

        String symbol = "";
        for(int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix[0].length; x++) {
                if(matrix[y][x] != null) {
                    symbol = matrix[y][x].symbol;
                } else {
                    symbol = "_";
                }
                System.out.print(symbol + ' ');
            }
            System.out.println();
        }
    }
} 