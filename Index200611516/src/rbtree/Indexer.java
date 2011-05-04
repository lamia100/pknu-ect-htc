package rbtree;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Indexer {
	
	private NoisyWords noisy = null;;
	private RedBlackTree<IndexWord> words = null;
	private File file = null;
	
	public Indexer() {
		initialize();
	}
	
	private void initialize() {
		words = new RedBlackTree<IndexWord>();
		noisy = new NoisyWords("noisy.txt");
		System.out.println(noisy.isNoisyWord("b"));
	}
	
	public void setTextFile(File file) {
		this.file = file;
	}
	
	public void start() {
		int lineCounter = 0;
		try {
			Scanner input = new Scanner(file);
			Pattern pattern = Pattern.compile("[^a-zA-Z]");
			while (input.hasNextLine()) {
				Scanner line = new Scanner(input.nextLine());
				line.useDelimiter(pattern);
				lineCounter++;
				while (line.hasNext()) {
					String word = line.next();
					word=word.toLowerCase();
					//System.out.println(word);
					if (noisy.isNoisyWord(word)) {
						continue;
					} else {
						IndexWord iword = new IndexWord(word);
						iword = words.add(iword);
						iword.addLine(lineCounter);
						iword.increase();
					}
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println();
	}
	
	public void removeUnnecessaryNodes() {
		ArrayList<IndexWord> list = words.getElementList();
		for (IndexWord word : list) {
			if (word.getCount() < 6) {
				words.remove(word);
				System.out.println(word);
			}
		}
	}
	
	public ArrayList<IndexWord> getList() {
		return words.getElementList();
	}
	
	public void search(String word) {
		word=word.toLowerCase();
	}
/*
	public static void main(String[] args) {
		
		Indexer index = new Indexer();
		index.initialize();
		index.setTextFile(new File("pg27827.txt"));
		index.start();
		
		ArrayList<IndexWord> list = index.getList();
		PrintWriter out = null;
		try {
			out = new PrintWriter(new File("result1.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		int count=0;
		for (IndexWord i : list) {
			out.print(i + " : ");
			for (int l : i.getLine()) {
				out.print(l + " ");
			}
			out.println("count : " + i.getCount());
			count++;
		}
		
		System.out.println(count);
		
		count=0;
		index.removeUnnecessaryNodes();
		list = index.getList();
		out.flush();
		try {
			out = new PrintWriter(new File("result2.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		
		for (IndexWord i : list) {
			out.print(i + " : ");
			for (int l : i.getLine()) {
				out.print(l + " ");
			}
			out.println("count : " + i.getCount());
			count++;
		}
		out.flush();
		System.out.println(count);
	}
*/	
}
