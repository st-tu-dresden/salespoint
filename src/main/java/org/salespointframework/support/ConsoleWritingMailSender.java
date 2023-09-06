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

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

/**
 * {@link MailSender} implementation writing the {@link SimpleMailMessage} to be sent into the logs. If you want to
 * access the mail messages sent, prefer {@link RecordingMailSender}.
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
 * <a href="https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#io.email">reference documentation</a>.
 * 
 * @author Oliver Gierke
 * @see <a href="https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#io.email">Sending Email (Spring
 *      Boot Reference Docs)</a>
 * @see RecordingMailSender
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
		Arrays.stream(simpleMessages).forEach(this::send);
	}
}
