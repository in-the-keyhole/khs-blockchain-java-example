package simple.chain;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class SimpleBlockchain<T extends Tx> {
	public List<Block<T>> chain = new ArrayList<Block<T>>();
	public SimpleBlockchain() {	}
	public SimpleBlockchain(List<Block<T>> blocks) {
		this();
		chain = blocks;
	}

	public List<Block<T>> getChain() {
		return chain;
	}

	public void setChain(List<Block<T>> chain) {
		this.chain = chain;
	}

	public Block<T> getHead() {
		
		Block<T> result = null;
		if (this.chain.size() > 0) {
			result = this.chain.get(0);
		} else {
		
	  	 throw new RuntimeException("No Block's have been added to chain...");
		}
		
		return result;
	}
	
	public Block<T> newBlock() {
		int count = chain.size();
		String previousHash = "root";

		if (count > 0)
			previousHash = blockChainHash();

		Block<T> block = new Block<T>();

		block.setTimeStamp(System.currentTimeMillis());
		block.setIndex(count);
		block.setPreviousHash(previousHash);
		chain.add(block);
		return block;
	}
	
	public SimpleBlockchain<T> add(T item) {

	   if (chain.size() == 0) {
			newBlock();
		} 		
		
		getHead().add(item);

		return this;
	}

	/* Deletes the index of the after. */
	public void DeleteAfterIndex(int index) {
		if (index >= 0) {
			Predicate<Block<T>> predicate = b -> chain.indexOf(b) >= index;
			chain.removeIf(predicate);
		}
	}

	public SimpleBlockchain<T> Clone() {
		List<Block<T>> clonedChain = new ArrayList<Block<T>>();
		Consumer<Block> consumer = (b) -> clonedChain.add(b.Clone());
		chain.forEach(consumer);
		return new SimpleBlockchain<T>(clonedChain);
	}

	/* Gets the root hash. */
	public String blockChainHash() {	
		return getHead().getHash();
	}
		
}