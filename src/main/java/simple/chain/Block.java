package simple.chain;

import java.util.Calendar;

import com.google.gson.Gson;

import helpers.SHA256;

public class Block<T> {
	public Calendar timeStamp;

	public int index;

	public T transactions;

	public String previousHash;

	public Gson parser = new Gson();

	public String GetHash() {
		String serializedData = parser.toJson(transactions);

		String hash = SHA256.generateHash(timeStamp.toString() + index + serializedData + previousHash);

		return hash;
	}

	public Block<T> Clone() {
		// Object serialized then rehydrated into a new instance of an object so
		// memory conflicts don't happen
		// There are more efficent ways but this is the most reaadable
		String serializedData = parser.toJson(this);
		Block<T> rehydratedObject = parser.fromJson(serializedData, Block.class);

		return rehydratedObject;
	}
}
