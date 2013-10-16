package org.salespointframework.calendar;

import static org.junit.Assert.*;
import static org.junit.Assume.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;

import org.junit.Test;
import org.salespointframework.AbstractIntegrationTests;
import org.salespointframework.core.calendar.Calendar;
import org.salespointframework.core.calendar.CalendarEntry;
import org.salespointframework.core.user.User;
import org.salespointframework.core.user.UserIdentifier;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("javadoc")
public class CalendarTest extends AbstractIntegrationTests {
	private static final DateTime basicDateTime = new DateTime();

	private User user;
	private User notUser;
	@Autowired
	private Calendar calendar;
	private CalendarEntry entry;
	private CalendarEntry notEntry;

	@Before
	public void before() {

		user = new User(new UserIdentifier("user"), "test");
		entry = new CalendarEntry(user.getIdentifier(), "entry", basicDateTime,
				basicDateTime.plusMinutes(30));
		calendar.add(entry);

		notUser = new User(new UserIdentifier("notUser"), "test");
		notEntry = new CalendarEntry(notUser.getIdentifier(), "notEntry",
				basicDateTime, basicDateTime.plusMinutes(30));
		calendar.add(notEntry);
	}

	@Test
	public void deleteEntry() {
		CalendarEntry deleteEntry = new CalendarEntry(new UserIdentifier(),
				"deleteEntry", basicDateTime, basicDateTime.plusMinutes(10));
		calendar.add(deleteEntry);

		assumeThat(
				calendar.get(CalendarEntry.class, deleteEntry.getIdentifier()),
				is(deleteEntry));

		calendar.remove(deleteEntry.getIdentifier());

		assertNull(calendar.get(CalendarEntry.class,
				deleteEntry.getIdentifier()));
	}

	@Test
	public void getEntriesByTitle() {
		Iterable<CalendarEntry> actual = calendar.find(CalendarEntry.class,
				entry.getTitle());
		assertThat(actual, hasItem(entry));
		assertThat(actual, not(hasItem(notEntry)));
	}

	@Test
	public void getEntriesByOwner() {
		Iterable<CalendarEntry> actual = calendar.find(CalendarEntry.class,
				user.getIdentifier());
		assertThat(actual, hasItem(entry));
		assertThat(actual, not(hasItem(notEntry)));
	}

	@Test
	public void filterEntries() {
		CalendarEntry has = new CalendarEntry(new UserIdentifier(), "early",
				new DateTime().minusHours(1), new DateTime().plusHours(1));
		CalendarEntry hasnot = new CalendarEntry(new UserIdentifier(), "late",
				new DateTime().plusHours(1), new DateTime().plusHours(2));
		calendar.add(has);
		calendar.add(hasnot);

		List<CalendarEntry> actual = new ArrayList<CalendarEntry>();
		for (CalendarEntry entry : calendar.find(CalendarEntry.class)) {
			if (entry.getStart().isBefore(basicDateTime.plusMinutes(10)))
				actual.add(entry);
		}

		assertThat(actual, hasItem(has));
		assertThat(actual, not(hasItem(hasnot)));
	}
}