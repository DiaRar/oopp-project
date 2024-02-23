/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package commons;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class QuoteTest {

	private static final Participant SOME_PARTICIPANT = new Participant("John", "Doe", "j.d@email.com");

	@Test
	public void checkConstructor() {
		var q = new Quote(SOME_PARTICIPANT, "q");
		assertEquals(SOME_PARTICIPANT, q.participant);
		assertEquals("q", q.quote);
	}

	@Test
	public void equalsHashCode() {
		var a = new Quote(new Participant("John", "Doe", "j.d@email.com"), "c");
		var b = new Quote(new Participant("John", "Doe", "j.d@email.com"), "c");
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
	}

	@Test
	public void notEqualsHashCode() {
		var a = new Quote(new Participant("John", "Doe", "j.d@email.com"), "c");
		var b = new Quote(new Participant("John", "Doe", "j.d@email.com"), "d");
		assertNotEquals(a, b);
		assertNotEquals(a.hashCode(), b.hashCode());
	}

	@Test
	public void hasToString() {
		var actual = new Quote(new Participant("John", "Doe", "j.d@email.com"), "q").toString();
		assertTrue(actual.contains("John"));
		assertTrue(actual.contains("Doe"));
		assertTrue(actual.contains("j.d@email.com"));
		assertTrue(actual.contains("q"));
	}
}