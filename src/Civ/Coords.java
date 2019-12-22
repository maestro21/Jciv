package Civ;

public class Coords {

    public int x = 0;
    public int y = 0;
    public int cols = 0;

    public int index(int x, int y) {
        return y * cols + x;
    }

    public int[] xy(int index) {
        x = index % cols;
        y = index / cols;
        int[] ret = new int[]{x,y};
        return ret;
    }

    public void debug(){
        debug("");
    }

    public void debug(String text){
        System.out.print(text);
        System.out.print(x);
        System.out.print('x');
        System.out.println(y);
    }
}
