package com.worldsmostinterestinginfographic.collect.result;

public class WordCountPair implements Comparable<WordCountPair>  {
	private String word;
	private int count;
	
	public WordCountPair (String word, int count) {
		this.word = word;
		this.count = count;
	}
	
	@Override
	public int compareTo(WordCountPair o) {
		
		// Compare count
		if (count < o.count) {
			return -1;
		}
		if (count > o.count){
			return 1;
		}
		
		// If they are equal in count, compare length of word
		if (word.length() < o.word.length()) {
			return -1;
		}
		if (word.length() > o.word.length()) {
			return 1;
		}
		
		
		// They are equal
		return 0;
	}

	public String getWord() {
		return word;
	}

	public int getCount() {
		return count;
	}
}
