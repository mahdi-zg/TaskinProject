package devteam.tn.taskin.Services;

import com.sun.mail.smtp.SMTPTransport;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

import static devteam.tn.taskin.Constant.EmailConstant.*;
import static devteam.tn.taskin.Constant.FileConstant.TEMP_PROFILE_IMAGE_BASE_URL;


@Service
public class EmailService {
    public void sendNewPasswordEmail(String firstName, String password, String email) throws MessagingException {
        Message message = createEmail(firstName, password, email);
        SMTPTransport smtpTransport = (SMTPTransport) getEmailSession().getTransport(SIMPLE_MAIL_TRANSFER_PROTOCOL);
        smtpTransport.connect(GMAIL_SMTP_SERVER, USERNAME, PASSWORD);
        smtpTransport.sendMessage(message, message.getAllRecipients());
        smtpTransport.close();
    }

    private MimeMessage createEmail(String firstName, String password, String email) throws MessagingException {
        MimeMessage message = new MimeMessage(getEmailSession());
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        // Email Header
        helper.setFrom(new InternetAddress(FROM_EMAIL));
        helper.setTo(email);
        helper.setSubject(EMAIL_SUBJECT);

        // Email Body
        String profileImageUrl = TEMP_PROFILE_IMAGE_BASE_URL + firstName;

        // Email Styling
        String header = "#003366";
        String bgColor = "#f2f2f2";
        String textColor = "#333333";
        String linkColor = "#ffffff";
        String buttonColor = "#003366";

        // Email Content
        String html = "<html>" +
                "<head>" +
                "<style>" +
                "body { background-color: " + bgColor + "; }" +
                "h1, h2, p { color: " + textColor + "; font-weight: bold; }" +
                "a { color: " + linkColor + "; }" +
                ".button { background-color: " + buttonColor + "; color: " + linkColor + "; text-decoration: none; padding: 10px 20px; border-radius: 5px; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div style='background-color: #ffffff; padding: 20px;'>" +
                "<table width='100%' border='0' cellspacing='0' cellpadding='0' style='border-collapse: collapse;'>" +
                "<tr>" +
                "<td style='background-color: " + header + "; padding: 20px;'>" +
                "<h3 style='margin: 0;color:#FFFFFF; font-size: 36px;'>Bienvenue, " + firstName + " !</h3>" +
                "</td>" +
                "</tr>" +
                "<tr>" +
                "<td style='padding: 20px;'>" +
                "<p>Votre mot de passe a été créé avec succès. Vous pouvez désormais vous connecter à votre compte :</p>" +
                "<p style='text-align: center;'>" +
                "<a href='http://localhost:8081/user/login' class='button'>Connectez-vous maintenant</a>" +
                "</p>" +
                "<p>Voici votre mot de passe :</p>" +
                "<h2 style='font-size: 28px;'>" + password + "</h2>" +
                "<p>Conservez-le précieusement.</p>" +
                "<p>Voici votre photo de profil :</p>" +
                "<img src='" + profileImageUrl + "' alt='Photo de profil de " + firstName + "' />" +
                "<p>Merci de faire confiance à notre entreprise.</p>" +
                "</td>" +
                "</tr>" +
                "</table>" +
                "</div>" +
                "</body>" +
                "</html>";

        // Email Footer
        html += "<div style='background-color: #f2f2f2; padding: 20px; text-align: center;'>" +
                "<p style='color: #777777; margin: 0;'>Ceci est un email automatique, merci de ne pas y répondre.</p>" +
                "</div>";

        // Email Finalization
        helper.setText(html, true);
        helper.setSentDate(new Date());

        return helper.getMimeMessage();
    }



    private Session getEmailSession() {
        Properties properties = System.getProperties();
        properties.put(SMTP_HOST, GMAIL_SMTP_SERVER);
        properties.put(SMTP_AUTH, true);
        properties.put(SMTP_PORT, DEFAULT_PORT);
        properties.put(SMTP_STARTTLS_ENABLE, true);
        properties.put(SMTP_STARTTLS_REQUIRED, true);
        return Session.getInstance(properties, null);
    }
}
