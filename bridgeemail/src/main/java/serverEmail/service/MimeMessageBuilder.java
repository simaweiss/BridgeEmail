package serverEmail.service;

import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.Date;

public class MimeMessageBuilder {
    private static final Logger logger = LoggerFactory.getLogger(MimeMessageBuilder.class);

    private MimeMessage msg;

    public MimeMessageBuilder(Session session) {
        this.msg = new MimeMessage(session);
    }

    public MimeMessageBuilder from(String fromEmailServer, String fromEmailPersonal) throws UnsupportedEncodingException, MessagingException {
        logger.info("Setting From email: {} with personal name: {}", fromEmailServer, fromEmailPersonal);
        msg.setFrom(new InternetAddress(fromEmailServer, fromEmailPersonal));
        return this;
    }

    public MimeMessageBuilder toRecipients(String toEmail) throws MessagingException {
        logger.info("Setting To email: {}", toEmail);
        msg.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(toEmail, false));
        return this;
    }

    public MimeMessageBuilder replayTo(String toEmail) throws MessagingException {
        logger.info("Setting replay To email: {}", toEmail);
        msg.setReplyTo(InternetAddress.parse(toEmail, false));
        return this;
    }

    public MimeMessageBuilder body(String body) throws MessagingException {
        logger.info("Setting email body");
        msg.setText(body, "UTF-8");
        return this;
    }

    public MimeMessageBuilder withSentDate() throws MessagingException {
        logger.info("Setting sent date");
        msg.setSentDate(new Date());
        return this;
    }

    public MimeMessageBuilder addHeaders() throws MessagingException {
        logger.info("Adding headers to the email");
        msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
        msg.addHeader("format", "flowed");
        msg.addHeader("Content-Transfer-Encoding", "8bit");
        return this;
    }

    public MimeMessage build() {
        logger.info("MimeMessage building completed.");
        return msg;
    }
}
