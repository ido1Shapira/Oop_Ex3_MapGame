package gameClient;

/**
*this class is the only connection we have directly with the client
*in order to start the game all the client needs to do is to run this class
*this class is the main that activated by the executable jar
*/

public class SimpleGameClient {
	public static void main(String[] a) {
		MyGameGUI gui = MyGameGUI.getGui(); //starts the gui thread that activates the level picking window starting the game
		Thread toPaint = new Thread(gui);
		toPaint.start();
	}
}