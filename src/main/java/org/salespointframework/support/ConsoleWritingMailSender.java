package org.salespointframework.support;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

/**
 * {@link MailSender} implementation writing the {@link SimpleMailMessage} to be sent into the logs. Should be used for
 * testing purposes only.
 * <p>
 * The component can be used by declaring a Spring bean definition for it, e.g. in a Spring Boot application like this:
 * 
 * <pre>
 * &#64;Bean
 * public MailSender mailSender() {
 * 	return new ConsoleWritingMailSender();
 * }
 * </pre>
 * 
 * Make sure, you're setting the log level for Salespoint to INFO to actually see the sent messages. This can be
 * achieved adding the following line to your {@code application.properties}.
 * 
 * <pre>
 * logging.level.org.salespointframework.support = INFO
 * </pre>
 * 
 * To use a real {@link MailSender} instead, include the {@code spring-boot-starter-mail} module in your Maven POM file
 * and follow the instructions in the
 * <a href="http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-email">reference
 * documentation</a>.
 * 
 * @author Oliver Gierke
 * @see http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-email
 */
public class ConsoleWritingMailSender implements MailSender {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConsoleWritingMailSender.class);

	/* 
	 * (non-Javadoc)
	 * @see org.springframework.mail.MailSender#send(org.springframework.mail.SimpleMailMessage)
	 */
	@Override
	public void send(SimpleMailMessage simpleMessage) throws MailException {
		LOGGER.info(simpleMessage.toString());
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.mail.MailSender#send(org.springframework.mail.SimpleMailMessage[])
	 */
	@Override
	public void send(SimpleMailMessage... simpleMessages) throws MailException {
		Arrays.stream(simpleMessages).forEach(message -> send(message));
	}
}
