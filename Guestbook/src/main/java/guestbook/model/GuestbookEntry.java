package guestbook.model;

import java.util.Date;

//All work and no play makes ‎(｡◕‿◕｡) a dull boy
//All work and no play makes ‎(｡◕‿◕｡) a dull boy
//All work and no play makes ‎(｡◕‿◕｡) a dull boy
//All work and no play makes ‎(｡◕‿◕｡) a dull boy
//All work and no play makes ‎(｡◕‿◕｡) a dull boy
//All work and no play makes ‎(｡◕‿◕｡) a dull boy
//All work and no play makes ‎(｡◕‿◕｡) a dull boy
//All work and no play makes ‎(｡◕‿◕｡) a dull boy
//All work and no play makes ‎(｡◕‿◕｡) a dull boy
//All work and no play makes ‎(｡◕‿◕｡) a dull boy
//All work and no play makes ‎(｡◕‿◕｡) a dull boy
//All work and no play makes ‎(｡◕‿◕｡) a dull boy

public class GuestbookEntry {
	
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
}
