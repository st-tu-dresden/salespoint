package videoshop;

import org.salespointframework.Salespoint;
import org.salespointframework.SalespointWebConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableAutoConfiguration
@Import({ Salespoint.class, SalespointWebConfiguration.class })
@ComponentScan
public class VideoShop {

	public static void main(String[] args) {
		SpringApplication.run(VideoShop.class, args);
	}

	@Configuration
	@EnableWebSecurity
	@EnableGlobalMethodSecurity(prePostEnabled = true)
	static class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

		@Autowired AuthenticationProvider authenticationProvider;

		@Override
		protected void registerAuthentication(AuthenticationManagerBuilder amBuilder) {
			amBuilder.authenticationProvider(authenticationProvider);
		}

		@Bean
		@Override
		public AuthenticationManager authenticationManagerBean() throws Exception {
			return super.authenticationManagerBean();
		}

		@Override
		public void configure(WebSecurity web) throws Exception {
			web.ignoring().antMatchers("/resources/**");
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {

			http.csrf().disable();

			http.authorizeRequests().antMatchers("/**").permitAll().and().formLogin().loginProcessingUrl("/login").and()
					.logout().logoutUrl("/logout").logoutSuccessUrl("/");
		}
	}
}
