package simple.chain;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleChainTest {


	@Test
	public void test() {

		SimpleBlockchain<Transaction> chain1 = new SimpleBlockchain<Transaction>();
	
		chain1.add(new Transaction("A")).add(new Transaction("B")).add(new Transaction("C"));

		SimpleBlockchain<Transaction> chain2 = chain1.Clone();

		chain1.add(new Transaction("D"));
		//chain2.add(new Transaction("E"));

		System.out.println(String.format("Chain 1 Hash: %s", chain1.blockChainHash()));
		System.out.println(String.format("Chain 2 Hash: %s", chain2.blockChainHash()));
		System.out.println(
				String.format("Chains Are In Sync: %s", chain1.blockChainHash().equals(chain2.blockChainHash())));

		// Synchronize the blockchain order,
		// Clone() emulates sending block across the Peer Network 
		// in a very simplified manner
		//chain2 = chain1.Clone();
		//chain2.add(new Transaction("E"));
		chain2.add(new Transaction("D"));
		//chain1 = chain2.Clone();

		System.out.println(String.format("Chain 1 Hash: %s", chain1.blockChainHash()));
		System.out.println(String.format("Chain 2 Hash: %s", chain2.blockChainHash()));
		System.out.println(
				String.format("Chains Are In Sync: %s", chain1.blockChainHash().equals(chain2.blockChainHash())));

		System.out.println("Current Chain: ");
		for (int i = 0; i < chain1.chain.size(); i++) {
			System.out.println(chain1.chain.get(i).getTransactions());
		}

		assertTrue(chain1.blockChainHash().equals(chain2.blockChainHash()));

	}
	
	@Test
	public void merkleTreeTest() {
		
		
		// create chain, add transaction
		
		SimpleBlockchain<Transaction> chain1= new SimpleBlockchain<Transaction>();

		chain1.add(new Transaction("A")).add(new Transaction("B")).add(new Transaction("C")).add(new Transaction("D"));
	
		// get a block in chain
		Block<Transaction> block = chain1.getHead(); 
	   
		System.out.println("Merkle Hash tree :"+block.merkleTree());
		
		// get a transaction from block 
		Transaction tx = block.getTransactions().get(0);
		
		// see if hash is valid... using merkle Tree...
		
		block.isTransactionValid(tx);
		assertTrue( block.isTransactionValid(tx) );
		
		// mutate the transaction data 
		tx.setValue("Z");
		
		assertFalse(block.isTransactionValid(tx) );
		
	
		
		
	}

}
