package org.salespointframework.util;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class IterablesTests {
	
	// i fucking hate java generics and boxing
	private static List<Integer> list =  Collections.unmodifiableList(Arrays.asList(1,2,3,4,5,6,7,8,9));
	
	@Test
	public void asList() {
		assertEquals(list, Iterables.asList(list));
	}
	
	@Test
	public void asSet() {
		Set<Integer> set = new HashSet<Integer>(list);
		assertEquals(set, Iterables.asSet(list));
	}
	
	@Test
	public void asArray() {
		Integer[] array = list.toArray(new Integer[0]);
		assertTrue(Arrays.equals(array, Iterables.asArray(list)));
	}
	
	@Test
	public void isEmpty() {
		assertFalse(Iterables.isEmpty(list));
	}
	
	@Test 
	public void empty() {

	}
	
	@Test
	public void size() {
		assertEquals(9, Iterables.size(list));
	}
	
	@Test
	public void first() {
		//assertEquals(1, Iterables.first(list));
	}
	
	public void ofIterable() {
		Iterables.of((Iterable<Integer>)null);
	}
	
	@Test
	public void ofArray() {
		
		Iterable<Integer> iterable = Iterables.of((Integer[])list.toArray());
		
		int c = 0;
		for(Integer i : iterable ) {
			assertEquals(list.get(c), i);
			c++;
		}
	}
}

