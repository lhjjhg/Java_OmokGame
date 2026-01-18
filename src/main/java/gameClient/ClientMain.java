package gameClient;

import gameClient.Main.MainFrame;

public class ClientMain  {
	private static final int portNum = 8080;
	public static void main(String args[]) {
		new MainFrame(portNum);
	}
}