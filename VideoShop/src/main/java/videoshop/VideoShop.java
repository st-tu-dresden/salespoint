package videoshop;

import java.util.List;

import org.salespointframework.Salespoint;
import org.salespointframework.web.SalespointDaoAuthenticationProvider;
import org.salespointframework.web.spring.converter.JpaEntityConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.format.FormatterRegistry;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Central application class for VideoShop.
 * 
 * @author Oliver Gierke
 */
@Configuration
@EnableAutoConfiguration
@Import({ Salespoint.class, VideoShop.WebConfiguration.class, VideoShop.CustomWebSecurityConfigurerAdapter.class })
@ComponentScan
public class VideoShop {

	/**
	 * runs the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(VideoShop.class, args);
	}

	/**
	 * Custom web configuration for Spring MVC. 
	 *
	 * @author Oliver Gierke
	 */
	@Configuration
	static class WebConfiguration extends WebMvcConfigurerAdapter {

		// Web application configuration
		@Autowired JpaEntityConverter entityConverter;

		@Override
		public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
			argumentResolvers.add(new org.salespointframework.web.spring.support.LoggedInUserArgumentResolver());
		}

		@Override
		public void addInterceptors(InterceptorRegistry registry) {
			registry.addInterceptor(new org.salespointframework.web.spring.support.LoggedInUserInterceptor());
			registry.addInterceptor(new org.salespointframework.web.spring.support.CapabilitiesInterceptor());
		}

		@Override
		public void addFormatters(FormatterRegistry registry) {
			registry.addConverter(entityConverter);
			registry.addConverter(new org.salespointframework.web.spring.converter.StringToCapabilityConverter());
		}

		@Override
		public void addResourceHandlers(ResourceHandlerRegistry registry) {
			registry.addResourceHandler("/res/**").addResourceLocations("/resources/");
		}
	}

	@EnableWebSecurity
	@Configuration
	static class CustomWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
	  @Override
	  protected void registerAuthentication(AuthenticationManagerBuilder auth) {
		  
		  auth.authenticationProvider(new SalespointDaoAuthenticationProvider());
		  
		  return;
	    auth
	      .inMemoryAuthentication()
	        .withUser("user")  // #1
	          .password("password")
	          .roles("USER")
	          .and()
	        .withUser("admin") // #2
	          .password("password")
	          .roles("ADMIN","USER");
	  }

	  @Override
	  public void configure(WebSecurity web) throws Exception {
	    //web	      .ignoring()	         .antMatchers("/resources/**"); // #3
	  }

	  //@Override
	  protected void configure_(HttpSecurity http) throws Exception {
	    http
	      .authorizeUrls()
	        .antMatchers("/signup","/about").permitAll() // #4
	        .antMatchers("/admin/**").hasRole("ADMIN") // #6
	        .anyRequest().authenticated() // 7
	        .and()
	    .formLogin()  // #8
	        .loginUrl("/login") // #9
	        .permitAll(); // #5
	  }
	}
}
