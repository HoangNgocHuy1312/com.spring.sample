package com.spring.sample.listener;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.github.sonus21.rqueue.annotation.RqueueListener;
import com.spring.sample.model.UserModel;
import com.spring.sample.util.Constants;

@Component
@PropertySource("classpath:application.properties")
public class SendMailListener {

	private static final Logger logger = LoggerFactory.getLogger(SendMailListener.class);

	@Value("${base.url}")
	private String baseUrl;

	@Value("${mail.default.from}")
	private String defaultFrom;

	@Autowired
	@Qualifier("templateEngine")
	TemplateEngine templateEngine;

	@Autowired
	JavaMailSender mailSender;

	@Autowired
	MessageSource messageSource;

	@RqueueListener(value = Constants.PASSWORD_RESET_QUEUE)
	public void passwordReset(UserModel userModel) {
		logger.info("Sending password reset mail to user");
		send("password_reset", "Password Reset", userModel);
	}

	@RqueueListener(value = Constants.ACCOUNT_ACTIVATION_QUEUE)
	public void accountActivation(UserModel userModel) {
		logger.info("Sending activation mail to user");
		send("account_activation", "Account Activation", userModel);
	}

	private void send(String template, String subject, UserModel user) {
		try {
			// Prepare the evaluation context
			final Context ctx = new Context();
			ctx.setVariable("baseUrl", baseUrl);
			ctx.setVariable("user", user);

			// Prepare message using a Spring helper
			final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
			final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, false, "UTF-8"); // true = multipart
			message.setSubject(subject);
			message.setFrom(defaultFrom);
			message.setTo(user.getEmail());

			// Create the HTML body using Thymeleaf
			final String htmlContent = this.templateEngine.process("mailers/" + template, ctx);
			// CONTENT_TYPE_TEXT_HTML
			message.setText(htmlContent, true); // true = isHtml

			// Add the inline image, referenced from the HTML code as
			// "cid:${imageResourceName}"
//     		final InputStreamSource imageSource = new ByteArrayResource(imageBytes);
//     		message.addInline(imageResourceName, imageSource, imageContentType);

			// Send mail
			this.mailSender.send(mimeMessage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(), e);
		}
	}

	//

//	private SimpleMailMessage constructEmailMessage(final RegistrationCompleteEvent event, final User user,
//			final String token) {
//		final String recipientAddress = user.getEmail();
//		final String subject = "Registration Confirmation";
//		final String confirmationUrl = event.getAppUrl() + "/registrationConfirm.html?token=" + token;
//		final String message = messages.getMessage("message.regSuccLink", null,
//				"You registered successfully. To confirm your registration, please click on the below link.",
//				event.getLocale());
//		final SimpleMailMessage email = new SimpleMailMessage();
//		email.setTo(recipientAddress);
//		email.setSubject(subject);
//		email.setText(message + " \r\n" + confirmationUrl);
//		email.setFrom(env.getProperty("support.email"));
//		return email;
//	}

}