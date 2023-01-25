import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GameOfLifePanel extends JPanel {
	private GameOfLife gameOfLife;
	private boolean edit=false;
	private int edit_x=0,edit_y=0;
	public GameOfLifePanel(GameOfLife gol){
		this.gameOfLife=gol;
	}
	public void setGameOfLife(GameOfLife gol){
		this.gameOfLife=gol;
	}
	public void paint(Graphics g){
		BufferedImage img = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = img.createGraphics();

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0,0,this.getWidth(),this.getHeight());
		double wx=this.getWidth()/(double)this.gameOfLife.getWidth();
		double hx=this.getHeight()/(double)this.gameOfLife.getHeight();
		for(int w=0;w<gameOfLife.getWidth();w++){
			for(int h=0;h<gameOfLife.getHeight();h++){
				int livingNeighbours=gameOfLife.numberOfLivingNeighbours(w,h);
				if(gameOfLife.isLiving(w,h)){
					if(livingNeighbours<2){
						g2d.setColor(new Color(23, 51, 87));
					}else if(livingNeighbours>3){
						g2d.setColor(new Color(23, 51, 87));
					}else {
						g2d.setColor(new Color(27, 74, 134));
					}
				}else{
					if(edit && (w==edit_x && h==edit_y)) {
						g2d.setColor(Color.RED);
					}else{
						g2d.setColor(new Color(11, 28, 49));
					}
				}
				g2d.fillRect((int) (w*wx), (int) (h*hx), (int) wx, (int) hx);
				//if an dot is bigger than 2x2 px, draw a rect around it
				if(wx>2 && hx>2){
					g2d.setColor(new Color(9, 26, 45));
					g2d.drawRect((int) (w*wx), (int) (h*hx), (int) wx, (int) hx);
				}
			}
		}
		g2d.dispose();
		//img = transposedHBlur(img);
		//img = transposedHBlur(img);
		g.drawImage(img,0,0,this);
	}
	public void setEdit(boolean edit){
		this.edit=edit;
	}
	public void setEdit(int edit_x,int edit_y){
		double wx=this.getWidth()/(double)this.gameOfLife.getWidth();
		double hx=this.getHeight()/(double)this.gameOfLife.getHeight();
		this.edit_x=(int)(edit_x/wx);
		this.edit_y=(int)(edit_y/hx);
	}
	public void changeEdit(){
		this.gameOfLife.getGrid()[this.edit_x][edit_y]=!this.gameOfLife.getGrid()[this.edit_x][edit_y];
	}
	private BufferedImage transposedHBlur(BufferedImage im) {
		int height = im.getHeight();
		int width = im.getWidth();
		// result is transposed, so the width/height are swapped
		BufferedImage temp =  new BufferedImage(height, width, BufferedImage.TYPE_INT_RGB);
		float[] k = new float[]{ 0.00598f, 0.060626f, 0.241843f, 0.383103f, 0.241843f, 0.060626f, 0.00598f };
		// horizontal blur, transpose result
		for (int y = 0; y < height; y++) {
			for (int x = 3; x < width - 3; x++) {
				float r = 0, g = 0, b = 0;
				for (int i = 0; i < 7; i++) {
					int pixel = im.getRGB(x + i - 3, y);
					b += (pixel & 0xFF) * k[i];
					g += ((pixel >> 8) & 0xFF) * k[i];
					r += ((pixel >> 16) & 0xFF) * k[i];
				}
				int p = (int)b + ((int)g << 8) + ((int)r << 16);
				// transpose result!
				temp.setRGB(y, x, p);
			}
		}
		return temp;
	}
}