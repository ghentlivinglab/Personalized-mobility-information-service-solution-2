package controllers;

import backend.AppProperties;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Class for sending the verification email
 */
public class MailThread implements Runnable{
    
    private final String email;
    private final String subject;
    private final String text;
    private final boolean htmlEnabled;
    
    public MailThread(String email, String subject, String text) {
        this(email, subject, text, false);
    }
    
    /**
     *
     * @param email The email of the user to send to
     * @param text The body of the email
     */
    public MailThread (String email, String subject, String text, boolean htmlEnabled) {
        this.email = email;
        this.subject = subject;
        this.text = text;
        this.htmlEnabled = htmlEnabled;
    }
    
    @Override
    public void run() {
        String from = AppProperties.instance().getProp(AppProperties.PROP_KEY.MAIL_SERVER_FROM);
        
        String host = AppProperties.instance().getProp(AppProperties.PROP_KEY.MAIL_SERVER_HOST);
        
        Properties properties = new Properties();
        
        properties.put("mail.smtp.host", host);
        
        Session session = Session.getDefaultInstance(properties);

        try {
            MimeMessage message = new MimeMessage(session);
            
            message.setFrom(new InternetAddress(from));
            
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            
            message.setSubject(subject);
            
            if(htmlEnabled) {
                message.setContent(text,  "text/html; charset=utf-8");
            } else {
                message.setText(text);
            }
            Transport.send(message);
        } catch (MessagingException ex) {
            ex.printStackTrace();
        } 
        
        
    }
    
}
