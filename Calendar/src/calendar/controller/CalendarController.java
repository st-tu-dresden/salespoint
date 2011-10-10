package calendar.controller;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.salespointframework.core.calendar.CalendarEntryIdentifier;
import org.salespointframework.core.calendar.PersistentCalendar;
import org.salespointframework.core.calendar.PersistentCalendarEntry;
import org.salespointframework.core.user.PersistentUser;
import org.salespointframework.core.user.PersistentUserManager;
import org.salespointframework.core.user.UserIdentifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CalendarController {
    private static final DateTimeFormatter monthFormatter = new DateTimeFormatterBuilder().appendMonthOfYearText().toFormatter();
    // 2011-10-08T17:30
    private static final DateTimeFormatter inputFormatter = new DateTimeFormatterBuilder().appendYear(4, 4).appendLiteral("-").appendMonthOfYear(2)
                                                                          .appendLiteral("-").appendDayOfMonth(2).appendLiteral("T").appendHourOfDay(2)
                                                                          .appendLiteral(":").appendMinuteOfHour(2).toFormatter();
    private static final Period            ONE_MONTH      = new Period(0, 1, 0, 0, 0, 0, 0, 0);
    private static final Period            ONE_DAY        = new Period(0, 0, 0, 1, 0, 0, 0, 0);

    private ModelAndView addMonthData(int year, int month, ModelAndView mav) {
        DateTime firstDayOfMonth = new DateTime(year, month, 1, 0, 0, 0, 0);
        int week = 0;

        // last week is needed for December
        // if first or last day of a year is a Thursday, the year has 53 week,
        // otherwise there are 52 weeks per year.
        int numberOfWeeksLastYear = (new DateTime(year - 1, 1, 1, 0, 0, 0, 0).getDayOfWeek() == DateTimeConstants.THURSDAY || new DateTime(year - 1, 12, 31, 0,
                        0, 0, 0).getDayOfWeek() == DateTimeConstants.THURSDAY) ? 53 : 52;
        int numberOfWeeksThisYear = (new DateTime(year, 1, 1, 0, 0, 0, 0).getDayOfWeek() == DateTimeConstants.THURSDAY || new DateTime(year, 12, 31, 0, 0, 0, 0)
                        .getDayOfWeek() == DateTimeConstants.THURSDAY) ? 53 : 52;

        if (month != DateTimeConstants.JANUARY) {
            week = firstDayOfMonth.weekOfWeekyear().get();
        } else {
            if (firstDayOfMonth.getDayOfWeek() < DateTimeConstants.FRIDAY)
                week = 1;
            else
                week = numberOfWeeksLastYear;
        }

        int[] weekNumbers = new int[6];
        for (int i = 0; i < 6; i++) {

            if ((month == DateTimeConstants.JANUARY && week > numberOfWeeksLastYear) || (month == DateTimeConstants.DECEMBER && week > numberOfWeeksThisYear))
                week = 1;

            weekNumbers[i] = week++;
        }

        int firstWeekdayOfMonth = firstDayOfMonth.getDayOfWeek();
        int numberOfDaysPerMonth = firstDayOfMonth.plus(ONE_MONTH).minus(ONE_DAY).getDayOfMonth();

        mav.addObject("firstWeekday", Integer.valueOf(firstWeekdayOfMonth));
        mav.addObject("numberOfDays", Integer.valueOf(numberOfDaysPerMonth));
        mav.addObject("weekNumbers", weekNumbers);
        mav.addObject("year", Integer.valueOf(year));
        mav.addObject("month", Integer.valueOf(firstDayOfMonth.getMonthOfYear()));
        mav.addObject("monthTxt", monthFormatter.print(firstDayOfMonth));

        return mav;
    }

    private UserIdentifier getCurrentUser(HttpSession session) {
        PersistentUserManager mgr = new PersistentUserManager();
        return mgr.getUserByToken(PersistentUser.class, session).getIdentifier();
    }

    private Map<Integer, List<PersistentCalendarEntry>> getAppointments(int year, int month) {
        PersistentCalendar cal = new PersistentCalendar();

        DateTime start = new DateTime(year, month, 1, 0, 0);
        DateTime end = start.plusMonths(1);

        Map<Integer, List<PersistentCalendarEntry>> entries = new HashMap<Integer, List<PersistentCalendarEntry>>();

        for (PersistentCalendarEntry entry : cal.between(PersistentCalendarEntry.class, start, end)) {
            int startDay = entry.getStart().getDayOfMonth();
            int endDay = entry.getEnd().getDayOfMonth();
            List<PersistentCalendarEntry> dayEntries;

            for (; startDay <= endDay; startDay++) {
                if ((dayEntries = entries.get(startDay)) == null) {
                    dayEntries = new LinkedList<PersistentCalendarEntry>();
                    entries.put(startDay, dayEntries);
                }

                dayEntries.add(entry);
            }
        }

        return entries;
    }

    @SuppressWarnings("boxing")
    @RequestMapping("/calendar")
    public ModelAndView index(ModelAndView mav, @RequestParam(value = "year", required = false) Integer year,
                    @RequestParam(value = "month", required = false) Integer month, HttpSession session) {
        DateTime today = new DateTime();

        if (year == null)
            year = today.getYear();

        if (month == null)
            month = today.getMonthOfYear();

        if (month == 13) {
            month = 1;
            year++;
        }

        if (month == 0) {
            month = 12;
            year--;
        }

        mav.addObject("user", getCurrentUser(session));
        mav.addObject("entries", getAppointments(year, month));
        mav.setViewName("calendar");
        return addMonthData(year, month, mav);
    }

    @RequestMapping(value = "/entry", method = RequestMethod.GET)
    public ModelAndView newEntry(@RequestParam(value = "id", required = false) CalendarEntryIdentifier id, ModelAndView mav) {
        if (id != null) {
            PersistentCalendarEntry entry = new PersistentCalendar().get(PersistentCalendarEntry.class, id);
            mav.addObject("id", id);
            mav.addObject("title", entry.getTitle());
            mav.addObject("description", entry.getDescription());
            mav.addObject("start", inputFormatter.print(entry.getStart()));
            mav.addObject("end", inputFormatter.print(entry.getEnd()));
        }
        mav.setViewName("entry");
        return mav;
    }

    @RequestMapping(value = "/entry", method = RequestMethod.POST)
    public ModelAndView newEntry(@RequestParam(value = "id", required = false) CalendarEntryIdentifier id, @RequestParam("title") String title,
                    @RequestParam("description") String description, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate,
                    ModelAndView mav, HttpSession session) {

        PersistentCalendar cal = new PersistentCalendar();

        if (id == null || id.getIdentifier().equals("")) {
            PersistentCalendarEntry entry = new PersistentCalendarEntry(getCurrentUser(session), title, inputFormatter.parseDateTime(startDate),
                            inputFormatter.parseDateTime(endDate), description);
            cal.add(entry);
        } else {
            PersistentCalendarEntry entry = cal.get(PersistentCalendarEntry.class, id);
            entry.setTitle(title);
            entry.setDescription(description);
            entry.setStart(inputFormatter.parseDateTime(startDate));
            entry.setEnd(inputFormatter.parseDateTime(endDate));
            cal.update(entry);
        }

        mav.setViewName("redirect:/calendar");
        return mav;
    }
}
