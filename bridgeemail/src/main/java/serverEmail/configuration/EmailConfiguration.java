package serverEmail.configuration;

public interface EmailConfiguration {
   String getHost();
   int getPort();
   String getUsername();
   String getPassword();
}