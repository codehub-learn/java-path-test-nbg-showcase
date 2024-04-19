package gr.codelearn.rentbnb.service.impl;

import gr.codelearn.rentbnb.service.MailService;
import lombok.extern.slf4j.Slf4j;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Slf4j
public class MailServiceImpl implements MailService {
	private static final String USERNAME = "mymail@athtech.gr";
	private static final String PASSWORD = "-------mypassword-------";
	private Session session;

	public MailServiceImpl() {
		initializeContext();
	}

	private void initializeContext() {
		Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.office365.com");
		prop.put("mail.smtp.port", "587");
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.starttls.enable", "true");

		session = Session.getInstance(prop, new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(USERNAME, PASSWORD);
			}
		});
	}

	@Override
	public boolean send(String subject, String body, String receiver) {
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(USERNAME));
			//Multiple receivers can be added, seperated by a comma.
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver));
			message.setSubject(subject);
			message.setText(body);
			Transport.send(message);
		} catch (MessagingException e) {
			log.error("Error while sending mail", e);
			return false;
		}
		log.info("Message was sent successfully.");
		return true;
	}
}
