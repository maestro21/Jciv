package Civ.classes.gfx;

import Civ.classes.Coords;
import Civ.entities.Ruleset;

import java.util.ArrayList;

class CityLayout {

    private ArrayList<BuildingGfx> buildingsGfx;
    public BuildingGfx park = null;
    public BuildingGfx[][] cityLayout;
    private BuildingGfx[][] buildingMatrix;
    ArrayList<String> buildings = new ArrayList<>();
    int buildingMatrixSize;
    int cityLayoutMatrixSize;
    int bc = 0;
    boolean walled;
    boolean wallx4;
    boolean railroad;
    String age;

    public Coords cityCenter;

    private BuildingGfx emptyBuilding = new BuildingGfx();

    public BuildingGfx getBuilding(int x, int y) {
        return cityLayout[y][x];
    }


    public CityLayout(CityBuildingsGfxSettings settings, ArrayList<BuildingGfx> buildingsGfx) {
        this.walled = settings.walled;
        this.wallx4 = settings.wallx4();
        this.age = settings.age;
        int citySize = settings.size;
        int buildingCount = (Math.min(settings.buildings.size(), citySize));
        this.buildingsGfx = buildingsGfx;
        park = getBuilding("park");

        int houseIndex = 0;
        for(int i = 0; i < citySize; i++) {
            if(i < settings.buildings.size()) {
                this.buildings.add(settings.buildings.get(i));
                BuildingGfx b = getBuilding(settings.buildings.get(i));
                if(b != null && b.size > 1) {
                    buildingCount += b.size - 1;
                }
            }
            houseIndex = (int)Math.ceil((citySize - i ) / 4.0);
            if(houseIndex > 4) houseIndex = 4;
            if(houseIndex < 1) houseIndex = 1;
            this.buildings.add("house" + houseIndex);

        }

        buildingMatrixSize = (int)Math.ceil(Math.sqrt(citySize + buildingCount));
        buildingMatrix = new BuildingGfx[buildingMatrixSize][buildingMatrixSize];

        spiralBuildingsCounterClockwise();
        printCity();
        cityLayout();
        //printCityLayout();
    }

    public boolean isEmptyRow() {
        int to = buildingMatrixSize - 1;
        for (int i = 0; i < to; i++) {
            if(buildingMatrix[to][i] != null) {
                return false;
            }
        }
        return true;
    }


    public void cityLayout() {
        cityLayoutMatrixSize = buildingMatrixSize * 2 + 3;
        cityLayout = new BuildingGfx[cityLayoutMatrixSize][cityLayoutMatrixSize];
        int halfX = (int)Math.ceil((double)buildingMatrixSize / 2);
        int halfY = (int)Math.floor((double)buildingMatrixSize / 2);
        int i; int ix; int iy; int j;
        cityCenter = new Coords(halfX * 2 + 1,halfY * 2 + 1);


        for(i = 0; i < buildingMatrixSize; i++ ) {
            ix = 1 + i * 2;
            if (i >= halfX) {
                ix++;
            }
            for(j = 0; j < buildingMatrixSize; j++ ) {
                iy = 1 + j * 2;
                if (j >= halfY) {
                    iy++;
                }
                BuildingGfx buildingGfx = buildingMatrix[buildingMatrixSize - j - 1][ buildingMatrixSize - i - 1];
                if(buildingGfx == null) {
                    buildingGfx = park;
                }
                cityLayout[iy][ix] = buildingGfx;
            }
        }

        addWalls();
    }

    public void addWalls() {
        if(!walled) {
            return;
        }
        int e = cityLayoutMatrixSize - 1;
        int ey = e;
        /*if(isEmptyRow()) {
            ey--;
            ey--;
        } */

        int cx = cityCenter.x;
        int cy = cityCenter.y;

        if(wallx4) {
            for (int i = 0; i < cityLayoutMatrixSize; i++) {
                if (i < ey) {
                    cityLayout[i][0] = getBuilding("wall_l");
                    cityLayout[i][e] = getBuilding("wall_r");
                }
                cityLayout[0][i] = getBuilding("wall_b");
                cityLayout[ey][i] = getBuilding("wall_t");
            }
            cityLayout[0][0] = getBuilding("tower_l");
            cityLayout[0][e] = getBuilding("tower_b");
            cityLayout[ey][0] = getBuilding("tower_t");
            cityLayout[ey][e] = getBuilding("tower_r");
            cityLayout[cy][0] = getBuilding("gate_v");
            cityLayout[cy][e] = getBuilding("gate_v");
            cityLayout[0][cx] = getBuilding("gate_h");
            cityLayout[ey][cx] = getBuilding("gate_h");

        } else {
            for (int i = 0; i < cityLayoutMatrixSize; i++) {
                if (i < ey) {
                    cityLayout[i][0] = getBuilding("wallv");
                    cityLayout[i][e] = getBuilding("wallv");
                }
                cityLayout[0][i] = getBuilding("wallh");
                cityLayout[ey][i] = getBuilding("wallh");
            }
            cityLayout[0][0] = getBuilding("tower");
            cityLayout[0][e] = getBuilding("tower");
            cityLayout[ey][0] = getBuilding("tower");
            cityLayout[ey][e] = getBuilding("tower");
            cityLayout[cy][0] = getBuilding("gateh");
            cityLayout[cy][e] = getBuilding("gateh");
            cityLayout[0][cx] = getBuilding("gatev");
            cityLayout[ey][cx] = getBuilding("gatev");
        }
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

    public BuildingGfx getBuilding(String name) {
        BuildingGfx bgfx = null, _bgfx;
        name = getBuildingGfxNameByName(name);
        System.out.println(name);
        for(int i = 0; i < buildingsGfx.size(); i++) {
            _bgfx = buildingsGfx.get(i);
            if(_bgfx != null && _bgfx.name.equals(name) && Ruleset.hasAge(age, _bgfx.age)) {
                bgfx = buildingsGfx.get(i);
            }
        }
        return bgfx;
    }


    public String getBuildingGfxNameByName(String name) {
        switch (name) {
            case "townhall":
                return "house4";
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

    private void placeAqueduct(int x, int y) {
        for(int i = 0; i < buildingMatrixSize; i++) {
            BuildingGfx buildingGfx =  buildingMatrix[y][i];
            if(buildingGfx != null && buildingGfx.size == 4) {
                buildingMatrix[y][x] = buildingMatrix[y--][x];
                y--;
                buildingMatrix[y][x] = getBuilding("aqueduct");
                placeAqueduct(x,y);
            }
        }
    }

    private boolean setBuilding(int i, int j) {
        if (i < 0 || i >= buildingMatrixSize || j < 0 || j>= buildingMatrixSize) return false;
        if(buildingMatrix[i][j] != null) {
            return true;
        }
        if(bc < buildings.size()) {
            BuildingGfx buildingGfx =  getBuilding(buildings.get(bc));
            if(buildingGfx == null) {
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

    public void printCity() {
        printMatrix(buildingMatrix, buildingMatrixSize);
    }

    public void printCityLayout() {
        printMatrix(cityLayout, cityLayoutMatrixSize);
    }

    public void printMatrix(BuildingGfx[][] matrix, int size) {

        String symbol = "";
        for(int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if(matrix[y][x] != null) {
                    symbol = matrix[y][x].symbol;
                } else {
                    symbol = " ";
                }
                System.out.print(symbol + ' ');
            }
            System.out.println();
        }
    }

} 