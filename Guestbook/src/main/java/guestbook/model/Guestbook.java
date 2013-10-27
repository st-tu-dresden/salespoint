package guestbook.model;

import java.util.*;

import org.springframework.stereotype.Component;

@Component
public class Guestbook {
	
	// Entry.id -> Entry
	private final Map<Integer, GuestbookEntry> entries = new TreeMap<Integer, GuestbookEntry>();
	
	public Guestbook() {
		addEntry("H4xx0r", "first!!!");
		addEntry("Arni", "Hasta la vista, baby");
		addEntry("Duke Nukem", "It's time to kick ass and chew bubble gum. And I'm all out of gum.");
		addEntry("Gump1337", "Mama always said life was like a box of chocolates. You never know what you're gonna get.");
	}
	
	public GuestbookEntry addEntry(String name, String text) {
		GuestbookEntry entry = new GuestbookEntry(name, text, new Date(), entries.size());
		entries.put(entry.getId(), entry);
		return entry;
	}
	
	public boolean removeEntry(int id) {
		if(!entries.containsKey(id)) return false;
		entries.remove(id);
		return true;
	}
	
	public Iterable<GuestbookEntry> getEntries() {
		return entries.values();
	}
}
