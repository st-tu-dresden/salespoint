package praktikum.config;

import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;


import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@EnableWebMvc
@Configuration
@ComponentScan(basePackages = "example.controller")
public class WebConfig extends WebMvcConfigurerAdapter  {
	
	// standard viewresolver 
    @Bean
    public ViewResolver getViewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/jsp/");
        viewResolver.setSuffix(".jsp");
 
        return viewResolver;
     }
    
    // messages for i18n
    // do not change the name of this method
    @Bean
    public MessageSource messageSource() {
    	ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    	messageSource.setBasename("messages");
    	return messageSource;
    }
    
    // for static resources like images and css files
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    }
    
    // Salespoint5 goodies start
	/*
    public WebConfig() {
    	// The name of your persistence-unit, look it up in persistence.xml
    	org.salespointframework.core.database.Database.INSTANCE.initializeEntityManagerFactory("Videoshop");

    }
    
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

