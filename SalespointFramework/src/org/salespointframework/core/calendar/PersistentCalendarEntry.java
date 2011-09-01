package org.salespointframework.core.calendar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.joda.time.DateTime;
import org.salespointframework.core.database.Database;
import org.salespointframework.core.users.UserIdentifier;
import org.salespointframework.util.ArgumentNullException;
import org.salespointframework.util.Objects;

/**
 * 
 * This is an abstract representation of an calendar entry which provides basic
 * functionality.
 * 
 * @author Stanley Förster
 * 
 */
@Entity
public final class PersistentCalendarEntry implements CalendarEntry {

    /**
     * The unique identifier for this entry.
     */
    @EmbeddedId
    private CalendarEntryIdentifier calendarEntryIdentifier;

    /**
     * The entitymanager is used to persist capabilities for this entry.
     * 
     * @see EntityManager
     */
    @Transient
    private EntityManager           em;
    
    /**
     * Description of this calendar entry. May be empty.
     */
    protected String                description;

    /**
     * The start date and time for this entry.
     */
    @Temporal(TemporalType.TIMESTAMP)
    protected Date                  startTime;

    /**
     * The end date and time for this entry.
     */
    @Temporal(TemporalType.TIMESTAMP)
    protected Date                  endTime;

    /**
     * Title of this entry.
     */
    protected String                title;

    /**
     * Represents how often this entry should be repeated. For determining the
     * time between two repetitions, see
     * {@link PersistentCalendarEntry#repeatStep}
     * 
     */
    protected int                   repeatCount;

    /**
     * Represents the time in millis between two repetitions of this entry. For
     * determining how often an entry should be repeated, see
     * {@link PersistentCalendarEntry#repeatCount}
     */
    protected long                  repeatStep;

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
     * @throws IllegalArgumentException
     *             The {@link IllegalArgumentException} will be thrown if the
     *             begin is not before the end of this calendar entry or the
     *             title is empty.
     * @throws ArgumentNullException
     *             The {@link ArgumentNullException} will be thrown if one ore
     *             more arguments are <code>null</code>
     */
    public PersistentCalendarEntry(UserIdentifier owner, String title, DateTime start, DateTime end) {
        Objects.requireNonNull(owner, "owner");
        Objects.requireNonNull(title, "title");
        Objects.requireNonNull(start, "start");
        Objects.requireNonNull(end, "end");

        em  = Database.INSTANCE.getEntityManagerFactory().createEntityManager();

        this.calendarEntryIdentifier = new CalendarEntryIdentifier();

        if (start.isAfter(end))
            throw new IllegalArgumentException("An calendar entry cannot end before it starts.");

        if (title.isEmpty())
            throw new IllegalArgumentException("The title cannot be empty.");

        this.title = title;
        this.startTime = start.toDate();
        this.endTime = end.toDate();

        description = "";
        repeatCount = 0;
        repeatStep = 0;

        addCapabilities(owner, CalendarEntryCapability.values());
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof CalendarEntry)
            return this.equals((CalendarEntry) object);
        return false;
    }

    /**
     * Determines if the given {@link CalendarEntry} is equal to this one or
     * not. Two calendar entries are equal to each other, if their hash code is
     * the same.
     * 
     * @param entry
     *            The entry this one should be compared with.
     * @return <code>true</code> if and only if the hash code of this calendar
     *         entry equals the hash code of the entry that is given as
     *         parameter. <code>false</code> otherwise.
     */
    public boolean equals(CalendarEntry entry) {
        return (entry != null) && (this.hashCode() == entry.hashCode());
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
     * Returns the description of this entry. The description may be empty but
     * never <code>null</code>.
     * 
     * @return the description of this entry
     */
    @Override
    public String getDescription() {
        return description;
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
     * Returns the start date of this entry. The start date is never
     * <code>null</code> and never after the end date.
     * 
     * @return The date when this entry starts.
     */
    @Override
    public DateTime getStart() {
        return new DateTime(startTime);
    }

    /**
     * Returns the end date of this entry. The end date is never
     * <code>null</code> and never before the start date.
     * 
     * @return The date when this entry ends.
     */
    @Override
    public DateTime getEnd() {
        return new DateTime(endTime);
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
        if (start.getMillis() >= endTime.getTime())
            throw new IllegalArgumentException("An calendar entry cannot start after it ends.");

        this.startTime = start.toDate();
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
        if (end.getMillis() <= startTime.getTime())
            throw new IllegalArgumentException("An calendar entry cannot end before it starts.");

        this.endTime = end.toDate();
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
            throw new IllegalArgumentException("The title cannot be empty.");
        this.title = title;
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
        Objects.requireNonNull(description, "description");
        this.description = description;
    }

    /**
     * Return the user identifiaction of the owner of this entry. Typically the
     * owner is the creator of an entry but this can change over time. An entry
     * can only have one owner at a time.
     * 
     * @return The user id of the user who is the owner of this entry.
     */
    @Override
    public UserIdentifier getOwner() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<CalendarEntryCapabilitySet> q = cb.createQuery(CalendarEntryCapabilitySet.class);
        Root<CalendarEntryCapabilitySet> r = q.from(CalendarEntryCapabilitySet.class);

        Predicate pEntry = cb.equal(r.get(CalendarEntryCapabilitySet_.calendarEntryIdentifier), this.calendarEntryIdentifier);
        q.where(pEntry);

        TypedQuery<CalendarEntryCapabilitySet> tq = em.createQuery(q);

        for (CalendarEntryCapabilitySet capSet : tq.getResultList()) {
            if (capSet.contains(CalendarEntryCapability.OWNER))
                return capSet.getUserIdentifier();
        }

        return null;
    }

    /**
     * Adds the capability to a specific user. Parameters must not be
     * <code>null</code>. The capability <code>OWNER</code> cannot be added,
     * because there can be only one owner who has been defined when the entry
     * was created.
     * 
     * @param user
     *            The user identification of the user who should get the new
     *            capability.
     * @param capability
     *            The new capability for the given user.
     * 
     * @throws IllegalArgumentException
     *             if capability <code>OWNER</code> should be added and there is
     *             an owner already.
     */
    @Override
    public void addCapability(UserIdentifier user, CalendarEntryCapability capability) {
        addCapabilities(Objects.requireNonNull(user, "user"), new CalendarEntryCapability[] { Objects.requireNonNull(capability, "capability") });
    }

    /**
     * Adds the capabilities to a specific user. Parameters must not be
     * <code>null</code>. The capability <code>OWNER</code> cannot be added,
     * because there can be only one owner who has been defined when the entry
     * was created.
     * 
     * @param user
     *            The user identification of the user who should get the new
     *            capabilities.
     * @param capabilities
     *            The new capabilities for the given user.
     * 
     * @throws IllegalArgumentException
     *             if capability <code>OWNER</code> should be added and there is
     *             an owner already.
     */
    private void addCapabilities(UserIdentifier user, CalendarEntryCapability[] capabilities) {
        Objects.requireNonNull(user, "user");
        Objects.requireNonNull(capabilities, "capability");

        if (Arrays.asList(capabilities).contains(CalendarEntryCapability.OWNER) && getUsersByCapability(CalendarEntryCapability.OWNER).iterator().hasNext())
            throw new IllegalArgumentException("There can be only one owner of a calendar entry!");

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<CalendarEntryCapabilitySet> q = cb.createQuery(CalendarEntryCapabilitySet.class);
        Root<CalendarEntryCapabilitySet> r = q.from(CalendarEntryCapabilitySet.class);

        Predicate pEntry = cb.equal(r.get(CalendarEntryCapabilitySet_.calendarEntryIdentifier), this.calendarEntryIdentifier);
        Predicate pUser = cb.equal(r.get(CalendarEntryCapabilitySet_.userIdentifier), user);
        q.where(cb.and(pUser, pEntry));

        TypedQuery<CalendarEntryCapabilitySet> tq = em.createQuery(q);

        EntityTransaction t = em.getTransaction();
        t.begin();
        {
            CalendarEntryCapabilitySet capSet = null;
            try {
                capSet = tq.getSingleResult();
                capSet.addAll(capabilities);
                em.merge(capSet);
            } catch (NoResultException e) {
                capSet = new CalendarEntryCapabilitySet(user, calendarEntryIdentifier);
                capSet.addAll(capabilities);
                em.persist(capSet);
            }
        }
        t.commit();
    }

    /**
     * Removes a capability from a user. Parameters must not be
     * <code>null</code>. The capability <code>OWNER</code> cannot be removed.
     * 
     * @param user
     *            userID of the user who should loose the capability
     * @param capability
     *            the capability that schould be removed from the user
     * 
     * @throws IllegalArgumentException
     *             if the capability <code>OWNER</code> should be removed.
     */
    @Override
    public void removeCapability(UserIdentifier user, CalendarEntryCapability capability) {
        Objects.requireNonNull(user, "user");
        Objects.requireNonNull(capability, "capability");

        if (capability == CalendarEntryCapability.OWNER)
            throw new IllegalArgumentException("Capability OWNER cannot be removed.");
        
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<CalendarEntryCapabilitySet> q = cb.createQuery(CalendarEntryCapabilitySet.class);
        Root<CalendarEntryCapabilitySet> r = q.from(CalendarEntryCapabilitySet.class);

        Predicate pEntry = cb.equal(r.get(CalendarEntryCapabilitySet_.calendarEntryIdentifier), this.calendarEntryIdentifier);
        Predicate pUser = cb.equal(r.get(CalendarEntryCapabilitySet_.userIdentifier), user);
        q.where(cb.and(pUser, pEntry));

        TypedQuery<CalendarEntryCapabilitySet> tq = em.createQuery(q);

        CalendarEntryCapabilitySet capSet = null;
        try {
            capSet = tq.getSingleResult();
            capSet.remove(capability);

            if (capSet.isEmpty()) {
                EntityTransaction t = em.getTransaction();
                t.begin();
                em.remove(capSet);
                t.commit();
            }
        } catch (NoResultException e) {

        }
    }

    /**
     * Returns all capabilities the given user has for this entry. The user identification
     * must not be <code>null</code>
     * 
     * @param user
     *            the user identification of the user whose capabilities should be returned.
     * 
     * @return An {@link Iterable} which contains all capabilities of this user
     *         or null if the user has no capabilities for this entry.
     */
    @Override
    public Iterable<CalendarEntryCapability> getCapabilitiesByUser(UserIdentifier user) {
        Objects.requireNonNull(user, "user");

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<CalendarEntryCapabilitySet> q = cb.createQuery(CalendarEntryCapabilitySet.class);
        Root<CalendarEntryCapabilitySet> r = q.from(CalendarEntryCapabilitySet.class);

        Predicate pEntry = cb.equal(r.get(CalendarEntryCapabilitySet_.calendarEntryIdentifier), this.calendarEntryIdentifier);
        Predicate pUser = cb.equal(r.get(CalendarEntryCapabilitySet_.userIdentifier), user);
        q.where(cb.and(pUser, pEntry));

        TypedQuery<CalendarEntryCapabilitySet> tq = em.createQuery(q);

        try {
            CalendarEntryCapabilitySet capSet = tq.getSingleResult();
            return capSet.getCapabilities();
        } catch (NoResultException e) {
            return null;
        }
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
        
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<CalendarEntryCapabilitySet> q = cb.createQuery(CalendarEntryCapabilitySet.class);
        Root<CalendarEntryCapabilitySet> r = q.from(CalendarEntryCapabilitySet.class);

        Predicate pEntry = cb.equal(r.get(CalendarEntryCapabilitySet_.calendarEntryIdentifier), this.calendarEntryIdentifier);
        q.where(pEntry);

        TypedQuery<CalendarEntryCapabilitySet> tq = em.createQuery(q);

        List<UserIdentifier> result = new ArrayList<UserIdentifier>();
        for (CalendarEntryCapabilitySet capSet : tq.getResultList()) {
            if (capSet.contains(capability))
                result.add(capSet.getUserIdentifier());
        }

        return result.isEmpty() ? null : result;
    }
}