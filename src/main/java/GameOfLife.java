import java.util.Arrays;
import java.util.Random;

public class GameOfLife {
	private boolean[][] grid;
	private final boolean wrapBorders;
	private final int width;
	private final int height;
	public GameOfLife(int width, int height, boolean wrapBorders){
		this.grid=new boolean[width][height];
		this.width=width;
		this.height=height;
		this.wrapBorders=wrapBorders;
	}
	public void setup(int dots,long seed){
		for(int w=0;w<width;w++) {
			for (int h = 0; h < height; h++) {
				this.grid[w][h]=false;
			}
		}
		Random random = new Random(seed);
		while(dots>0) {
			int w = random.nextInt(width);
			int h = random.nextInt(height);
			if (!this.grid[w][h]) {
				this.grid[w][h]=true;
				dots--;
			}
		}
	}
	public boolean doStep() {
		boolean[][] grid = new boolean[width][height];
		for (int i = 0; i < this.grid.length; i++){
			for (int j = 0; j < this.grid[i].length; j++){
				grid[i][j] = this.grid[i][j];
			}
		}
		boolean changed=false;
		for(int w=0;w<width;w++){
			for(int h=0;h<height;h++){
				int livingNeighbours=numberOfLivingNeighbours(w,h);
				if (this.grid[w][h]) {
					if(livingNeighbours<2){
						changed=true;
						grid[w][h]=false;
					}else if(livingNeighbours>3){
						changed=true;
						grid[w][h]=false;
					}
				}else if(livingNeighbours==3){
					changed=true;
					grid[w][h]=true;
				}
			}
		}
		this.grid=grid;
		return changed;
	}
	public int numberOfLivingNeighbours(int w,int h){
		int livingNeighbours=0;
		for(int i=w-1;i<w+2;i++){
			for(int j=h-1;j<h+2;j++){
				if(wrapBorders || (i>=0&&i<width && j>=0&&j<height)){
					int w2=i, h2=j;
					if(w2<0)w2+=width;
					if(w2>=width)w2-=width;
					if(h2<0)h2+=height;
					if(h2>=height)h2-=height;
					if(!(w2==w && h2==h)){
						if(grid[w2][h2])livingNeighbours++;
					}
				}
			}
		}
		return livingNeighbours;
	}

	public boolean[][] getGrid() {
		return grid;
	}

	public boolean isLiving(int width, int height){
		return grid[width][height];
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}