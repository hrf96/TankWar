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


/**
 * 这个类的作用是坦克游戏的主窗口
 * @author hanrunfan
 *
 */
public class TankClient extends Frame{

	private static final long serialVersionUID = 1L;
	/**
	 * 整个坦克游戏的宽度
	 */
	public static final int GAME_WIDTH = 640;
	/**
	 * 整个坦克游戏的高度
	 */
	public static final int GAME_HEIGHT = 480;
	private Tank myTank = new Tank(50,50,true,Tank.Direction.STOP,this);
	private List<Tank> enemies = new ArrayList<Tank>();
	private List<Explode> explodes = new ArrayList<Explode>();
	private List<Missile> missiles = new ArrayList<Missile>();
	private List<Wall> walls = new ArrayList<Wall>();
	private Blood blood = new Blood();
	private Image offScreenImage = null;
	

	
	
	public void paint(Graphics g) {
		g.drawString("Missiles count:" + missiles.size(), 10, 50);
		g.drawString("Explodes count:" + explodes.size(), 10, 70);
		g.drawString("Enemies count:" + enemies.size(), 10, 90);
		g.drawString("Blood:" + myTank.getLife(), 10, 110);
		
		
		//循环画出墙
		for(int i=0;i<walls.size();i++){
			Wall w = walls.get(i);
			if(w.isLive()){
				w.draw(g);
			}
		}
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
		
		
		if(enemies.size()<=0){
			for(int i=0;i<6;i++){
				enemies.add(new Tank(50 + i * 100,100,false,Tank.Direction.D,this));
			}
		}
		
		
		//循环画出敌人坦克
		for(int i=0;i<enemies.size();i++){
			Tank enemy = enemies.get(i);
			if(enemy.isLive()){
				enemy.draw(g);
			}
		}
		
		//画出血块来
		if(blood.isLive()){
			blood.draw(g);
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
	
	
	
	
	/**
	 * 本方法显示坦克主窗口，初始化数据
	 */
	public void lanchFrame(){
		
		//添加墙
		walls.add(new Wall(70,200,20,200,this));
		walls.add(new Wall(170,300,250,20,this));
		walls.add(new Wall(270,200,20,100,this));
		walls.add(new Wall(370,200,20,100,this));
		
		//添加敌方坦克
		for(int i=0;i<6;i++){
			enemies.add(new Tank(50 + i * 100,100,false,Tank.Direction.D,this));
		}
		
		
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
			if(e.getKeyCode() == KeyEvent.VK_F2){
				myTank = new Tank(50,50,true,Tank.Direction.STOP,TankClient.this);
			}
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

	public List<Wall> getWalls() {
		return walls;
	}

	public Blood getBlood() {
		return blood;
	}
	
	
}
