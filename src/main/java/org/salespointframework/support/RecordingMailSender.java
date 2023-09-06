/*
 * Copyright 2017-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.salespointframework.support;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.util.Streamable;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

/**
 * A {@link MailSender} implementation that allows looking up all {@link SimpleMailMessage}s that have been sent. Will
 * also log the messages as they are received for sending. The required setup steps can be found in
 * {@link ConsoleWritingMailSender}'s JavaDoc.
 *
 * @author Oliver Gierke
 * @since 6.3.1
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
