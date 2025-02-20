package serverEmail.service;

import serverEmail.configuration.EmailConfiguration;
import serverEmail.configuration.EmailConfigurationGmail;
import serverEmail.configuration.EmailConfigurationWalla;
import serverEmail.configuration.EmailConfigurationYahoo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailConfigurationFactory {

    @Autowired
    private EmailConfigurationGmail gmailConfig;

    @Autowired
    private EmailConfigurationYahoo yahooConfig;

    @Autowired
    private EmailConfigurationWalla wallaConfig;

    public EmailConfiguration getEmailConfiguration(String emailType) {
        switch (emailType.toLowerCase()) {
            case "gmail":
                return gmailConfig;
            case "yahoo":
                return yahooConfig;
            case "walla":
                return wallaConfig;
            default:
                throw new IllegalArgumentException("Unsupported email type: " + emailType);
        }
    }
}
