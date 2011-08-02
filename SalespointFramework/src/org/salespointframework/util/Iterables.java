package org.salespointframework.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


//TODO
// SCHEISS Collection<T> die Idioten von Sun hätten mal ihre APis um Iterable<T> erweitern sollen, alles muss man selber machen....
// http://google-collections.googlecode.com/svn/trunk/javadoc/com/google/common/collect/Iterables.html
// ob es sich lohnt die ganze lib reinzuziehen?

public class Iterables {

	private Iterables() {}
	
	public static <T> List<T> toList(Iterable<T> iterable) {
		List<T> temp = new ArrayList<T>();
		for(T item : iterable) {
			temp.add(item);
		}
		return temp;
	}
	
	public static <T> Set<T> toSet(Iterable<T> iterable) {
		Set<T> temp = new HashSet<T>();
		for(T item : iterable) {
			temp.add(item);
		}
		return temp;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T[] toArray(Iterable<T> iterable) {
		return (T[]) toList(iterable).toArray();
	}
	
	public static <T> boolean isEmpty(Iterable<T> iterable) {
		return !iterable.iterator().hasNext();
	}
	
	
}
