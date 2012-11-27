package praktikum.config;

import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
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
@Import(Sp5Manager.class)
@ComponentScan(basePackages = "videoshop" ) /* All your basepackages are belong to us, seriously, put your basepackage here */
public class WebConfig extends WebMvcConfigurerAdapter  {
	
	// standard viewresolver 
    @Bean
    public ViewResolver getViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/jsp/");
        resolver.setSuffix(".jsp");
 
        return resolver;
     }
    
    // messages for i18n
    @Bean
    public MessageSource getMessageSource() {
    	ResourceBundleMessageSource src = new ResourceBundleMessageSource();
    	src.setBasename("messages");
    	return src;
    }
    
    // for static resources like images and css files
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/res/**").addResourceLocations("/resources/");
    }
    
    // Salespoint5 goodies
    public WebConfig() {
    	// The name of your persistence-unit, look it up in persistence.xml
    	org.salespointframework.core.database.Database.INSTANCE.initializeEntityManagerFactory("Videoshop");

    }
    
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
    	argumentResolvers.add(new org.salespointframework.web.spring.LoggedInUserArgumentResolver());
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    	registry.addInterceptor(new org.salespointframework.web.spring.interceptor.LoggedInUserInterceptor());
    	registry.addInterceptor(new org.salespointframework.web.spring.interceptor.CapabilitiesInterceptor());
    }
    
    @Override
    public void addFormatters(FormatterRegistry registry) {
    	registry.addConverterFactory(new org.salespointframework.web.spring.converter.StringToIdentifierConverterFactory());
    	registry.addConverter(new org.salespointframework.web.spring.converter.StringToCapabilityConverter());
    	registry.addConverter(new org.salespointframework.web.spring.converter.StringToMoneyConverter());
    	registry.addConverter(new org.salespointframework.web.spring.converter.StringToUnitsConverter());
    }
}
