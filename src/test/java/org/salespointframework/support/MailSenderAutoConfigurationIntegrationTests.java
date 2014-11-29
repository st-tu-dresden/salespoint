package org.salespointframework.support;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * Integration tests for {@link MailSenderAutoConfiguration}.
 * 
 * @author Oliver Gierke
 */
public class MailSenderAutoConfigurationIntegrationTests {

	private static final String PREFIX = "classpath:org/salespointframework/support/";

	public @Rule ExpectedException exception = ExpectedException.none();

	/**
	 * @see #35
	 */
	@Test
	public void autoconfiguresMailSenderIfPropertiesSet() throws Exception {

		try (ConfigurableApplicationContext context = getContextWith("with-mail.properties")) {
			assertThat(context.getBean(MailSender.class), is(notNullValue()));
		}
	}

	/**
	 * @see #35
	 */
	@Test
	public void doesNotAutoConfigureForInsufficientProperties() throws Exception {

		exception.expect(NoSuchBeanDefinitionException.class);

		try (ConfigurableApplicationContext context = getContextWith("with-insufficient-mail.properties")) {
			context.getBean(MailSender.class);
		}
	}

	/**
	 * @see #35
	 */
	@Test
	public void usesManuallyConfiguredBean() throws Exception {

		try (ConfigurableApplicationContext context = getContextWith("with-mail.properties", ManualMailConfiguration.class)) {

			MailSender mailSender = context.getBean(MailSender.class);

			assertThat(mailSender, is(instanceOf(JavaMailSenderImpl.class)));
			JavaMailSenderImpl impl = (JavaMailSenderImpl) mailSender;

			assertThat(impl.getUsername(), is(nullValue()));
		}
	}

	/**
	 * @see #59
	 */
	@Test
	public void autoconfiguresMailSenderPort() throws Exception {

		try (ConfigurableApplicationContext context = getContextWith("with-mail.properties")) {

			MailSender mailSender = context.getBean(MailSender.class);

			assertThat(mailSender, is(notNullValue()));
			assertThat(ReflectionTestUtils.getField(mailSender, "port"), is((Object) 4711));
		}
	}

	private static ConfigurableApplicationContext getContextWith(String properties, Class<?>... additionalConfigClasses)
			throws Exception {

		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.getEnvironment().getPropertySources().addFirst(new ResourcePropertySource(PREFIX + properties));

		if (additionalConfigClasses.length > 0) {
			context.register(additionalConfigClasses);
		}

		context.register(MailSenderAutoConfiguration.class);
		context.refresh();

		return context;
	}

	@Configuration
	static class ManualMailConfiguration {

		@Bean
		JavaMailSender customMailSender() {
			return new JavaMailSenderImpl();
		}
	}
}
