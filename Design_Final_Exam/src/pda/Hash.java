package pda;

import java.util.Iterator;
import org.apache.log4j.*;

public class Hash implements Iterable<Integer> {
	private static Logger logger;
	
	private MyLinkedList<Pair> hashTable[];
	private int count;
	
	@SuppressWarnings("unchecked")
	public Hash() {
		hashTable = new MyLinkedList[100];
		count = 0;
	}
	
	public static void initLogger() {
		logger = Logger.getLogger(Hash.class);
	}

	public void insert(int key, String value) {
		int hashKey = key % 100;
		
		if (hashTable[hashKey] == null) {
			hashTable[hashKey] = new MyLinkedList<Pair>();
		}
		
		Pair result = null;
		for (Pair target : hashTable[hashKey]) {
			if (target.key == key) {
				result = target;
				break;
			}
		}
		
		if (result == null) {
			hashTable[hashKey].add(new Pair(key, value));
			count++;
		}
		else {
			result.value = value;
		}
		
		logger.debug("insert ¸Þ½îµå");
	}

	public String remove(int key) {
		int hashKey = key % 100;
		
		if (hashTable[hashKey] == null) {
			return null;
		}
		
		Pair result = null;
		for (Pair target : hashTable[hashKey]) {
			if (target.key == key) {
				result = target;
				break;
			}
		}
		
		if (result == null) {
			return null;
		}
		
		hashTable[hashKey].remove(result);
		count--;
		
		if (hashTable[hashKey].isEmpty()) {
			hashTable[hashKey] = null;
		}
		
		logger.debug("remove ¸Þ½îµå");
		
		return result.value;
	}
	
	public String lookup(int key) {
		int hashKey = key % 100;
		
		if (hashTable[hashKey] == null) {
			return null;
		}
		
		String result = null;
		for (Pair target : hashTable[hashKey]) {
			if (target.key == key) {
				result = target.value;
				break;
			}
		}
		
		logger.debug("lookup ¸Þ½îµå");
		
		return result;
	}

	public int size() {
		return count;
	}
	
	@Override
	public Iterator<Integer> iterator() {
		MyLinkedList<Integer> pairList = new MyLinkedList<Integer>();
		
		for (int i = 0; i < 100; i++) {
			if (hashTable[i] != null) {
				for (Pair target : hashTable[i]) {
					pairList.add(target.key);
				}
			}
		}
		
		return pairList.iterator();
	}
}