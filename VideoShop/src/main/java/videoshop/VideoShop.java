package videoshop;

import java.util.List;

import org.salespointframework.Salespoint;
import org.salespointframework.core.useraccount.UserAccountDetailService;
import org.salespointframework.core.useraccount.UserAccountManager;
import org.salespointframework.util.SalespointPasswordEncoder;
import org.salespointframework.web.spring.converter.JpaEntityConverter;
import org.salespointframework.web.spring.converter.StringToRoleConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.format.FormatterRegistry;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
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
 * @author Paul Henke
 */
@Configuration
@EnableAutoConfiguration
@Import({ Salespoint.class, VideoShop.WebConfiguration.class,
		VideoShop.WebSecurityConfiguration.class })
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
		@Autowired
		private JpaEntityConverter entityConverter;

		@Autowired
		private UserAccountManager userAccountManager;

		@Override
		public void addArgumentResolvers(
				List<HandlerMethodArgumentResolver> argumentResolvers) {
			argumentResolvers
					.add(new org.salespointframework.web.spring.support.LoggedInUserArgumentResolver(
							userAccountManager));
		}

		@Override
		public void addInterceptors(InterceptorRegistry registry) {
			// registry.addInterceptor(new
			// org.salespointframework.web.spring.support.LoggedInUserInterceptor());
		}

		@Override
		public void addFormatters(FormatterRegistry registry) {
			registry.addConverter(entityConverter);
			registry.addConverter(new StringToRoleConverter());
		}

		@Override
		public void addResourceHandlers(ResourceHandlerRegistry registry) {
			// registry.addResourceHandler("/res/**").addResourceLocations("/resources/");
		}
	}

	@EnableWebSecurity
	@Configuration
	static class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

		@Autowired
		private UserAccountManager userAccountManager;

		@Override
		protected void registerAuthentication(
				AuthenticationManagerBuilder builder) {

			DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

			provider.setPasswordEncoder(new SalespointPasswordEncoder());
			provider.setUserDetailsService(new UserAccountDetailService(
					userAccountManager));

			builder.authenticationProvider(provider);

			return;
			/*
			 * auth.inMemoryAuthentication().withUser("user") // #1
			 * .password("password").roles("USER").and().withUser("admin") // #2
			 * .password("password").roles("ADMIN", "USER");
			 */
		}

		@Override
		public void configure(WebSecurity web) throws Exception {
			web.ignoring().antMatchers("/resources/**"); // #3
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {

			http.csrf().disable();

			http.authorizeRequests().antMatchers("/**").permitAll().and()
					.formLogin().loginProcessingUrl("/login").and().logout()
					.logoutUrl("/logout").logoutSuccessUrl("/");

			// WTF
			// http://docs.spring.io/spring-security/site/docs/3.2.x/apidocs/org/springframework/security/config/annotation/web/builders/HttpSecurity.html#logout()

		}
	}
}
