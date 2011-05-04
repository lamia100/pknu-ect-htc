package rbtree;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class NoisyWords {
	ArrayList<String> words;
	
	String path = "";
	
	public NoisyWords() {
		// TODO Auto-generated constructor stub
		words = new ArrayList<String>();
		initialize();
	}
	
	public NoisyWords(String path) {
		words = new ArrayList<String>();
		this.path = path;
		initialize();
	}
	
	/**
	 * 
	 * @param word 반드시 소문자일것
	 * @return true | false
	 */
	public boolean isNoisyWord(String word) {
		
		int index = Collections.binarySearch(words, word);
		return index >= 0;
	}
	
	private void initialize() {
		try {
			Scanner input = new Scanner(new File(path));
			input.useDelimiter("[^a-zA-Z]");
			while (input.hasNext()){
				words.add(input.next().toLowerCase());
			}
			//System.out.println(input.next());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Collections.sort(words);
	}
	/*
	public static void main(String[] args) {
		new NoisyWords("noisy.txt");
	}
	*/
}
