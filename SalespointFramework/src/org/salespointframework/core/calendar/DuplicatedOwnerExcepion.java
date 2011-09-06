package org.salespointframework.core.calendar;

public class DuplicatedOwnerExcepion extends IllegalArgumentException {
    private static final long serialVersionUID = 8587021776875949931L;

    public DuplicatedOwnerExcepion(String calendarEntry) {
        super("Entry \""+calendarEntry+"\" has already an owner and there can only be one!");
    }
}
