package calendar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CalendarController {
    @RequestMapping("/calendar")
    public ModelAndView index(ModelAndView mav) {
        mav.setViewName("calendar");
        return mav;
    }
}
