package helpers;
import static org.junit.Assert.*;

import org.junit.Test;


public class HashTest {

	@Test
	public void test() {
		
		String hash = helpers.SHA256.generateHash("TEST String");
		System.out.println(hash);
		assertTrue(hash.length() == 64);
		
	}

}
