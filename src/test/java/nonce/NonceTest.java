package nonce;

import static org.junit.Assert.*;

import org.junit.Test;
import helpers.SHA256;

public class NonceTest {

	@Test
	public void test() {
		String message = "Keyhole Software";

		System.out.println("Message: " + message);

		String hashValue = SHA256.generateHash(message);

		System.out.println(String.format("Hash: %s", hashValue));

		String nonceKey = "12345";
		long nonce = 0;
		boolean nonceFound = false;
		String nonceHash = "";

		long start = System.currentTimeMillis();

		while (!nonceFound) {

			nonceHash = SHA256.generateHash(message + nonce);
			nonceFound = nonceHash.substring(0, nonceKey.length()).equals(nonceKey);
			nonce++;
	
		}

		long ms = System.currentTimeMillis() - start;

		System.out.println(String.format("Nonce: %s ", ms));
		System.out.println(String.format("Nonce Hash: %s", nonceHash));
		System.out.println(String.format("Nonce Search Time: %s ms", ms));
		
		assertTrue(nonceFound);

	}

}
