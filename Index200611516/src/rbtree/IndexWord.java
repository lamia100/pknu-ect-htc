package rbtree;

import java.util.ArrayList;

public class IndexWord implements Comparable<IndexWord> {

	private String word = "";
	private int count;
	private ArrayList<Integer> lines;

	public IndexWord(String word) {
		this.word = word.toLowerCase();
		lines = new ArrayList<Integer>();
		//lines.add(0);
	}

	@Override
	public int compareTo(IndexWord o)
	{
		// TODO Auto-generated method stub
		return word.compareToIgnoreCase(o.word);
	}

	public String getWord()
	{
		return word;
	}
	public String toString()
	{
		return word;
	}

	public void increase()
	{
		count++;
	}

	public int getCount()
	{
		return count;
	}

	public void addLine(int line)
	{
		if (lines.isEmpty()) {
			lines.add(line);
		} else if (lines.get(lines.size() - 1) != line) {
			lines.add(line);
		}
	}

	public ArrayList<Integer> getLine()
	{
		return lines;
	}

}
