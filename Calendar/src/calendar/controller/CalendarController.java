package calendar.controller;

import javax.servlet.http.HttpSession;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
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
    private static final Period ONE_MONTH = new Period(0, 1, 0, 0, 0, 0, 0, 0);
    private static final Period ONE_DAY = new Period(0, 0, 0, 1, 0, 0, 0, 0);
    
    private ModelAndView addMonthData(int year, int month, ModelAndView mav) {
        //FIXME: calculation of weeks
        DateTime firstDayOfMonth = new DateTime(year, month, 1, 0, 0, 0, 0);
        int week = 0;

        // last week is needed for December
        //      if first or last day of a year is a Thursday, the year has 53 week, otherwise there are 52 weeks per year.
        int numberOfWeeksLastYear = (new DateTime(year-1,1,1,0, 0, 0, 0).getDayOfWeek() == DateTimeConstants.THURSDAY || new DateTime(year-1,12,31,0, 0, 0, 0).getDayOfWeek() == DateTimeConstants.THURSDAY) ? 53 : 52 ;
        int numberOfWeeksThisYear = (new DateTime(year,1,1,0, 0, 0, 0).getDayOfWeek() == DateTimeConstants.THURSDAY || new DateTime(year,12,31,0, 0, 0, 0).getDayOfWeek() == DateTimeConstants.THURSDAY) ? 53 : 52 ;
        
        if (month != DateTimeConstants.JANUARY) {
            week = firstDayOfMonth.weekOfWeekyear().get();
        } else {
            if (firstDayOfMonth.getDayOfWeek() < DateTimeConstants.FRIDAY)
                week = 1;
            else
                week = numberOfWeeksLastYear;
        }

        int[] weekNumbers = new int[6];
        for (int i=0;i<6;i++) {
            
            if ((month == DateTimeConstants.JANUARY && week>numberOfWeeksLastYear) || (month == DateTimeConstants.DECEMBER && week>numberOfWeeksThisYear))
                week = 1;
            
            weekNumbers[i]=week++;
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
        mav.setViewName("calendar");
        return addMonthData(year, month, mav);
    }

    @RequestMapping(value = "/newEntry", method = RequestMethod.GET)
    public String newEntry() {
        return "newEntry";
    }

    @RequestMapping(value = "/newEntry", method = RequestMethod.POST)
    public String newEntry(@RequestParam("title") String title, @RequestParam("description") String description, ModelAndView mav, HttpSession session) {

        // PersistentCalendarEntry entry = new
        // PersistentCalendarEntry(getCurrentUser(session), title, start, end,
        // description);

        return null;
    }
}
