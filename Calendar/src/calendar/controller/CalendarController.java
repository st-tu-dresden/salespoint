package calendar.controller;

import org.joda.time.DateTime;
import org.joda.time.DateTime.Property;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CalendarController {
    @RequestMapping("/calendar")
    public ModelAndView index(ModelAndView mav) {
        
        DateTime today = new DateTime();
        Property firstWeek = new DateTime( today.getYear(), today.getMonthOfYear(), 1, 0, 0, 0, 0).weekOfWeekyear();

        mav.addObject("firstWeek", Integer.valueOf(firstWeek.get()));
        mav.addObject("lastWeek", Integer.valueOf(firstWeek.get() + 50));
        mav.setViewName("calendar");
        return mav;
    }
}
