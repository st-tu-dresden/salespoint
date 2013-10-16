package blankweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@Configuration
@EnableAutoConfiguration
@Import({ BlankWeb.WebConfiguration.class })
//@ComponentScan
@ComponentScan(basePackages = "blankweb.controller")
public class BlankWeb {

	public static void main(String[] args) {
		SpringApplication.run(BlankWeb.class, args);
	}
	
	@Configuration
	static class WebConfiguration extends WebMvcConfigurerAdapter {

		@Override
		public void addResourceHandlers(ResourceHandlerRegistry registry) {
			registry.addResourceHandler("/res/**").addResourceLocations("/resources/");
		}
	}
}
