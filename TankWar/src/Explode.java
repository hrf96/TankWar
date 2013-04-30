import java.awt.Color;
import java.awt.Graphics;


public class Explode {
	private int x;
	private int y;
	private boolean live = true;
	private int[] diameter={4,7,12,18,26,32,49,30,14,6};
	private int step = 0;
	private TankClient tc;
	
	public Explode(int x,int y,TankClient tc){
		this.x = x;
		this.y = y;
		this.tc = tc;
	}
	
	//获得画板，在画板上画出爆炸
	public void draw(Graphics g){
		if(!live) return;
		
		if(step==diameter.length){
			live = false;
			step = 0;
			return;
		}
		
		Color c = g.getColor();
		g.setColor(Color.ORANGE);
		g.fillOval(x, y, diameter[step], diameter[step]);
		g.setColor(c);
		step++;
		
	}
}
