package winterdevcamp.Auth.Service.service;

public interface EmailService {
    void sendMail(String to, String sub, String text);
}
