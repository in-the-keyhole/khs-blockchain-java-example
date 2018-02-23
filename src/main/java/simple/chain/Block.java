package simple.chain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.google.gson.Gson;

import helpers.SHA256;

public class Block<T extends Tx> {
	public long timeStamp;
	private int index;
	private List<T> transactions = new ArrayList<T>();
	private String previousHash;
	private String merkleRoot;
	
	// caches Transaction SHA256 hashes
    public Map<String,T> map = new HashMap<String,T>();
	public Gson parser = new Gson();
	public Block<T> add(T tx) {
		transactions.add(tx);
		map.put(tx.hash(), tx);
		computeMerkleRoot();
		return this;
	}

	public String getHash() {
		String serializedData = parser.toJson(transactions);
		String hash = SHA256.generateHash(timeStamp + index + merkleRoot + serializedData + previousHash);
		return hash;
	}

	public Block<T> Clone() {
		// Object serialized then rehydrated into a new instance of an object so
		// memory conflicts don't happen
		// There are more efficent ways but this is the most reaadable
		Block<T> clone = new Block();
		clone.setIndex(this.getIndex());
		clone.setPreviousHash(this.getPreviousHash());
		clone.setMerkleRoot(this.getMerkleRoot());
		clone.setTimeStamp(this.getTimeStamp());
		//clone.setTransactions(this.getTransactions());
		
		List<T> clonedtx = new ArrayList<T>();
		Consumer<T> consumer = (t) -> clonedtx.add(t);
		this.getTransactions().forEach(consumer);
	    clone.setTransactions(clonedtx);
		
		return clone;
	}

	public boolean isTransactionValid(T tx) {
		
		T hash = map.get(tx.hash());
		return hash != null;
	}
	
	public void computeMerkleRoot() {
		
		List<String> treeList = merkleTree();
		// Last element is the merkle root hash if transactions
		setMerkleRoot(treeList.get(treeList.size()-1) );
		
	}
	
	/*
	    This method was adapted from the https://github.com/bitcoinj/bitcoinj project
	    
	    The Merkle root is based on a tree of hashes calculated from the
	    transactions:
		
			 root
			 / \
			 A B
			 / \ / \
		   t1 t2 t3 t4
		
		The tree is represented as a list: t1,t2,t3,t4,A,B,root where each
		entry is a hash.
	
		The hashing algorithm is SHA-256. The leaves are a hash of the
		serialized contents of the transaction.
	    The interior nodes are hashes of the concatenation of the two child
	    hashes.
		
		This structure allows the creation of proof that a transaction was
		included into a block without having to
		provide the full block contents. Instead, you can provide only a
		Merkle branch. For example to prove tx2 was
		in a block you can just provide tx2, the hash(tx1) and A. Now the
		other party has everything they need to
		derive the root, which can be checked against the block header. These
		proofs aren't used right now but
		will be helpful later when we want to download partial block
		contents.
	
		Note that if the number of transactions is not even the last tx is
		repeated to make it so (see
		tx3 above). A tree with 5 transactions would look like this:
		
		    root
		 	/ \
		    1 5
		   / \ / \
		   2 3 4 4
		   / \ / \ / \
		  t1 t2 t3 t4 t5 t5
	
	*/
	public List<String> merkleTree() {		
		ArrayList<String> tree = new ArrayList<>();
		// Start by adding all the hashes of the transactions as leaves of the
		// tree.
		for (T t : transactions) {
			tree.add(t.hash());
		}
		int levelOffset = 0; // Offset in the list where the currently processed
								// level starts.
		// Step through each level, stopping when we reach the root (levelSize
		// == 1).
		for (int levelSize = transactions.size(); levelSize > 1; levelSize = (levelSize + 1) / 2) {
			// For each pair of nodes on that level:
			for (int left = 0; left < levelSize; left += 2) {
				// The right hand node can be the same as the left hand, in the
				// case where we don't have enough
				// transactions.
				int right = Math.min(left + 1, levelSize - 1);
				String tleft = tree.get(levelOffset + left);
				String tright = tree.get(levelOffset + right);
				tree.add(SHA256.generateHash(tleft + tright));
			}
			// Move to the next level.
			levelOffset += levelSize;
		}
		return tree;
	}

	
	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getPreviousHash() {
		return previousHash;
	}

	public void setPreviousHash(String previousHash) {
		this.previousHash = previousHash;
	}

	public List<T> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<T> transactions) {
		this.transactions = transactions;
	}
	
	public String getMerkleRoot() {
		return merkleRoot;
	}

	public void setMerkleRoot(String merkleRoot) {
		this.merkleRoot = merkleRoot;
	}
	
	
}
