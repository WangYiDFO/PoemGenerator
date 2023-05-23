package org.memoryextension.nngm.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.Random;
import java.util.Vector;


// strongly inspired by https://gist.github.com/veryphatic/3190969
public class Markov {

	private Hashtable<String, Vector<String>> markovChain;
	private Random rnd;
	
	private void init() {
		markovChain = new Hashtable<String, Vector<String>>();
    // Create the first two entries (_start, _end)
		markovChain.put("_start", new Vector<String>());
		markovChain.put("_end", new Vector<String>());
		
	}

	public Markov() {
		rnd = new Random();
		init();
	}

    public Markov(long seed) {
		rnd = new Random(seed);
		init();
    }

    public Hashtable<String, Vector<String>> getMarkovChain() { return markovChain; }

	public void settMarkovChain(Hashtable<String, Vector<String>> markovChain) {
		this.markovChain = markovChain;
	}

  
  public void addWords(String wordsSequence) {
		String[] words = wordsSequence.split(" ");
				
		// Loop through each word, check if it's already added
		// if its added, then get the suffix vector and add the word
		// if it hasn't been added then add the word to the list
		// if its the first or last word then select the _start / _end key
		
		for (int i=0; i<words.length; i++) {
						
			// Add the start and end words to their own
			if (i == 0) {
				Vector<String> startWords = markovChain.get("_start");
				startWords.add(words[0]);
				
				Vector<String> suffix = markovChain.get(words[0]);
				if (suffix == null) {
					suffix = new Vector<String>();
					suffix.add(words[1]);
					markovChain.put(words[0], suffix);
				}
				
			} else if (i == words.length-1) {
				Vector<String> endWords = markovChain.get("_end");
				endWords.add(words[i]);
				
			} else {	
				Vector<String> suffix = markovChain.get(words[i]);
				if (suffix == null) {
					suffix = new Vector<String>();
					suffix.add(words[i+1]);
					markovChain.put(words[i], suffix);
				} else {
					suffix.add(words[i+1]);
					markovChain.put(words[i], suffix);
				}
			}
		}		
	}
  
  public Vector<String> generateSentence() {
		
		// Vector to hold the phrase
		Vector<String> newPhrase = new Vector<String>();
		
		// String for the next word
		String nextWord = "";
				
		// Select the first word
		Vector<String> startWords = markovChain.get("_start");
		int startWordsLen = startWords.size();
		nextWord = startWords.get(rnd.nextInt(startWordsLen));
		newPhrase.add(nextWord);
		
		// Keep looping through the words until we've reached the end
		while ( (nextWord.charAt(nextWord.length()-1) != '.') && (nextWord.charAt(nextWord.length()-1) != '?') && (nextWord.charAt(nextWord.length()-1) != '!') ) {
			System.out.println(nextWord);
			Vector<String> wordSelection = markovChain.get(nextWord);
			if(wordSelection==null) break;
			int wordSelectionLen = wordSelection.size();
			nextWord = wordSelection.get(rnd.nextInt(wordSelectionLen));
			newPhrase.add(nextWord);
		}
		
		return newPhrase;	
	}

}