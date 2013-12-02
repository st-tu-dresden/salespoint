package videoshop;

import org.salespointframework.SalespointWebConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
@Import({ SalespointWebConfiguration.class })
@ComponentScan
public class VideoShop {

	public static void main(String[] args) {
		SpringApplication.run(VideoShop.class, args);
	}

	@Configuration
	static class WebSecurityConfiguration extends org.salespointframework.SalespointSecurityConfiguration {

		@Override
		protected void configure(HttpSecurity http) throws Exception {

			http.csrf().disable();

			http.authorizeRequests().antMatchers("/**").permitAll().and().formLogin().loginProcessingUrl("/login").and()
					.logout().logoutUrl("/logout").logoutSuccessUrl("/");
		}
	}
}
