import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;



public class Missile {
	public static final int WIDTH = 10;
	public static final int HEIGHT = 10;
	public static final int XSPEED = 10;
	public static final int YSPEED = 10;
	private boolean live = true;
	private boolean good;
	private int x,y;
	private Tank.Direction dir;
	private TankClient tc;
	
	public Missile(int x, int y, Tank.Direction dir) {
		this.x = x;
		this.y = y;
		this.dir = dir;
	}
	
	
	public Missile(int x, int y, boolean good ,Tank.Direction dir,TankClient tc) {
		this(x,y,dir);
		this.good = good;
		this.tc=tc;
	}
	
	public void draw(Graphics g){
		Color c = g.getColor();
		g.setColor(Color.BLACK);
		g.fillOval(x, y, WIDTH, HEIGHT);
		g.setColor(c);
		move();
	}

	private void move() {
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
		}
		
		
		if(x<0||y<0||x>TankClient.GAME_WIDTH+Missile.WIDTH||y>TankClient.GAME_HEIGHT+Missile.HEIGHT) tc.getMissiles().remove(this);
		
		
		
		hitTanks(tc.getEnemies());
		hitTank(tc.getMyTank());
		
	}
	
	private boolean hitTanks(List<Tank> tanks) {
		for(int i=0;i<tanks.size();i++){
			if(hitTank(tanks.get(i))){
				return true;
			}
		}
		return false;
	}


	public boolean hitTank(Tank tank){
		if(this.isLive() && tank.isLive() && tank.getRectangle().intersects(this.getRectangle()) && tank.isLive() && this.good != tank.isGood()) {
			this.live = false;
			this.tc.getMissiles().remove(this);
			tank.setLive(false);
			this.tc.getEnemies().remove(tank);
			Explode explode = new Explode(this.x,this.y,this.tc);
			tc.getExplodes().add(explode);
			return true;
		}
		return false;
	}
	
	public Rectangle getRectangle(){
		return new Rectangle(x,y,WIDTH,HEIGHT);
	}
	
	public boolean isLive() {
		return live;
	}
}
