import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.Random;


public class Tank {
	public static final int XSPEED = 5;
	public static final int YSPEED = 5;
	public static final int WIDTH = 30;
	public static final int HEIGHT = 30;
	private static Random random = new Random();
	
	private int x,y;
	private boolean bL = false,bU = false,bR = false,bD = false;
	private boolean good;
	private boolean live = true;
	private int step = random.nextInt(12) + 1;
	private Direction dir = Direction.STOP;
	private Direction ptDir = Direction.R;
	private TankClient tc;
	public Tank(int x, int y,boolean good) {
		this.x = x;
		this.y = y;
		this.good = good;
	}
	
	public Tank(int x, int y,boolean good,Direction dir,TankClient tc) {
		this(x, y,good);
		this.dir = dir;
		this.tc=tc;
	}
	
	public void draw(Graphics g){
		Color c = g.getColor();
		if(good==true) {
			g.setColor(Color.RED);
		}
		else{
			g.setColor(Color.BLUE);
		}
		
		if(!this.live) return;
		g.fillOval(x, y, WIDTH, HEIGHT);
		g.setColor(c);
		move();
		switch(ptDir){
		case L:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x, y + Tank.HEIGHT/2);
			break;
		case LU:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x, y );
			break;
		case U:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH/2, y);
			break;
		case RU:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH, y);
			break;
		case R:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH, y + Tank.HEIGHT/2);
			break;
		case RD:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH, y + Tank.HEIGHT);
			break;
		case D:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH/2, y + Tank.HEIGHT);
			break;
		case LD:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x, y + Tank.HEIGHT);
			break;
		}
	}
	
	
	void move(){
		switch(dir){
		case L:
			x-=XSPEED;
			break;
		case LU:
			x-=XSPEED;
			y-=YSPEED;
			break;
		case U:
			y-=YSPEED;
			break;
		case RU:
			x+=XSPEED;
			y-=YSPEED;
			break;
		case R:
			x+=XSPEED;
			break;
		case RD:
			x+=XSPEED;
			y+=YSPEED;
			break;
		case D:
			y+=YSPEED;
			break;
		case LD:
			x-=XSPEED;
			y+=YSPEED;
			break;
		case STOP:
			break;
		}
		if(dir!=Direction.STOP) ptDir = dir;
		if(x<0) x=0;
		if(y<30) y=30;
		if(x>TankClient.GAME_WIDTH-Tank.WIDTH) x=TankClient.GAME_WIDTH-Tank.WIDTH;
		if(y>TankClient.GAME_HEIGHT-Tank.HEIGHT) y=TankClient.GAME_HEIGHT-Tank.HEIGHT;
		
		if(!good){
			Direction[] dirs = Direction.values();
			if(step==0){
				this.step = random.nextInt(12) +1;
				this.dir = dirs[random.nextInt(dirs.length)];
			}
			step--;
			if(random.nextInt(40)>38){
				this.fire();
			}
		}
	}
	
	public void keyPressed(KeyEvent e){
		switch(e.getKeyCode()){
		case KeyEvent.VK_LEFT:
			bL=true;
			break;
		case KeyEvent.VK_UP:
			bU=true;
			break;
		case KeyEvent.VK_RIGHT:
			bR=true;
			break;
		case KeyEvent.VK_DOWN:
			bD=true;
			break;
		}
		locateDirection();
	}
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()){
		case KeyEvent.VK_CONTROL:
			fire();
			break;
		case KeyEvent.VK_LEFT:
			bL=false;
			break;
		case KeyEvent.VK_UP:
			bU=false;
			break;
		case KeyEvent.VK_RIGHT:
			bR=false;
			break;
		case KeyEvent.VK_DOWN:
			bD=false;
			break;
		}
		locateDirection();
	}
	
	void locateDirection(){
		if(bL & !bU & !bR & !bD) dir = Direction.L;
		else if(bL & bU & !bR & !bD) dir = Direction.LU;
		else if(!bL & bU & !bR & !bD) dir = Direction.U;
		else if(!bL & bU & bR & !bD) dir = Direction.RU;
		else if(!bL & !bU & bR & !bD) dir = Direction.R;
		else if(!bL & !bU & bR & bD) dir = Direction.RD;
		else if(!bL & !bU & !bR & bD) dir = Direction.D;
		else if(bL & !bU & !bR & bD) dir = Direction.LD;
		else if(!bL & !bU & !bR & !bD) dir = Direction.STOP;
	}

	private Missile fire(){
		if(!this.live) return null;
		int x = this.x + Tank.WIDTH/2 - Missile.WIDTH/2;
		int y = this.y + Tank.HEIGHT/2 - Missile.HEIGHT/2;
		Missile m = new Missile(x,y,good,ptDir,tc);
		tc.getMissiles().add(m);
		return m;
	}
	public Rectangle getRectangle(){
		return new Rectangle(x,y,WIDTH,HEIGHT);
	}
	
	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	public boolean isGood() {
		return good;
	}
}
