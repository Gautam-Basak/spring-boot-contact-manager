package com.spring.demo.application.Service;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;


@Service
public class EmailService {
	
	/*
	 * @Value("smtp.gmail.com") private String host;
	 * 
	 * @Value("587") private int port;
	 * 
	 * 
	 * @Bean public JavaMailSender javaMailService() {
	 * 
	 * JavaMailSenderImpl javaMailSenderImpl = new JavaMailSenderImpl();
	 * javaMailSenderImpl.setHost(host); javaMailSenderImpl.setPort(port);
	 * javaMailSenderImpl.setUsername("basakgautam16@gmail.com");
	 * javaMailSenderImpl.setPassword("still love mine");
	 * 
	 * javaMailSenderImpl.setJavaMailProperties(javaMailProperties());
	 * 
	 * return javaMailSenderImpl;
	 * 
	 * } private Properties javaMailProperties() {
	 * 
	 * Properties properties = new Properties();
	 * 
	 * properties.put("mail.transport.protocol", "smtp");
	 * properties.put("mail.smtp.auth", "true");
	 * properties.put("mail.smtp.starttls.enable", "true");
	 * properties.put("mail.debug", "true");
	 * 
	 * return properties; }
	 * }
	 */
	
	//This method is responsible for sending email...
	public boolean sendEmail(String subject, String message, String to) {
		
		
		boolean f = false;
		
		String from = "basakgautam16@gmail.com";
	
		// From where gmail will send mail
		String host="smtp.gmail.com";
		
		
		//get the system properties
		Properties properties = System.getProperties();
		
		//Setting imp  info to properties object
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");

		
		
		//step 1: to get the session object
		 Session session = Session.getInstance(properties, new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				
				return new PasswordAuthentication("basakgautam16@gmail.com", "still love mine");
			}	
		});
		
		 session.setDebug(true);
		 
		 //step 2: compose the message (text or multi media)
		 
		 MimeMessage m = new MimeMessage(session);
		 
		 try {
			 
			m.setFrom(from);
			
			m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			
			m.setSubject(subject);
			
			m.setText(message);
			
			Transport.send(m);
			
			System.out.println("Sent Successfully");
			f = true;
			
			
			
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return f;
	}

}
