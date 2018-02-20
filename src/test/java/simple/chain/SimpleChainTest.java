package simple.chain;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleChainTest {

	@Test
	public void test() {

		SimpleBlockchain<String> chain1 = new SimpleBlockchain<String>();

		chain1.Add("A").Add("B").Add("C");

		SimpleBlockchain<String> chain2 = chain1.Clone();

		chain1.Add("D");
		chain2.Add("E");

		System.out.println(String.format("Chain 1 Hash: %s", chain1.blockChainHash()));
		System.out.println(String.format("Chain 2 Hash: %s", chain2.blockChainHash()));
		System.out.println(
				String.format("Chains Are In Sync: %s", chain1.blockChainHash().equals(chain2.blockChainHash())));

		// Synchronize the blockchain order
		chain2 = chain1.Clone();
		chain2.Add("E");

		chain1 = chain2.Clone();

		System.out.println(String.format("Chain 1 Hash: %s", chain1.blockChainHash()));
		System.out.println(String.format("Chain 2 Hash: %s", chain2.blockChainHash()));
		System.out.println(
				String.format("Chains Are In Sync: %s", chain1.blockChainHash().equals(chain2.blockChainHash())));

		System.out.println("Current Chain: ");
		for (int i = 0; i < chain1.chain.size(); i++) {
			System.out.println(chain1.chain.get(i).transactions);
		}
		
		assertTrue(chain1.blockChainHash().equals(chain2.blockChainHash()));

	}

}
