package simple.chain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class SimpleBlockchain<T> {
	public List<Block<T>> chain;
	private Calendar clock;

	public SimpleBlockchain() {

		chain = new ArrayList<Block<T>>();

	}

	private SimpleBlockchain(List<Block<T>> blocks) {
		clock = Calendar.getInstance();
		chain = blocks;
	}

	public SimpleBlockchain<T> Add(T item) {
		int count = chain.size();
		String previousHash = "root";

		if (chain.size() > 0)
			previousHash = blockChainHash();

		Block<T> block = new Block<T>();

		block.timeStamp = Calendar.getInstance();
		block.index = count;
		block.transactions = item;
		block.previousHash = previousHash;
		chain.add(block);

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
		Consumer<Block> consumer = (b) -> clonedChain.add(b);
		chain.forEach(consumer);
		return new SimpleBlockchain<T>(clonedChain);
	}

	/* Gets the root hash. */
	public String blockChainHash() {
		int count = chain.size();
		return chain.get(count - 1).GetHash();
	}
}