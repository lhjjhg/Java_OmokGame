package gameServer;

public class ServerMain {
	private static final int portNum = 8080;
	public static void main(String[] args) {
		new ServerBack(portNum);
	}
}
