package serverEmail.controller;

import serverEmail.dto.EmailRequest;
import serverEmail.service.EmailService;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    private static final Logger logger = LoggerFactory.getLogger(EmailController.class);

    @Autowired
    private EmailService emailService;

    @PostMapping(value = "/send")
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequest emailRequest) {
        try {
            logger.info("Received email request to send from {} to {}", emailRequest.getFromEmail(), emailRequest.getToEmail());
            // Call the sendEmail method in the service
            emailService.sendEmail(emailRequest.getFromEmail(), emailRequest.getToEmail(), emailRequest.getBody());

            // Return success response
            logger.info("Email sent successfully.");
            return ResponseEntity.ok("Email sent successfully.");
        } catch (IllegalArgumentException ex) {
            logger.error("Invalid email request: {}, {}", ex.getMessage(), ex.getStackTrace());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email: " + ex.getMessage());
        } catch (MessagingException | UnsupportedEncodingException ex) {
            logger.error("Error sending email: {}, {}", ex.getMessage(), ex.getStackTrace());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending email: " + ex.getMessage());
        } catch (Exception ex) {
            logger.error("An unexpected error occurred: {}, {}", ex.getMessage(), ex.getStackTrace());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + ex.getMessage());
        }
    }
}
































/*import com.assignment.bridgeemail.model.EmailRequest;
import service.serverEmail.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/bridgeemail")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/get")
    public String testSendEmail() throws MessagingException, UnsupportedEncodingException {

        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setTo("simaweiss7@gmail.com");
        emailRequest.setFrom("simaweiss7@gmail.com");
        emailRequest.setBody("this is the email body");

        emailService.sendEmail(emailRequest);
        return "sent succssefully";
    }

    @PostMapping(value = "/sendemail")
    public ResponseEntity<EmailRequest> sendEmail(@RequestBody EmailRequest emailRequest) throws MessagingException, UnsupportedEncodingException {
        EmailRequest emailRequest1 = emailService.sendEmail(emailRequest);
        return ResponseEntity.ok(emailRequest1);
    }



}*/
