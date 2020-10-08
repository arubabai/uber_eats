package com.uber.eats.serviceimpl;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.uber.eats.exception.DataInvalidException;
import com.uber.eats.service.CustomMailService;

@Service
public class CustomMailServiceImpl implements CustomMailService {

	@Autowired
	Environment env;

	@Override
	public void sendMail(String sendTo, String mailSubject, String mailBody) throws DataInvalidException {

		Properties properties = new Properties();

		properties.put("mail.smtp.host", env.getProperty("mail.smtp.host"));
		properties.put("mail.smtp.port", env.getProperty("mail.smtp.port"));
		properties.put("mail.smtp.auth", env.getProperty("mail.smtp.auth"));
		properties.put("mail.smtp.starttls.enable", env.getProperty("mail.smtp.starttls.enable"));

		final String mailServerUsername = env.getProperty("mail.username");
		final String mailServerPassword = env.getProperty("mail.password");

		Session session = Session.getInstance(properties, new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {

				return new PasswordAuthentication(mailServerUsername, mailServerPassword);

			}

		});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(mailServerUsername));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(sendTo));
			message.setSubject(mailSubject);
			message.setText(mailBody);

			Transport.send(message);

		} catch (MessagingException e) {

			e.printStackTrace();
			throw new DataInvalidException();

		}

	}

}
