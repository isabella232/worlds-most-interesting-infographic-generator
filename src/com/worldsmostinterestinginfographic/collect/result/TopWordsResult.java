package com.worldsmostinterestinginfographic.collect.result;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TopWordsResult {
	private List<WordCountPair> topWords;
	
	public TopWordsResult(Map<String, Integer> wordMap) {
		topWords = new ArrayList<WordCountPair>();
		for (Map.Entry<String, Integer> entry : wordMap.entrySet()) {

			// System.out.println(entry.getKey() + ": " + entry.getValue());
			WordCountPair wordCountPair = new WordCountPair(entry.getKey(), entry.getValue());
			
			boolean inserted = false;
			for (int i = 0; i < topWords.size(); i++) {
				
				// If it's greater than the current one, insert ahead of it
				if (wordCountPair.compareTo(topWords.get(i)) >= 0) {
					topWords.add(i, wordCountPair);
					inserted = true;
					break;
				}
			}
			
			// If it wasn't less than any of the things in the list already, put it at the end
			if (!inserted) {
				topWords.add(topWords.size(), wordCountPair);
			}
		}
	}

	public List<WordCountPair> getTopWords() {
		return topWords;
	}
	
	/**
	 * Return an ordered list of <code>WordCountPair</code>s where words at the beginning
	 * of the list are larger than the next.  The comparison is made with both the word count
	 * and the length of the word.  First, word count is compared.  If they are equal, then
	 * word length wins.
	 * 
	 * Can specify a maximum number of top words to return.  The size of the list will be equal
	 * or less than the limit given.
	 * 
	 * @param limit	The max number of top words to return.
	 * @return An ordered list of <code>WordCountPair</code>s with the words at the front of the 
	 * 			list being "larger" than the words at the end.
	 * 
	 * @see com.worldsmostinterestinginfographic.collect.result.WordCountPair#compareTo(WordCountPair)
	 */
	public List<WordCountPair> getTopWords(int limit) {
		return topWords.subList(0, limit);
	}
}
