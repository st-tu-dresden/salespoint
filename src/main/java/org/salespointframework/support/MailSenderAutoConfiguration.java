package org.salespointframework.support;

import java.util.Optional;

import org.salespointframework.support.MailSenderAutoConfiguration.MailProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * Spring Boot extension that automatically configures a {@link JavaMailSenderImpl} bean if an {@code user},
 * {@code password} and {@code host} property are set for the {@code spring.mail} namespace.
 * 
 * @author Oliver Gierke
 */
@Configuration
@ConditionalOnProperty(prefix = "spring.mail", value = { "username", "host", "password" })
@ConditionalOnMissingBean(MailSender.class)
@EnableConfigurationProperties(MailProperties.class)
public class MailSenderAutoConfiguration {

	@Autowired MailProperties properties;

	@Bean
	JavaMailSender mailSender() {

		JavaMailSenderImpl sender = new JavaMailSenderImpl();

		sender.setUsername(properties.getUsername());
		sender.setPassword(properties.getPassword());
		sender.setHost(properties.getHost());
		sender.setDefaultEncoding("UTF-8");

		properties.getPort().ifPresent(sender::setPort);

		return sender;
	}

	@ConfigurationProperties(prefix = "spring.mail")
	public static class MailProperties {

		private String host, username, password;
		private Integer port;

		public String getHost() {
			return host;
		}

		public void setHost(String host) {
			this.host = host;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public Optional<Integer> getPort() {
			return Optional.ofNullable(port);
		}

		public void setPort(Integer port) {
			this.port = port;
		}
	}
}
