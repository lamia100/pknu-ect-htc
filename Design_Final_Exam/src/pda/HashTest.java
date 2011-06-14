package pda;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.Arrays;
import java.util.Random;

/**
 * Map 클래스를 테스트하기 위한 JUnit 테스트 클래스
 * 
 * @author HJSONG
 *
 */
public class HashTest {

	private class Pair {
		int key;
		String value;
		Pair(int k, String v) {
			key = k;
			value = v;
		}
	}
	
	@BeforeClass
	public static void setupLogger()
	{
		Hash.initLogger();
	}

	@Test
	public void testDefaultConstructor()
	{
		Hash map = new Hash();
		assertEquals(map.size(), 0);
	}
	
	@Test
	public void testInsert5()
	{
		Hash map = new Hash();

		populateEntries5(map);
		
		assertEquals(map.size(), 5);
		
		assertEquals(map.lookup(1), "One");
		assertEquals(map.lookup(2), "Two");
		assertEquals(map.lookup(3), "Three");		
		assertEquals(map.lookup(4), "Four");
		assertEquals(map.lookup(5), "Five");		
		
		assertNull(map.lookup(0));
	}
	
	@Test
	public void testDuplicates()
	{
		Hash map = new Hash();

		populateEntries5(map);
		
		map.insert(3, "THREE");
		map.insert(5, "five");
		
		assertEquals(map.lookup(3), "THREE");
		assertEquals(map.lookup(5), "five");
	}
	
	@Test
	public void testRemove5()
	{
		Hash map = new Hash();

		Pair[] pairs = populateEntries5(map);
		
		for (int i=0; i<5; i++)
		{
			assertEquals(map.remove(pairs[i].key), pairs[i].value);
			assertEquals(map.size(), 4-i);
			assertNull(map.lookup(pairs[i].key), null);
		}
		
		assertEquals(map.size(), 0);
	}	
	
	@Test 
	public void testRandom()
	{
		int numData = 1000;
		Pair[] pairs = getRandPairs(numData, 1, 100000);
		
		Hash map = new Hash();
		
		for (int i=0; i<numData; i++) 
		{
			map.insert(pairs[i].key, pairs[i].value);
		}
		
		// Check if inserted key and values are retrieved properly
		for (int i=0; i<numData; i++)		
		{
			assertEquals(map.lookup(pairs[i].key), pairs[i].value);
		}	
		
		// Remove randomly 100 key-value pairs
		Random rand = new Random(System.currentTimeMillis());
		int expSize = numData;
		for (int i=0; i<100; i++)		
		{
			Pair p = pairs[rand.nextInt(numData)];
			
			if (p.value != null) expSize--; 
			
			assertEquals(map.remove(p.key), p.value);
			assertEquals(map.size(), expSize );
			assertNull(map.lookup(p.key));
			p.value = null; // For duplicate removal			
		}			
	}
	
	@Test
	public void testIterator()
	{
		int numData = 10000;
		Pair[] pairs = getRandPairs(numData, 1, 100000);
		
		int i;
		int[] pkeys = new int[numData];
		for (i=0; i<numData; i++)
			pkeys[i] = pairs[i].key;
		
		Hash map = new Hash();
		
		for (i=0; i<numData; i++) 
		{
			map.insert(pairs[i].key, pairs[i].value);
		}
		
		int[] keys = new int[numData];
	
		
		i=0;
		for (int key : map) {
			keys[i++] = key;
		}
		
		Arrays.sort(pkeys);
		Arrays.sort(keys);
		
		assertTrue(Arrays.equals(pkeys, keys));
	}
	
	private Pair[] populateEntries5(Hash m)
	{
		Pair[] pairs = new Pair[5];
		
		pairs[0] = new Pair(1, "One");
		pairs[1] = new Pair(2, "Two");
		pairs[2] = new Pair(3, "Three");
		pairs[3] = new Pair(4, "Four");
		pairs[4] = new Pair(5, "Five");
		
		for (int i=0; i<5; i++)
		{
			m.insert(pairs[i].key, pairs[i].value);
		}
		
		return pairs;
	}
	
	private Pair[] getRandPairs(final int num, final int begin, final int end)
	{
		assert (num > 0) && (end >= begin) && (num <= end - begin + 1) : "Invalid arguments";
		
		class RandomInteger {
			Pair[] arr;
			Random rand;
			
			Pair[] generate() {
				arr = new Pair[num];
				int width = end - begin + 1;

				rand = new Random(System.currentTimeMillis());

				int cnt=0;
				while (cnt < num)
				{
					int intNo = begin + rand.nextInt(width);
					
					if (isDup(cnt, intNo)) continue;
					
					arr[cnt++] = new Pair(intNo, getRandStr());
				}
				
				return arr;
			}
			
			boolean isDup(int end, int intNo) {
				for (int i=0; i<end; i++) 
				{
					if (arr[i].key == intNo) return true;
				}
				
				return false;
			}
			
			String getRandStr()
			{
				String[] values = {
						"One", "Two", "Three", "Four", "Five", 
						"Six", "Seven", "Eight", "Nine", "Ten"
				};
				

				return values[rand.nextInt(10)];
			}
		}		
		
		return new RandomInteger().generate();
	}	
}
