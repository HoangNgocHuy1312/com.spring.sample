package com.spring.sample.configuration;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@PropertySource("classpath:application.properties")
public class SpringMailConfig {

	@Value("${config.mail.host}")
	private String host;
	@Value("${config.mail.port}")
	private String port;
	@Value("${config.mail.protocol}")
	private String protocol;
	@Value("${config.mail.username}")
	private String email;
	@Value("${config.mail.password}")
	private String password;
	@Value("${mail.smtp.auth}")
	private String mail_smtp_auth;
	@Value("${config.mail.password}")
	private String mail_smtp_starttls_enable;
	@Value("${config.mail.password}")
	private String mail_smtp_quitwait;

	@Bean("mailSender")
	public JavaMailSender mailSender() throws IOException {
		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", port);
		Session session = Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(email, password);
			}
		});
//        Message message = new MimeMessage(session);
//        try {
//            message.setRecipients(Message.RecipientType.TO, new InternetAddress[]{new InternetAddress("received_mail@domain.com")});
//
//            message.setFrom(new InternetAddress(email));
//            message.setSubject("Spring-email-with-thymeleaf subject");
//            message.setContent(thymeleafService.getContent(), CONTENT_TYPE_TEXT_HTML);
//            Transport.send(message);
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }

		final JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setSession(session);

		// Basic mail sender configuration, based on emailconfig.properties
		mailSender.setHost(host);
		mailSender.setPort(Integer.parseInt(port));
		mailSender.setProtocol(protocol);
		mailSender.setUsername(email);
		mailSender.setPassword(password);

		// JavaMail-specific mail sender configuration, based on javamail.properties
		final Properties javaMailProperties = new Properties();
		javaMailProperties.put("mail.smtp.auth", true);
		javaMailProperties.put("mail.smtp.starttls.enable", true);
		javaMailProperties.put("mail.smtp.quitwait", true);
		mailSender.setJavaMailProperties(javaMailProperties);

		return mailSender;

	}

}
