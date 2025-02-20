package clientEmail.controller;

import clientEmail.dto.EmailRequest;
import clientEmail.service.ClientEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client-api/email")
public class ClientEmailController {

    @Autowired
    private ClientEmailService clientEmailService;

    @PostMapping("/send")
    public void sendEmail(@RequestBody EmailRequest emailRequest){

        clientEmailService.sendEmail(emailRequest);
    }


}
