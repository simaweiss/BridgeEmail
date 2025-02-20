package serverEmail.service;

import serverEmail.configuration.EmailConfiguration;
import jakarta.mail.*;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private EmailConfigurationFactory emailConfigurationFactory;

    public void sendEmail(String fromEmail, String toEmail, String body) throws MessagingException, UnsupportedEncodingException {
        logger.info("Preparing to send email from {} to {}", fromEmail, toEmail);

        // Validate The email
        validateEmail(fromEmail);
        // Extract the vendor from the fromEmail
        String emailVendor = extractEmailVendor(fromEmail);

        // Use the factory to validate email format and get the appropriate email configuration
        EmailConfiguration config = emailConfigurationFactory.getEmailConfiguration(emailVendor);
        logger.info("Using {} configuration for sending email", emailVendor);

        // Set up email properties
        Properties props = getProperties(config);

        //create Authenticator object to pass in Session.getInstance argument
        Authenticator auth = getAuthenticator(config);

        // Create a session
        Session session = Session.getInstance(props, auth);

        //create mime message
        MimeMessage msg = createMimeMessage(fromEmail, toEmail, body, config, session);

        logger.info("Email message created successfully.");

        Transport.send(msg);
    }

    private MimeMessage createMimeMessage(String fromEmail, String toEmail, String body, EmailConfiguration config, Session session) throws MessagingException, UnsupportedEncodingException {
        MimeMessage msg = new MimeMessageBuilder(session)
                .addHeaders()
                .from(config.getUsername(), fromEmail)
                .toRecipients(toEmail)
                .replayTo(fromEmail)
                .body(body)
                .withSentDate()
                .build();
        return msg;
    }

    private Authenticator getAuthenticator(EmailConfiguration config) {
        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(config.getUsername(), config.getPassword());
            }
        };
        return auth;
    }

    private Properties getProperties(EmailConfiguration config) {
        Properties props = new Properties();
        props.put("mail.smtp.host", config.getHost());
        props.put("mail.smtp.port", config.getPort());
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.debug", "true");
        return props;
    }

    // Method to validate email format
    private void validateEmail(String email) {
        logger.info("Validating email format: {}", email);
        String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            logger.error("Invalid email format: {}", email);
            throw new IllegalArgumentException("Invalid email format: " + email);
        }
    }

    // Method to extract the email vendor from the domain
    private String extractEmailVendor(String email) {
        String vendor = email.substring(email.indexOf("@") + 1).toLowerCase();
        logger.info("Extracting vendor from email: {}", email);
        return vendor;
    }
}
