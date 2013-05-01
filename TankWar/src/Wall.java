import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;


public class Wall {
	private int x;
	private int y;
	private int width;
	private int height;
	private boolean live = true;
	@SuppressWarnings("unused")
	private TankClient tc;
	private Color color = Color.ORANGE;
	
	public Wall(int x,int y,int width,int height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public Wall(int x,int y,int width,int height,TankClient tc){
		this(x,y,width,height);
		this.tc = tc;
	}
	
	public Wall(int x,int y,int width,int height,TankClient tc,Color color){
		this(x,y,width,height,tc);
		this.color = color;
	}
	
	public void draw(Graphics g){
		Color color = g.getColor();
		g.setColor(this.color);
		g.fillRect(x, y, width, height);
		g.setColor(color);
	}

	public Rectangle getRectangle(){
		return new Rectangle(x,y,width,height);
	}
	
	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	
}
