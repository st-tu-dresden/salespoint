package org.salespointframework.support;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;
import org.springframework.mail.SimpleMailMessage;

/**
 * Unit tests for {@link RecordingMailSender}.
 * 
 * @author Oliver Gierke
 */
public class RecordingMailSenderUnitTests {

	/**
	 * @see #149
	 */
	@Test
	public void exposesSentEmails() {

		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("me");
		message.setTo("you");
		message.setSubject("subject");
		message.setText("text");

		RecordingMailSender sender = new RecordingMailSender();
		sender.send(message);

		assertThat(sender.getSentMessages()).containsExactly(message);
	}
}
