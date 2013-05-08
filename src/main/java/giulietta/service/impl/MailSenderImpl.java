package giulietta.service.impl;
 
import giulietta.service.api.MailSender;

import java.io.File;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
 
public class MailSenderImpl implements MailSender {
 
	@Override
	public void sendMessage(String to,File filePath) {
 
		final String username = "dufrannea@gmail.com";
		final String password = "Raht9eep";
 
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
 
		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });
 
		try {
			
 
//			Message message = new MimeMessage(session);
//			message.setFrom(new InternetAddress("dufrannea@gmail.com"));
//			message.setRecipients(Message.RecipientType.TO,
//				InternetAddress.parse("dufrannea@gmail.com"));
//			message.setSubject("Testing Subject");
//			message.setText("Dear Mail Crawler,"
//				+ "\n\n No spam to my email, please!");
			Message message = sendMessage(session,to,filePath);
			Transport.send(message);
 
			System.out.println("Done");
 
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
	
	private Message sendMessage(Session session,String to,File filePath) throws AddressException, MessagingException{
		// Define message
	    MimeMessage message = 
	      new MimeMessage(session);
	    message.setFrom(
	      new InternetAddress("dufrannea@gmail.com"));
	    message.addRecipient(
	      Message.RecipientType.TO, 
	      new InternetAddress(to));
	    message.setSubject(
	      "Completed session");

	    // create the message part 
	    MimeBodyPart messageBodyPart = 
	      new MimeBodyPart();

	    //fill message
	    messageBodyPart.setText("A new session is available");

	    Multipart multipart = new MimeMultipart();
	    multipart.addBodyPart(messageBodyPart);

	    // Part two is attachment
	    messageBodyPart = new MimeBodyPart();
	    String fileAttachment = filePath.getAbsolutePath();
	    DataSource source = 
	      new FileDataSource(fileAttachment);
	    messageBodyPart.setDataHandler(
	      new DataHandler(source));
	    messageBodyPart.setFileName(filePath.getName());
	    multipart.addBodyPart(messageBodyPart);
	    
	 // Put parts in message
	    message.setContent(multipart);
		System.out.println("Built message");
		return message;
	}
}