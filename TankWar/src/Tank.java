import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Tank {
	public static final int XSPEED = 3;
	public static final int YSPEED = 3;
	public static final int WIDTH = 30;
	public static final int HEIGHT = 30;
	private static Random random = new Random();
	
	private int x,y;
	private int oldX,oldY;
	private boolean bL = false,bU = false,bR = false,bD = false;
	private boolean good;
	private boolean live = true;
	private int step = random.nextInt(12) + 1;
	private int life =100;
	private BloodBar bb = new BloodBar();
	enum Direction {L,LU,U,RU,R,RD,D,LD,STOP};
	private Direction dir = Direction.STOP;
	private Direction ptDir = Direction.R;
	private TankClient tc;
	public Tank(int x, int y,boolean good) {
		this.x = x;
		this.y = y;
		this.oldX = x;
		this.oldY = y;
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
		bb.draw(g);
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
		move();
	}
	
	
	
	void move(){
		this.oldX = this.x;
		this.oldY = this.y;
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
		
		//限制坦克的活动范围
		preventOut();
		//防止坦克撞墙
		collidesWithWalls(tc.getWalls());
		//防止坦克互相穿越
		collidesWithTanks(tc.getEnemies());
		collidesWithTank(tc.getMyTank());
		//吃血
		eatBlood(tc.getBlood());
		//设置敌方坦克自动行走和自动发射子弹
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
	
	
	
	
	
	
	
	private boolean eatBlood(Blood blood) {
		if(this.isLive() && blood.isLive() && blood.getRectangle().intersects(this.getRectangle())){
			blood.setLive(false);
			this.life += 20;
			if(this.life>100){
				this.life = 100;
			}
			return true;
		}
		return false;
	}

	private boolean collidesWithTanks(List<Tank> tanks) {
		for(int i=0;i<tanks.size();i++){
			if(tanks.get(i)!=this && collidesWithTank(tanks.get(i))){
				return true;
			}
		}
		return false;
	}

	private boolean collidesWithTank(Tank tank) {
		if(tank != this && tank.getRectangle().intersects(this.getRectangle())){
			stay();
			tank.stay();
			return true;
		}
		return false;
	}

	private void preventOut() {
//		if(x<0) x=0;
//		if(y<30) y=30;
//		if(x>TankClient.GAME_WIDTH-Tank.WIDTH) x=TankClient.GAME_WIDTH-Tank.WIDTH;
//		if(y>TankClient.GAME_HEIGHT-Tank.HEIGHT) y=TankClient.GAME_HEIGHT-Tank.HEIGHT;
		if(x<0 ||y<30||x>TankClient.GAME_WIDTH-Tank.WIDTH||y>TankClient.GAME_HEIGHT-Tank.HEIGHT){
			stay();
		}
	}

	
	private boolean collidesWithWalls(List<Wall> walls) {
		for(int i=0;i<walls.size();i++){
			if(collidesWithWall(walls.get(i))){
				return true;
			}
		}
		return false;
	}


	
	private boolean collidesWithWall(Wall wall) {
		if(this.isLive() && wall.getRectangle().intersects(this.getRectangle())){
			stay();
		}
		return false;
	}

	private void stay() {
		this.x = this.oldX;
		this.y = this.oldY;
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
		case KeyEvent.VK_A:
			superFire();
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
	
	private Missile fire(Direction dir) {
		if(!this.live) return null;
		int x = this.x + Tank.WIDTH/2 - Missile.WIDTH/2;
		int y = this.y + Tank.HEIGHT/2 - Missile.HEIGHT/2;
		Missile m = new Missile(x,y,good,dir,tc);
		tc.getMissiles().add(m);
		return m;
	}
	
	private List<Missile> superFire(){
		Direction[] dirs = Direction.values();
		List<Missile> missiles = new ArrayList<Missile>();
		for(int i=0;i<8;i++){
			missiles.add(fire(dirs[i]));
		}
		return missiles;
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

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}
	
	private class BloodBar{
		private final int totalWidth = Tank.WIDTH;
		private final int borderWidth = 1;
		private final int innerWidth = totalWidth - borderWidth * 2;
		private final int innerHeight = 4;
		private final int totalHeight = innerHeight + borderWidth * 2;
		private final Color borderColor = Color.WHITE;
		private final Color innerColor = Color.RED;
		public void draw(Graphics g){
			Color c = g.getColor();
			g.setColor(borderColor);
			g.drawRect(x, y-totalHeight, totalWidth, totalHeight);
			g.setColor(innerColor);
			g.fillRect(x+borderWidth, y-totalHeight+borderWidth, innerWidth*life/100+1, innerHeight+1);
			g.setColor(c);
		}
	}
}
