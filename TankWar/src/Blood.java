import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;


public class Blood {
	private int x;
	private int y;
	private int width = 10;
	private int height = 10;
	private int[][] pos = {{500,250},{450,325},{525,400},{525,300}};
	private int step = 0;
	private boolean live = true;

	public Blood(){
		this.x = pos[0][0];
		this.y = pos[0][1];
	}
	
	public void draw(Graphics g){
		Color c = g.getColor();
		g.setColor(Color.PINK);
		g.fillRect(pos[step][0], pos[step][1], width, height);
		g.setColor(c);
		move();
	}
	private void move() {
		if(step==pos.length-1){
			step = 0;
		}else{
			step ++;
		}
		this.x = pos[step][0];
		this.y = pos[step][1];
	}
	
	public Rectangle getRectangle(){
		return new Rectangle(this.x,this.y,this.width,this.height);
	}

	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}
}
