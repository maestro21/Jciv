package Civ.classes.gfx;



public class ImgDimensions {
    public int x = 0;
    public int y = 0;
    public int offX = 0;
    public int offY = 0;
    public

    ImgDimensions() {}

    ImgDimensions(int x, int y, int offX, int offY) {
        this.x = x;
        this.y = y;
        this.offX = offX;
        this.offY = offY;
    }

    @Override
    public String toString() {
        return String.format("size: %d, %d, offset: %d, %d", x, y, offX, offY);
    }
}