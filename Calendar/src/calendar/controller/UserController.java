package calendar.controller;

import javax.servlet.http.HttpSession;

import org.salespointframework.core.database.Database;
import org.salespointframework.core.user.DuplicateUserException;
import org.salespointframework.core.user.PersistentUser;
import org.salespointframework.core.user.PersistentUserManager;
import org.salespointframework.core.user.UserCapability;
import org.salespointframework.core.user.UserIdentifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {
    {
        Database.INSTANCE.initializeEntityManagerFactory("Calendar");
    }

    @RequestMapping("/")
    public String index() {
        return "/user/login";
    }

    @RequestMapping("/register")
    public String register() {
        return "/user/register";
    }

    @RequestMapping(value = "/loginUser", method = RequestMethod.POST)
    public ModelAndView loginUser(@RequestParam("username") String name, @RequestParam("password") String password, ModelAndView mav, HttpSession session) {
        PersistentUserManager mgr = new PersistentUserManager();
        PersistentUser user = mgr.get(PersistentUser.class, new UserIdentifier(name));

        if (user != null) {
            if (user.verifyPassword(password)) {
                mgr.logOn(user, session);
                
                mav.addObject(user);
                mav.setViewName("redirect:/calendar/");
            } else {
                mav.addObject("Wrong password");
                mav.setViewName("/user/login");
            }
        } else {
            mav.addObject("Unknown user");
            mav.setViewName("/user/login");
        }

        return mav;
    }

    @RequestMapping(value = "/registerUser", method = RequestMethod.POST)
    public ModelAndView registerUser(@RequestParam("username") String name, @RequestParam("pwOne") String pwOne, @RequestParam("pwTwo") String pwTwo,
                    ModelAndView mav) {
        PersistentUserManager mgr = new PersistentUserManager();

        if (pwOne.equals(pwTwo)) {
            PersistentUser user = new PersistentUser(new UserIdentifier(name), pwOne, new UserCapability("calendar"));
            try {
                mgr.add(user);
            } catch (DuplicateUserException ex) {
                mav.addObject("Duplicate user");
                mav.setViewName("/user/register");
                return mav;
            }

            mav.addObject(user);
            mav.setViewName("redirect:/calendar/");

        } else {
            mav.addObject("Passwords are different");
            mav.setViewName("/user/register");
        }

        return mav;
    }
}