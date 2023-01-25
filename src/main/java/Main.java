import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Main extends JFrame implements MouseInputListener {
	private boolean isPaused=false;
	private ArrayList<boolean[][]> steps=new ArrayList<>();
	private long seed=new Random().nextLong();
	private int step=0;
	private int maxStep=-1;
	private Settings settings=new Settings(2000,200,150,true);
	private GameOfLife gameOfLife;
	private final GameOfLifePanel gameOfLifePanel;
	public Main(){
		super();
		this.setTitle("Game Of Life");
		this.setSize(800,600);
		this.setLayout(new BorderLayout());

		this.gameOfLife=new GameOfLife(this.settings.getGridWidth(),this.settings.getGridHeight(),this.settings.isWrapBorders());
		this.gameOfLifePanel=new GameOfLifePanel(gameOfLife);
		SettingsPanel settingsPanel=new SettingsPanel(this,settings);

		this.add(gameOfLifePanel,BorderLayout.CENTER);
		this.add(settingsPanel,BorderLayout.EAST);

		gameOfLifePanel.addMouseMotionListener(this);
		gameOfLifePanel.addMouseListener(this);

		this.setVisible(true);
		//getWindowDevice(this).setFullScreenWindow(this);


		Thread thread = new Thread(){
			@Override
			public void run() {
				super.run();
				while(true){
					createNewSeed(settings.getDots());
					do {
						if(isPaused){
							try {
								Thread.sleep(10);
							} catch (InterruptedException e) {
								throw new RuntimeException(e);
							}
						}else{
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
									//if the diff between the last repeatIndex and the current step is
									// - less than 10, let the repeating pattern appear 3 more times
									// - less than 50, let the repeating pattern appear 2 more times
									// - less than 50, let the repeating pattern appear one more times
									// - otherwise don't repeat the pattern and go on to create a new seed
									maxStep=step+(diff)*(diff<10?3: (diff<50?2:(diff<200?1:0)));
								}
							}
							step++;
							gameOfLife.doStep();
						}
					}while ((maxStep==-1 || step<maxStep));

					System.out.println("finished seed: "+seed+" steps: "+step);
					try {
						Thread.sleep(2500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		thread.start();
	}
	public void setSettings(Settings settings){
		this.settings=settings;
		this.isPaused=true;
		this.gameOfLife=new GameOfLife(this.settings.getGridWidth(),this.settings.getGridHeight(),this.settings.isWrapBorders());
		this.gameOfLifePanel.setGameOfLife(gameOfLife);
		this.createNewSeed(this.settings.getDots());
		this.togglePaused();
	}
	private void createNewSeed(int dots){
		this.steps=new ArrayList<>();
		this.seed=new Random().nextLong();
		this.step=0;
		this.maxStep=-1;
		this.gameOfLife.setup(dots, this.seed);
		System.out.println("new seed: "+this.seed);
	}
	public boolean togglePaused(){
		this.isPaused=!this.isPaused;
		this.gameOfLifePanel.setEdit(this.isPaused);
		return this.isPaused;
	}
	private int isRepeating(ArrayList<boolean[][]> steps){
		//travers reverse through the steps and check if they were equal to the current step
		if(steps.size()==1)return -1;
		for(int i=steps.size()-2;i>=0;i--){
			if(isEqual(steps.get(i),gameOfLife.getGrid())) {
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

	@Override
	public void mouseDragged(MouseEvent e) {

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if(isPaused){
			this.gameOfLifePanel.setEdit(e.getX(),e.getY());
			this.gameOfLifePanel.revalidate();
			this.gameOfLifePanel.repaint();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(isPaused){
			this.gameOfLifePanel.changeEdit();
			this.gameOfLifePanel.revalidate();
			this.gameOfLifePanel.repaint();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}
}