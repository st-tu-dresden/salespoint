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
 * 
 * @author Oliver Gierke
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
	public void send(SimpleMailMessage[] simpleMessages) throws MailException {
		Arrays.stream(simpleMessages).forEach(message -> send(message));
	}
}
