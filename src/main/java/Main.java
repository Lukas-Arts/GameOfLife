import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class Main extends JFrame {
	public Main(){
		super();
		this.setTitle("Game Of Life");
		this.setSize(800,600);

		GameOfLife gameOfLife=new GameOfLife(200,150,true);
		GameOfLifePanel gameOfLifePanel=new GameOfLifePanel(gameOfLife);
		this.add(gameOfLifePanel);

		this.setVisible(true);
		//getWindowDevice(this).setFullScreenWindow(this);


		Thread thread = new Thread(){
			@Override
			public void run() {
				super.run();
				while(true){
					ArrayList<boolean[][]> steps=new ArrayList<>();
					long seed=new Random().nextLong();
					int step=0;
					int maxStep=-1;
					gameOfLife.setup(5000, seed);
					System.out.println("new seed: "+seed);
					do {
						try {
							steps.add(gameOfLife.getGrid());
							gameOfLifePanel.revalidate();
							gameOfLifePanel.repaint();
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if(maxStep==-1){
							int repeatIndex=isRepeating(steps);
							if(repeatIndex!=-1){
								int diff=step-repeatIndex;
								System.out.println("Repetition found: "+step+" - "+repeatIndex+" = "+diff);
								maxStep=step+(diff)*(diff<10?3: (diff<50?2:(diff<200?1:0)));
							}
						}
						step++;
					}while (gameOfLife.doStep() && (maxStep==-1 || step<maxStep));

					System.out.println("finished seed: "+seed+" steps: "+step);
					try {
						Thread.sleep(2500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			private int isRepeating(ArrayList<boolean[][]> steps){
				for(boolean[][] step:steps){
					int i= steps.indexOf(step);
					if(i!=steps.size()-1 && isEqual(step,gameOfLife.getGrid())){
						return i;
					}
				}
				return -1;
			}
			private boolean isEqual(boolean[][] b1,boolean[][] b2){
				for (int i = 0; i < b1.length; i++) {
					for (int j = 0; j < b1[i].length; j++) {
						if(b1[i][j]!=b2[i][j]){
							return false;
						}
					}
				}
				return true;
			}
		};
		thread.start();
	}
	public static GraphicsDevice getWindowDevice(Window window) {
		Rectangle bounds = window.getBounds();
		return Arrays.stream(GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices())

				// pick devices where window located
				.filter(d -> d.getDefaultConfiguration().getBounds().intersects(bounds))

				// sort by biggest intersection square
				.sorted((f, s) -> Long.compare(//
						square(f.getDefaultConfiguration().getBounds().intersection(bounds)),
						square(s.getDefaultConfiguration().getBounds().intersection(bounds))))

				// use one with the biggest part of the window
				.reduce((f, s) -> s) //

				// fallback to default device
				.orElse(window.getGraphicsConfiguration().getDevice());
	}

	public static long square(Rectangle rec) {
		return Math.abs(rec.width * rec.height);
	}
	public static void main(String[] args){
		new Main();
	}
}