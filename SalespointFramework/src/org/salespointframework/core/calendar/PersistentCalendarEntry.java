package org.salespointframework.core.calendar;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.AttributeOverride;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.salespointframework.core.users.UserIdentifier;
import org.salespointframework.util.ArgumentNullException;
import org.salespointframework.util.Iterables;
import org.salespointframework.util.Objects;

/**
 * 
 * This is an abstract representation of an calendar entry which provides basic
 * functionality.
 * 
 * @author Stanley Förster
 * 
 */
// TODO sort getters/setters
// TODO javadoc!!! rechtschreibung!!!
@Entity
public final class PersistentCalendarEntry implements CalendarEntry {

    /**
     * The unique identifier for this entry.
     */
    @EmbeddedId
    @AttributeOverride(name = "id", column = @Column(name = "ENTRY_ID"))
    private CalendarEntryIdentifier                               calendarEntryIdentifier;

    /**
     * This map stores all capabilities, a user has in relation to this calendar
     * entry.
     */
    @ElementCollection(targetClass = EnumSet.class)
    @CollectionTable(joinColumns = @JoinColumn(referencedColumnName = "ENTRY_ID", name = "ENTRY_ID"))
    @AttributeOverride(name = "key.id", column = @Column(name = "USER_ID"))
    @Column(name = "ENUM")
    private Map<UserIdentifier, EnumSet<CalendarEntryCapability>> capabilities = new HashMap<UserIdentifier, EnumSet<CalendarEntryCapability>>();

    /**
     * Specifies start and end date of this entry, where the end is exclusive.
     * 
     * @see Interval
     */
    private Interval                                              duration;

    /**
     * The unique identifier of the user who is the owner of this entry. It will
     * be specified when the entry is created, so the creator will become the
     * owner of the entry. The owner canno change, once it's defined, it's
     * immutable.
     */
    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "OWNER_ID"))
    private UserIdentifier                                        owner;

    /**
     * Represents the time between two repetitions of this entry. For
     * determining how often an entry should be repeated, see
     * {@link PersistentCalendarEntry#count}. The time defined here is the time
     * between two starts of this entry and not between the end of one
     * appointment and the start of the next repetition. So this timespan also
     * has to be smaller than the duration of this appointment.
     * 
     * @see Period
     */
    private Period                                                period;

    /**
     * Description of this calendar entry.
     */
    protected String                                              description;

    // /**
    // * The end date and time for this entry.
    // */
    // @Temporal(TemporalType.TIMESTAMP)
    // protected Date endTime;

    // /**
    // * Represents how often this entry should be repeated. For determining the
    // * time between two repetitions, see
    // * {@link PersistentCalendarEntry#repeatStep}
    // *
    // */
    // protected int repeatCount;
    //
    // /**
    // * Represents the time in millis between two repetitions of this entry.
    // For
    // * determining how often an entry should be repeated, see
    // * {@link PersistentCalendarEntry#repeatCount} The time defined here is
    // the
    // * time between two starts of this entry and not between the end of one
    // * appointment and the start of the next repetition. So this timespan also
    // * has to be smaller than the duration of this appointment.
    // */
    // protected long repeatStep;

    // /**
    // * The start date and time for this entry.
    // */
    // @Temporal(TemporalType.TIMESTAMP)
    // protected Date startTime;

    /**
     * Title of this entry.
     */
    protected String                                              title;

    /**
     * Represents how often this entry should be repeated. For determining the
     * time between two repetitions, see {@link PersistentCalendarEntry#period}
     */
    int                                                           count;

    /**
     * This contructor should not be used. It only exists for persistence
     * reasons.
     */
    @Deprecated
    public PersistentCalendarEntry() {
    }

    /**
     * Creates a new calendar entry with a minimum of information.
     * 
     * @param owner
     *            The id of the user, who created this entry.
     * @param title
     *            The title of this entry.
     * @param start
     *            Start time and date.
     * @param end
     *            End time and date.
     * @throws ArgumentNullException
     *             The {@link ArgumentNullException} will be thrown if one ore
     *             more arguments are <code>null</code>
     */
    public PersistentCalendarEntry(UserIdentifier owner, String title, DateTime start, DateTime end) {
        /* new Interval throws IllegalArgumentE, if end < start */
        this(owner, title, new Interval(start, end));
    }

    /**
     * Creates a new calendar entry with a minimum of information.
     * 
     * @param owner
     *            The id of the user, who created this entry.
     * @param title
     *            The title of this entry.
     * @param duration
     *            The duration ({@link Interval}) of this entry. It contains
     *            start and end date.
     * @throws ArgumentNullException
     *             The {@link ArgumentNullException} will be thrown if one ore
     *             more arguments are <code>null</code>
     * 
     * @see Interval
     */
    public PersistentCalendarEntry(UserIdentifier owner, String title, Interval duration) {
        this(owner, title, duration, "", Period.ZERO, 0);
    }

    /**
     * Creates a new calendar entry with a minimum of information.
     * 
     * @param owner
     *            The id of the user, who created this entry.
     * @param title
     *            The title of this entry.
     * @param duration
     *            The duration ({@link Interval}) of this entry. It contains
     *            start and end date.
     * @param description
     *            The description of this entry.
     * @throws ArgumentNullException
     *             The {@link ArgumentNullException} will be thrown if one ore
     *             more arguments are <code>null</code>
     * 
     * @see Interval
     */
    public PersistentCalendarEntry(UserIdentifier owner, String title, Interval duration, String description) {
        this(owner, title, duration, description, Period.ZERO, 0);
    }

    /**
     * Creates a new cyclic calendar entry that will be repeated after a period.
     * There are parameters that define how often it will be repeated and how
     * much time beteween two repetitions will be.
     * 
     * @param owner
     *            The id of the user, who created this entry.
     * @param title
     *            The title of this entry.
     * @param duration
     *            The duration ({@link Interval}) of this entry. It contains
     *            start and end date.
     * @param description
     *            The description of this entry.
     * @param period
     *            The period of one repetition of this entry. This is the time
     *            between two starts and so it has to be less than the duration.
     * @param count
     *            Times how often the event is repeated. -1 means infinitely.
     * @throws ArgumentNullException
     *             The {@link ArgumentNullException} will be thrown if one ore
     *             more arguments are <code>null</code>
     * 
     * @see Interval
     * @see Period
     */
    public PersistentCalendarEntry(UserIdentifier owner, String title, Interval duration, String description, Period period, int count) {
        this.owner = Objects.requireNonNull(owner, "owner");
        this.title = Objects.requireNonNull(title, "title");
        this.duration = Objects.requireNonNull(duration, "duration");
        this.description = Objects.requireNonNull(description, "description");
        this.period = Objects.requireNonNull(period, "period");
        this.count = count;

        this.calendarEntryIdentifier = new CalendarEntryIdentifier();

        capabilities.put(owner, EnumSet.allOf(CalendarEntryCapability.class));

        if ((count != 0) && duration.getStart().plus(period).isBefore(duration.getEnd()))
            throw new IllegalArgumentException("The time between two repetitions can not be less than the duration of a calendar entry!");
    }

    /**
     * Adds the capability to a specific user. Parameters must not be
     * <code>null</code>.
     * 
     * @param user
     *            The user identification of the user who should get the new
     *            capability.
     * @param capability
     *            The new capability for the given user.
     */
    @Override
    public void addCapability(UserIdentifier user, CalendarEntryCapability capability) {
        Objects.requireNonNull(user, "user");
        Objects.requireNonNull(capabilities, "capability");

        if (capabilities.containsKey(user))
            capabilities.get(user).add(capability);
        else
            capabilities.put(user, EnumSet.of(capability));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object other) {
        if (other == null)
            return false;
        if (other == this)
            return true;
        if (!(other instanceof PersistentCalendarEntry))
            return false;
        return this.equals((PersistentCalendarEntry) other);
    }

    /**
     * Determines if the given {@link PersistentCalendarEntry} is equal to this
     * one or not. Two calendar entries are equal to each other, if their
     * identifiers are equal.
     * 
     * @param other
     *            The entry this one should be compared with.
     * @return <code>true</code> if and only if the identifier of this calendar
     *         entry equals the identifier of the entry that is given as
     *         parameter. <code>false</code> otherwise.
     */
    public boolean equals(PersistentCalendarEntry other) {
        if (other == null)
            return false;
        if (other == this)
            return true;
        return this.calendarEntryIdentifier.equals(other.calendarEntryIdentifier);
    }

    /**
     * Returns the ID of this entry. The ID is the entry's primary key,
     * generated automatically when the entry has been created.
     * 
     * @return The ID of this entry.
     */
    @Override
    public CalendarEntryIdentifier getCalendarEntryIdentifier() {
        return calendarEntryIdentifier;
    }

    /**
     * Returns all capabilities the given user has for this entry. The user
     * identification must not be <code>null</code>
     * 
     * @param user
     *            the user identification of the user whose capabilities should
     *            be returned.
     * 
     * @return An {@link Iterable} which contains all capabilities of this user
     *         or <code>null</code> if the user has no capabilities for this
     *         entry.
     */
    @Override
    public Iterable<CalendarEntryCapability> getCapabilitiesByUser(UserIdentifier user) {
        Objects.requireNonNull(user, "user");

        return Iterables.from(capabilities.get(user));
    }

    /**
     * Returns the number how often this appointment will be repeated, where -1
     * means infinitely.
     * 
     * @return The number how often this appointment will be repeated.
     */
    public long getCount() {
        return count;
    }

    /**
     * Returns the description of this entry. The description may be empty but
     * never <code>null</code>.
     * 
     * @return The description of this entry
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * Returns the end date of this entry. The end date is never
     * <code>null</code>, never before the start date and it's exclusive.
     * 
     * @return The end of this event.
     * 
     * @see Interval#getEnd()
     */
    @Override
    public DateTime getEnd() {
        return duration.getEnd();
    }

    /**
     * Returns an {@link Iterable} which contains all time intervals of this
     * event and its repetitions. <code>maxEntries</code> is required to limit
     * the output for infinitely often repeated events. If
     * <code>maxEntries</code> is greater than
     * {@link PersistentCalendarEntry#count}, only <code>count</code> items will
     * be returned. Possible values for maxEntries are
     * <ul>
     * <li>&lt; 0 - output will be an empty iterable.</li>
     * <li>=0 - output will contain only the original event.</li>
     * <li>&gt; 0 - output will contain a maximum of <code>maxEntries</code>
     * repetitions, inclusive the original event.</li>
     * </ul>
     * 
     * @param maxEntries
     *            limits the output to a specified amount of repetitions.
     * @return An iterable that contains the original event and all repetitions,
     *         limited to <code>maxEntires</code>.
     */
    public Iterable<Interval> getEntryList(int maxEntries) {
        List<Interval> dates = new ArrayList<Interval>();
        Interval last;

        if (count != -1)
            maxEntries = (maxEntries < count) ? maxEntries : count;

        if (maxEntries == 0)
            return dates;

        dates.add(duration);
        last = duration;
        // >1, because duration itself is the first entry
        for (; maxEntries > 1; maxEntries--) {
            last = new Interval(last.getStart().plus(period), last.getEnd().plus(period));
            dates.add(last);
        }

        return Iterables.from(dates);
    }


    /**
     * Return the user identifiaction of the owner of this entry. The owner is
     * the creator of the entry.
     * 
     * @return The user id of the user who is the owner of this entry.
     */
    @Override
    public UserIdentifier getOwner() {
        return owner;
    }

    /**
     * Returns the timespan that was defined to be between two starts of this
     * appointment.
     * 
     * @return the time between two repetitions of this appointment as a
     *         {@link Period}
     */
    public Period getPeriod() {
        return period;
    }

    /**
     * Returns the start date of this entry. The start date is never
     * <code>null</code> and never after the end date.
     * 
     * @return The date when this entry starts.
     * 
     * @see Interval#getStart()
     */
    @Override
    public DateTime getStart() {
        return duration.getStart();
    }

    /**
     * Returns the title of this entry. The title cannot be empty.
     * 
     * @return The title of this entry.
     */
    @Override
    public String getTitle() {
        return title;
    }

    /**
     * Returns all users that have the given capability. The capability must not
     * be <code>null</code>.
     * 
     * @param capability
     *            The capability for which all users should be found.
     * 
     * @return An {@link Iterable} which contains all user IDs of users who have
     *         the capability or null if there is no user who has the capability
     *         for this entry.
     */
    @Override
    public Iterable<UserIdentifier> getUsersByCapability(CalendarEntryCapability capability) {
        Objects.requireNonNull(capability, "capability");

        List<UserIdentifier> result = new ArrayList<UserIdentifier>();
        for (Entry<UserIdentifier, EnumSet<CalendarEntryCapability>> e : capabilities.entrySet()) {
            if (e.getValue().contains(capability))
                result.add(e.getKey());
        }

        return result;
    }

    /**
     * Returns the hash code for this entry. The hash of this object is the hash
     * of its primary key.
     * 
     * @return The hash code for this entry.
     */
    @Override
    public int hashCode() {
        return calendarEntryIdentifier.hashCode();
    }

    /**
     * Removes a capability from a user. Parameters must not be
     * <code>null</code>. The owner of this entry can not loose any capability.
     * 
     * @param user
     *            userID of the user who should loose the capability
     * @param capability
     *            the capability that schould be removed from the user
     * 
     * @throws IllegalArgumentException
     *             if a capability from the owner should be removed.
     */
    @Override
    public void removeCapability(UserIdentifier user, CalendarEntryCapability capability) {
        Objects.requireNonNull(user, "user");
        Objects.requireNonNull(capability, "capability");
        if (user.equals(owner))
            throw new IllegalArgumentException("Capabilities cannot be removed from the owner of the calendar entry");

        capabilities.get(user).remove(capability);

        if (capabilities.get(user).isEmpty())
            capabilities.remove(user);
    }

    /**
     * Sets the number how often this appointment should be repeated. -1 means
     * infinitely.
     * 
     * @param count
     *            Number how often this appointment should be repeated. This
     *            must be a positive value, including zero. Also -1 is allowed
     *            for indicating infinite repetitions.
     */
    public void setCount(int count) {
        if (count < -1) {
            throw new IllegalArgumentException("The repeat count of an appointment can't be less than zero, except -1, but was " + count);
        }
        this.count = count;
    }

    /**
     * Sets the new description of this entry. The description must not be null
     * but can be an empty string.
     * 
     * @param description
     *            The new description for this entry.
     */
    @Override
    public void setDescription(String description) {
        this.description = Objects.requireNonNull(description, "description");
    }

    /**
     * Sets the end date of this entry. The end date must neither be
     * <code>null</code> nor before the start date.
     * 
     * @param end
     *            The new end date.
     * 
     * @throws IllegalArgumentException
     *             if the end is before the start
     * 
     * @see DateTime
     */
    @Override
    public void setEnd(DateTime end) {
        Objects.requireNonNull(end, "end");
        duration = new Interval(duration.getStart(), end);
        // this.endTime = end.toDate();
    }

    /**
     * Sets a new timespan that should be between two starts of this entry. The
     * timespan has to be smaller than the duration of this appointment.
     * 
     * @param period
     *            The timespan between two repetitions.
     */
    public void setPeriod(Period period) {
        Objects.requireNonNull(period, "period");

        if ((count != 0) && duration.getStart().plus(period).isBefore(duration.getEnd()))
            throw new IllegalArgumentException("The time between two repetitions can not be less than the duration of a calendar entry!");

        this.period = period;
    }

    /**
     * Sets the start date of this entry. The start date must neither be
     * <code>null</code> nor after the end date.
     * 
     * @param start
     *            The new start date.
     * 
     * @throws IllegalArgumentException
     *             if the start is after the end.
     * 
     * @see DateTime
     */
    @Override
    public void setStart(DateTime start) {
        Objects.requireNonNull(start, "start");
        duration = new Interval(start, duration.getEnd());
        // this.startTime = start.toDate();
    }

    /**
     * Sets the new title of the entry. The title must neither be
     * <code>null</code> nor empty.
     * 
     * @param title
     *            the new title for this entry
     * 
     * @throws IllegalArgumentException
     *             if title is empty
     */
    @Override
    public void setTitle(String title) {
        Objects.requireNonNull(title, "title");
        if (title.isEmpty())
            // Why?
            throw new IllegalArgumentException("The title cannot be empty.");
        this.title = title;
    }

    /**
     * Returns a string representation of this entry. The format of this string
     * is:
     * <p>
     * <code>title + " (" + getStart() + " - " + getEnd() + ")"</code>
     * </p>
     * 
     * @return The string representation of this entry.
     */
    @Override
    public String toString() {
        return title + " (" + getStart() + " - " + getEnd() + ")";
    }

}