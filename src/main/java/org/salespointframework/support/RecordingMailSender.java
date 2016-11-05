package org.salespointframework.support;

import java.util.ArrayList;
import java.util.List;

import org.salespointframework.core.Streamable;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

/**
 * A {@link MailSender} implementation that allows looking up all {@link SimpleMailMessage}s that have been sent. Will
 * also log the messages as they are received for sending. The required setup steps can be found in
 * {@link ConsoleWritingMailSender}'s JavaDoc.
 *
 * @author Oliver Gierke
 * @since 6.2.1
 * @soundtrack Benny Greb - Soulfood (Moving Parts Live - https://www.youtube.com/watch?v=q9Ft5xLlBg4)
 */
public class RecordingMailSender extends ConsoleWritingMailSender {

	private final List<SimpleMailMessage> sentMessages = new ArrayList<>();

	/* 
	 * (non-Javadoc)
	 * @see org.salespointframework.support.ConsoleWritingMailSender#send(org.springframework.mail.SimpleMailMessage)
	 */
	@Override
	public void send(SimpleMailMessage simpleMessage) throws MailException {

		super.send(simpleMessage);

		this.sentMessages.add(simpleMessage);
	}

	/**
	 * Returns all {@link SimpleMailMessage}s that have been recorded so far.
	 * 
	 * @return will never be {@literal null}.
	 */
	public Streamable<SimpleMailMessage> getSentMessages() {
		return Streamable.of(sentMessages);
	}
}
