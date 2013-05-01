import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * 这是TankWar服务器端主程序类
 * @author hanrunfan
 *
 */
public class TankServer {
	/**
	 * 定义了一个常量，保存服务器监听TCP的端口号
	 */
	public static final int TCP_PORT = 8888;
	/**
	 * 连接到服务器的所有客户信息都保存在这个List中
	 */
	private List<Client> clients = new ArrayList<Client>();
	
	private static int ID = 100;
	
	public void start(){
		ServerSocket ss = null;
		try {
			ss = new ServerSocket(TCP_PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Socket s = null;
		while(true){
			try {
				s = ss.accept();
				DataInputStream dis = new DataInputStream(s.getInputStream());
				String IP = s.getInetAddress().getHostAddress();
				int udpPort = dis.readInt();
				Client c = new Client(IP,udpPort);
				clients.add(c);
				DataOutputStream dos = new DataOutputStream(s.getOutputStream());
				dos.writeInt(ID++);
				System.out.println("A Client is connect! Addr - " + s.getInetAddress() +":" + s.getPort() + "--------UDP Port:" + udpPort);
			} catch (IOException e) {
				e.printStackTrace();
			} finally{
				if(s != null){
					try {
						s.close();
						s = null;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	public static void main(String[] args) {
		new TankServer().start();
	}

	
	
	private class Client{
		String IP;
		int udpPort;
		
		public Client(String IP,int udpPort){
			this.IP = IP;
			this.udpPort = udpPort;
		}
	}
}
