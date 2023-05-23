package org.memoryextension.nngm.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;


class SyllableTests {

	@Test
	void count() {
		assertEquals(4, Syllable.count("calculator"));
		assertEquals(4, Syllable.count("wonderfully"));
		assertEquals(1, Syllable.count("wrong"));
	}

}
