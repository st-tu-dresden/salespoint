package guestbook.model;

import java.util.Date;

public class GuestbookEntry {
	private static int globalId = 0;
	
	private final String name, text;
	private final Date date;
	private final int id;

	public GuestbookEntry(String name, String text) {
		this.name = name;
		this.text = text;
		this.date = new Date();
		this.id = globalId;
		
		globalId++; // not safe (threading), who cares
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
}
