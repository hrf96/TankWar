import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;


public class TankClient extends Frame{

	private static final long serialVersionUID = 1L;
	public static final int GAME_WIDTH = 640;
	public static final int GAME_HEIGHT = 480;
	private Tank myTank = new Tank(50,50,true,Direction.STOP,this);
	private List<Tank> enemies = new ArrayList<Tank>();
	private List<Explode> explodes = new ArrayList<Explode>();
	private List<Missile> missiles = new ArrayList<Missile>();
	private Image offScreenImage = null;
	

	
	
	public void paint(Graphics g) {
		g.drawString("Missiles count:" + missiles.size(), 10, 50);
		g.drawString("Explodes count:" + explodes.size(), 10, 70);
		g.drawString("Enemies count:" + enemies.size(), 10, 90);
		
		
		myTank.draw(g);
		
		//循环画出子弹
		for(int i=0;i<missiles.size();i++){
			Missile m = missiles.get(i);
			if(m.isLive()){
				m.draw(g);
			}
		}
		//循环画出爆炸
		for(int i=0;i<explodes.size();i++){
			Explode explode = explodes.get(i);
			explode.draw(g);
		}
		
		
		
		//循环画出敌人坦克
		for(int i=0;i<enemies.size();i++){
			Tank enemy = enemies.get(i);
			if(enemy.isLive()){
				enemy.draw(g);
			}
		}
		
		
	}

	public void update(Graphics g) {
		if(offScreenImage==null){
			offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
		}
		Graphics gOffScreen = offScreenImage.getGraphics();
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(Color.GRAY);
		gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
		gOffScreen.setColor(c);
		paint(gOffScreen);
		g.drawImage(offScreenImage, 0, 0, null);
	}
	
	public void lanchFrame(){
		this.setLocation(100, 100);
		this.setSize(GAME_WIDTH, GAME_HEIGHT);
		this.setTitle("坦克大战");
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		this.addKeyListener(new KeyMonitor());
		this.setResizable(false);
		this.setBackground(Color.GRAY);
		this.setVisible(true);
		new Thread(new PaintThread()).start();
	}
	
	
	
	public Tank getMyTank() {
		return myTank;
	}


	public List<Missile> getMissiles() {
		return missiles;
	}

	public Image getOffScreenImage() {
		return offScreenImage;
	}

	private class PaintThread implements Runnable{

		public void run() {
			while(true){
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				repaint();
			}
		}
		
	}
	
	private class KeyMonitor extends KeyAdapter{
		public void keyReleased(KeyEvent e) {
			myTank.keyReleased(e);
		}

		public void keyPressed(KeyEvent e) {
			myTank.keyPressed(e);
		}
		
	}
	
	
	public static void main(String[] args) {
		TankClient tc = new TankClient();
		tc.lanchFrame();
	}

	public List<Explode> getExplodes() {
		return explodes;
	}

	public List<Tank> getEnemies() {
		return enemies;
	}
	
	
}
