package org.salespointframework.core.calendar;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.salespointframework.core.users.UserIdentifier;
import org.salespointframework.util.Iterables;

/**
 * This class stores a set of capabilities that a user has relative to a
 * calendar entry.
 * 
 * @author Stanley Förster
 * 
 */
//@Entity
class CalendarEntryCapabilitySet {
    /**
     * A set that stores the user's capabilities for the calendar entry.
     */
    @ElementCollection
    //private EnumSet<CalendarEntryCapability> capabilities = EnumSet.noneOf(CalendarEntryCapability.class); 
    private Set<CalendarEntryCapability> capabilities = new HashSet<CalendarEntryCapability>();

    /**
     * Simple id of this set, which is used as primary key.
     */
    @SuppressWarnings("unused")
    @Id
    @GeneratedValue
    private long                         id;

    /**
     * The identification of the user, whose capabilities are stored in this
     * set.
     */
    @AttributeOverride(name = "id", column = @Column(name = "USER_ID"))
    private UserIdentifier               userIdentifier;

    /**
     * The identification of the calendar entry for that the user's capabilities
     * are stored in this set.
     */
    @AttributeOverride(name = "id", column = @Column(name = "ENTRY_ID"))
    private CalendarEntryIdentifier      calendarEntryIdentifier;

    /**
     * This constructor only exists for persistence reasons. You must not use it
     * to keep the calendar system working.
     */
    @Deprecated
    public CalendarEntryCapabilitySet() {
    }

    /**
     * Creates a new set of capabilities which are related to the given pair of
     * user and calendar entry. It defindes wich capabilities a user has
     * relative to a calendar entry.
     * 
     * @param userIdentifier
     *            The user's identification who is part of this relationship.
     * @param calendarEntryIdentifier
     *            The calendar entrie's identification that is part of this
     *            relationship.
     */
    public CalendarEntryCapabilitySet(UserIdentifier userIdentifier, CalendarEntryIdentifier calendarEntryIdentifier) {
        this.calendarEntryIdentifier = calendarEntryIdentifier;
        this.userIdentifier = userIdentifier;
    }

    /**
     * Returns the user identification whose capabilities for a calendar entry
     * are stored in this set.
     * 
     * @return The user identification whose capabilities for a calendar entry
     *         are stored in this set.
     */
    public UserIdentifier getUserIdentifier() {
        return userIdentifier;
    }

    /**
     * Returns the calendar entry identification of the calendar entry that is
     * part of this relationship.
     * 
     * @return The calendar entry identification of the calendar entry that is
     *         part of this relationship.
     */
    public CalendarEntryIdentifier getCalendarEntryIdentifier() {
        return calendarEntryIdentifier;
    }

    /**
     * Adds the given capability to the set of capabilities of this
     * relationship.
     * 
     * @param capability
     *            The capability that should be added.
     */
    public void add(CalendarEntryCapability capability) {
        capabilities.add(capability);
    }

    /**
     * Adds all given capabilities to the set of capabilities of this
     * relationship.
     * 
     * @param capabilities
     *            All capabilities that should be added.
     */
    public void addAll(CalendarEntryCapability[] capabilities) {
        this.capabilities.addAll(Arrays.asList(capabilities));

    }

    /**
     * Removes the given capability from the set of capabilities of this
     * relationship.
     * 
     * @param capability
     *            the capability that should be removed.
     */
    public void remove(CalendarEntryCapability capability) {
        capabilities.remove(capability);
    }

    /**
     * Checks whether at least one capability is stored in the set.
     * 
     * @return <code>true</code> if at least one capability is contained in the
     *         set, <code>false</code> otherwise.
     */
    public boolean isEmpty() {
        return capabilities.isEmpty();
    }

    /**
     * Returns the set where all capabilities of this relationship are stored in
     * as an iterable.
     * 
     * @return An iterable that contains all capabilities of this relationship.
     */
    public Iterable<CalendarEntryCapability> getCapabilities() {
        return Iterables.from(capabilities);
    }

    /**
     * Checks whether the given capability is part of this relationship or not.
     * 
     * @param capability
     *            The capability that should be checked.
     * @return <code>true</code> if the given capability is part of this
     *         relationship or not, <code>false</code> otherwise.
     */
    public boolean contains(CalendarEntryCapability capability) {
        return capabilities.contains(capability);
    }
}
