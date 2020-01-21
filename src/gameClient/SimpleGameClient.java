package gameClient;

public class SimpleGameClient {
	public static void main(String[] a) {
		MyGameGUI gui = MyGameGUI.getGui(); //starts the gui thread 
		Thread toPaint = new Thread(gui);
		toPaint.start();
	}
}