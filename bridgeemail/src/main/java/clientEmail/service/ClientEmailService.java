package clientEmail.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import clientEmail.dto.EmailRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ClientEmailService {

    private final RestClient restClient;

    private static final Logger logger = LoggerFactory.getLogger(ClientEmailService.class);
    private static final String SEND_EMAIL_END_POINT = "http://localhost:8082/api/email/send";

    public ClientEmailService() {
        this.restClient = RestClient.builder()
                .build();
    }

    public void sendEmail(EmailRequest emailRequest){

    //    EmailRequest emailRequest = testCreateEmailRequest();
        ResponseEntity<String> response = restClient
                .post()
                .uri(SEND_EMAIL_END_POINT)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(emailRequest)
                .retrieve()
                .toEntity(String.class);

        // Check for a successful response
        if (response.getStatusCode() == HttpStatus.OK) {
            logger.info("Email sent successfully. Response: {}, {}", response.getStatusCode(), response.getBody());
        } else {
            logger.error("Failed to send email. Status code: {}, {}", response.getStatusCode(), response.getBody());
        }

    }

    private EmailRequest testCreateEmailRequest(){
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setFromEmail("test@gmail.com");
        emailRequest.setToEmail("recipient@gmail.com");
        emailRequest.setBody("This is a test email body.");
        return emailRequest;
    }
}
