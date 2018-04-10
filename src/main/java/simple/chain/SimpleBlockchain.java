package simple.chain;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class SimpleBlockchain<T extends Tx> {
	public static final int BLOCK_SIZE = 10;
	public List<Block<T>> chain = new ArrayList<Block<T>>();

	public SimpleBlockchain() {
		// create genesis block
		chain.add(newBlock());
	}

	public SimpleBlockchain(List<Block<T>> blocks) {
		this();
		chain = blocks;
	}

	public Block<T> getHead() {

		Block<T> result = null;
		if (this.chain.size() > 0) {
			result = this.chain.get(this.chain.size() - 1);
		} else {

			throw new RuntimeException("No Block's have been added to chain...");
		}

		return result;
	}

	public void addAndValidateBlock(Block<T> block) {

		// compare previous block hash back to genesis hash
		Block<T> current = block;
		for (int i = chain.size() - 1; i >= 0; i--) {
			Block<T> b = chain.get(i);
			if (b.getHash().equals(current.getPreviousHash())) {
				current = b;
			} else {

				throw new RuntimeException("Block Invalid");
			}

		}

		this.chain.add(block);

	}

	public boolean validate() {

		String previousHash = chain.get(0).getHash();
		for (Block<T> block : chain) {
			String currentHash = block.getHash();
			if (!currentHash.equals(previousHash)) {
				return false;
			}

			previousHash = currentHash;

		}

		return true;

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
		return block;
	}

	public SimpleBlockchain<T> add(T item) {

		if (chain.size() == 0) {
			// genesis block
			this.chain.add(newBlock());
		}

		// See if head block is full
		if (getHead().getTransactions().size() >= BLOCK_SIZE) {
			this.chain.add(newBlock());
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

	public List<Block<T>> getChain() {
		return chain;
	}

	public void setChain(List<Block<T>> chain) {
		this.chain = chain;
	}

	/* Gets the root hash. */
	public String blockChainHash() {
		return getHead().getHash();
	}

}