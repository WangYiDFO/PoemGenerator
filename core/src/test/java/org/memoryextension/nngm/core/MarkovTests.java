package org.memoryextension.nngm.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import java.util.Vector;

class MarkovTests {

	@Test
	void emptyGenerator() {
	  Markov m = new Markov(774592451); // fix random so tests are repeatable
	  
	  assertEquals(2, m.getMarkovChain().size());
	  
	  String firstOne ="Once upon a midnight dreary, while I pondered, weak and weary, Over many a quaint and curious volume of forgotten lore.";
	  m.addWords(firstOne);
	  assertEquals(firstOne.split(" ").length-1, m.getMarkovChain().size());
	  
	  m.addWords("If you can fill the unforgiving minute With sixty seconds’ worth of distance run Yours is the Earth and everything that’s in it, And—which is more—you’ll be a Man, my son!");
	  m.addWords("If you can fill the unforgiving minute With sixty seconds’ worth of distance run Yours is the Earth and everything that’s in it, And—which is more—you’ll be a Man, my son!");
	  m.addWords("He has a turtle and she has a shell. She’s the principal and he’s the janitor. She’s a widowed social worker looking for a father figure and he’s an elderly vagrant. He’s unprincipled and she’s … principled.");
	  
	  
		
	  Vector<String> result = m.generateSentence();
	  assertEquals(4,result.size());
	  assertEquals("He",result.get(0));
	  assertEquals("has",result.get(1));
	  assertEquals("a",result.get(2));
	  assertEquals("shell.",result.get(3));
	}

}
