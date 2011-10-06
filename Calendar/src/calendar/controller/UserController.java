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
    public ModelAndView index(ModelAndView mav, HttpSession session) {
        System.out.println("got session: "+session);
        
        PersistentUser user = null;
        
        if (session != null) {
            PersistentUserManager mgr = new PersistentUserManager();
            user = mgr.getUserByToken(PersistentUser.class, session);
        }
        
        if (user != null) {
            mav.addObject("user", user);
            mav.setViewName("redirect:/calendar");
        } else {
            mav.setViewName("login");
        }
        
        return mav;
    }

    @RequestMapping("/register")
    public String register() {
        return "register";
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        PersistentUserManager mgr = new PersistentUserManager();
        mgr.logOff(session);
        return "login";
    }
    
    @RequestMapping(value = "/loginUser", method = RequestMethod.POST)
    public ModelAndView loginUser(@RequestParam("username") String name, @RequestParam("password") String password, ModelAndView mav, HttpSession session) {
        PersistentUserManager mgr = new PersistentUserManager();
        PersistentUser user = mgr.get(PersistentUser.class, new UserIdentifier(name));

        if (user != null) {
            if (user.verifyPassword(password)) {
                mgr.logOn(user, session);
                
                mav.addObject("user", user);
                mav.setViewName("redirect:/calendar");
            } else {
                mav.addObject("message","Wrong password");
                mav.setViewName("login");
            }
        } else {
            mav.addObject("message","Unknown user");
            mav.setViewName("login");
        }

        return mav;
    }

    @RequestMapping(value = "/registerUser", method = RequestMethod.POST)
    public ModelAndView registerUser(@RequestParam("username") String name, @RequestParam("pwOne") String pwOne, @RequestParam("pwTwo") String pwTwo,
                    ModelAndView mav, HttpSession session) {
        PersistentUserManager mgr = new PersistentUserManager();

        if (pwOne.equals(pwTwo)) {
            PersistentUser user = new PersistentUser(new UserIdentifier(name), pwOne, new UserCapability("calendar"));
            try {
                mgr.add(user);
            } catch (DuplicateUserException ex) {
                mav.addObject("message", "Duplicate user");
                mav.setViewName("register");
                return mav;
            }

            //TODO if registered --> login user
            mgr.logOn(user, session);
            
            mav.addObject("user", user);
            mav.setViewName("redirect:/calendar");

        } else {
            mav.addObject("message", "Passwords are different");
            mav.setViewName("register");
        }

        return mav;
    }
}