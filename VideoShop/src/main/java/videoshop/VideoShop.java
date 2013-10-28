package videoshop;

import java.util.List;

import org.salespointframework.Salespoint;
import org.salespointframework.core.useraccount.UserAccountDetailService;
import org.salespointframework.core.useraccount.UserAccountManager;
import org.salespointframework.spring.converter.JpaEntityConverter;
import org.salespointframework.spring.converter.StringToRoleConverter;
import org.salespointframework.spring.support.LoggedInUserAccountArgumentResolver;
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
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

// (｡◕‿◕｡)
// Die ganze Klasse ist komplett aus dem blankweb Projekt übernommen.

@Configuration
@EnableAutoConfiguration
@Import({Salespoint.class, VideoShop.WebConfiguration.class, VideoShop.WebSecurityConfiguration.class })
@ComponentScan
public class VideoShop {


	public static void main(String[] args) {
		SpringApplication.run(VideoShop.class, args);
	}


	@Configuration
	static class WebConfiguration extends WebMvcConfigurerAdapter {

		@Autowired
		private JpaEntityConverter entityConverter;

		@Autowired
		private UserAccountManager userAccountManager;

		@Override
		public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
			
			argumentResolvers.add(new LoggedInUserAccountArgumentResolver(userAccountManager));
		}

		@Override
		public void addFormatters(FormatterRegistry registry) {
			registry.addConverter(entityConverter);
			registry.addConverter(new StringToRoleConverter());
		}
	}

	@EnableWebSecurity
	@Configuration
	static class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

		@Autowired
		private UserAccountManager userAccountManager;

		@Override
		protected void registerAuthentication(AuthenticationManagerBuilder amBuilder) {

			DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

			provider.setPasswordEncoder(userAccountManager.getPasswordEncoder());
			provider.setUserDetailsService(new UserAccountDetailService(userAccountManager));

			amBuilder.authenticationProvider(provider);

			return;

		}

		@Override
		public void configure(WebSecurity web) throws Exception {
			web.ignoring().antMatchers("/resources/**"); 
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {

			http.csrf().disable();

			http
			.authorizeRequests().antMatchers("/**").permitAll()
			.and()
			.formLogin().loginProcessingUrl("/login")
			.and()
			.logout().logoutUrl("/logout").logoutSuccessUrl("/");

		}
	}
}
