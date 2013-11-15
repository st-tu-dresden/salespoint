package guestbook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;


// ‎(｡◕‿◕｡)
// Der Einstiegspunkt unserer Applikation.
// Hier kann sehr viel konfiguriert werden, da dies im GB nicht benötigt wird, wird dieser Punkt auf den Videoshop verschoben.
// Da sich hier eine main-Methode befindet, kann die Webanwendung wie eine normale Anwendung gestartet werden.


@Configuration
@EnableAutoConfiguration
@ComponentScan
public class GBStart {

	public static void main(String[] args) {
		SpringApplication.run(GBStart.class, args);
	}
	
	@Bean
	public CharacterEncodingFilter characterEncodingFilter() {
		final CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		characterEncodingFilter.setEncoding("UTF-8");
		characterEncodingFilter.setForceEncoding(true);
		return characterEncodingFilter;
	}
}
