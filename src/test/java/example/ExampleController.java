package example;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Example controller for integration tests.
 * 
 * @author Oliver Gierke
 */
@Controller
public class ExampleController {

	/**
	 * Expects the value of the request parameter to be {@code äöü€}.
	 * 
	 * @param value
	 * @param response
	 */
	@RequestMapping("/encoding")
	public void parameterEncoding(@RequestParam String value, HttpServletResponse response) {
		assertThat(value, is("äöü€"));
	}
}
