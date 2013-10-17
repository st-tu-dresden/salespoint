package blankweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@Configuration
@EnableAutoConfiguration
@Import({ BlankWeb.WebConfiguration.class })
@ComponentScan
//@ComponentScan(basePackages = "blankweb.controller")
public class BlankWeb {

	public static void main(String[] args) {
		SpringApplication.run(BlankWeb.class, args);
	}
	
	@Configuration
	static class WebConfiguration extends WebMvcConfigurerAdapter {

		// TODO Nötig??
	    // messages for i18n
	    // do not change the name of this method
	    /*
		@Bean
	    public MessageSource messageSource() {
	    	ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
	    	messageSource.setBasename("messages");
	    	return messageSource;
	    }
	    */
		
	    // Salespoint5 goodies start
		/*
	   
	    @Override
	    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
	    	argumentResolvers.add(new org.salespointframework.web.spring.support.LoggedInUserArgumentResolver());
	    	argumentResolvers.add(new org.salespointframework.web.spring.support.SalespointArgumentResolver());
	    }
	    
	    @Override
	    public void addInterceptors(InterceptorRegistry registry) {
	    	registry.addInterceptor(new org.salespointframework.web.spring.support.LoggedInUserInterceptor());
	    	registry.addInterceptor(new org.salespointframework.web.spring.support.CapabilitiesInterceptor());
	    }
	    
	    @Override
	    public void addFormatters(FormatterRegistry registry) {
	    	registry.addConverterFactory(new org.salespointframework.web.spring.converter.StringToIdentifierConverterFactory());
	    	registry.addConverter(new org.salespointframework.web.spring.converter.StringToCapabilityConverter());
	    }
	    */
	    // Salespoint5 goodies end
	}
}
