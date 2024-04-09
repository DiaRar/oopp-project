package client.utils;

import commons.Debt;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

public class EmailUtils {
    private JavaMailSenderImpl mailSender;
    private String address;
    private String code;
    private final int port = 587;
    public EmailUtils(String address, String code) {
        this.address = address;
        this.code = code;
        mailSender = new JavaMailSenderImpl();
    }
    public void sendEmail() {
        setMetadata();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("ooppteam12@gmail.com");
        message.setSubject("Event Invite");
        message.setText(code);
        message.setTo(address);
        mailSender.send(message);
    }

    public void sendDebtReminder(String payerName, String amount) {
        setMetadata();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("ooppteam12@gmail.com");
        message.setSubject("Settle your debt to " + payerName);
        message.setText(String.format("""
                You should settle your debt to %s.
                You owe %s.""", payerName, amount));
        message.setTo(address);
        mailSender.send(message);
    }

    private void setMetadata() {
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(port);
        mailSender.setUsername("ooppteam12@gmail.com");
        mailSender.setPassword("mrvthleczcxjmbrg");
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
    }

}
