package client.utils;

import com.google.inject.Inject;
import javafx.scene.control.Alert;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class EmailUtils {
    private final Config config;
    private final Executor executor = Executors.newVirtualThreadPerTaskExecutor();
    private final JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    @Inject
    public EmailUtils(Config config) {
        this.config = config;
        setMetadata();
    }

    public void emailAlert(String address) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Email error");
        alert.setHeaderText("Could not send email through smtp server to " + address);
        alert.setContentText("Please check if the email address exists");
        alert.show();
    }
    public void sendEmail(SimpleMailMessage message) {
        try {
            executor.execute(() -> mailSender.send(message));
        } catch (MailException e) {
            emailAlert(config.getEmail().getUsername());
        }
    }
    public void testEmail() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(config.getEmail().getUsername());
        message.setTo(config.getEmail().getUsername());
        message.setSubject("Mail server test");
        message.setText("If you receive this email, then you have set up your smtp server correctly");
        sendEmail(message);
    }
    public void sendEmailInvite(String address, String code, String name) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(config.getEmail().getUsername());
        message.setCc(config.getEmail().getUsername());
        message.setSubject("Event Invite");
        message.setText("You have been invited to join " + name + " using invite code: " + code);
        message.setTo(address);
        sendEmail(message);
    }

    public void sendDebtReminder(String payerName, String amount, String address) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setCc(config.getEmail().getUsername());
        message.setFrom(config.getEmail().getUsername());
        message.setSubject("Settle your debt to " + payerName);
        message.setText(String.format("""
                You should settle your debt to %s.
                You owe %s.""", payerName, amount));
        message.setTo(address);
        sendEmail(message);
    }

    private void setMetadata() {
        if (config.getEmail() == null)
            return;
        mailSender.setHost(config.getEmail().getHost());
        mailSender.setPort(config.getEmail().getPort());
        mailSender.setUsername(config.getEmail().getUsername());
        mailSender.setPassword(config.getEmail().getPassword());
        Properties props = mailSender.getJavaMailProperties();
        props.putAll(config.getEmail().getProps());
    }

}
