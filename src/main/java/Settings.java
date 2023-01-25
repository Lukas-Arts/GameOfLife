public class Settings{
    int dots, gridWidth, gridHeight;
    boolean wrapBorders;
    public Settings(int dots,int gridWidth,int gridHeight,boolean wrapBorders) {
        this.dots=dots;
        this.gridWidth=gridWidth;
        this.gridHeight=gridHeight;
        this.wrapBorders=wrapBorders;
    }
    public Settings copy(){
        return new Settings(dots,gridWidth,gridHeight,wrapBorders);
    }

    public int getDots() {
        return dots;
    }

    public void setDots(int dots) {
        this.dots = dots;
    }

    public int getGridWidth() {
        return gridWidth;
    }

    public void setGridWidth(int gridWidth) {
        this.gridWidth = gridWidth;
    }

    public int getGridHeight() {
        return gridHeight;
    }

    public void setGridHeight(int gridHeight) {
        this.gridHeight = gridHeight;
    }

    public boolean isWrapBorders() {
        return wrapBorders;
    }

    public void setWrapBorders(boolean wrapBorders) {
        this.wrapBorders = wrapBorders;
    }
}
