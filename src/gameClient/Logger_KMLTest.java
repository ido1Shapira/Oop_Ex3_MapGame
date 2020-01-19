package gameClient;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.jupiter.api.Test;

class Logger_KMLTest {

	/**
	 * this test meant to check whether Logger_MKL creates file and is able to write into it
	 */
	@Test
	void testLogger_KML() {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader("data/11.kml"));
			String line;
			try {
				while ((line = br.readLine()) != null) {
					System.out.println(line);
				}
			} catch (IOException e) {

				e.printStackTrace();
				fail("something went wrong");
			}
		}
		catch (FileNotFoundException e) {	
			e.printStackTrace();
			fail("something went wrong");
		}
	}


}
