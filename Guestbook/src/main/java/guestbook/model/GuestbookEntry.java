package guestbook.model;

import java.util.Date;

public class GuestbookEntry implements Comparable<GuestbookEntry> {
	
	private final String name, text;
	private final Date date;
	private final int id;

	GuestbookEntry(String name, String text, Date date, int id) {
		this.name = name;
		this.text = text;
		this.date = date;
		this.id = id;

	}

	public String getName() {
		return name;
	}
	
	public int getId() {
		return id;
	}

	public Date getDate() {
		return date;
	}

	public String getText() {
		return text;
	}
	
	@Override
	public int compareTo(GuestbookEntry other) {
		return Integer.valueOf(this.id).compareTo(other.getId());
	}
}
