package com.shop.JewleryMS.service;
import com.shop.JewleryMS.entity.Account;
import com.shop.JewleryMS.model.EmailDetail;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import mail.dto.EmailDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import javax.naming.Context;
import java.util.Random;

@Service
public class EmailService {

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private JavaMailSender javaMailSender;

    private static final String UPCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMBER = "0123456789";
    private static final Random RANDOM = new Random();
    @Autowired
    MailSender mailSender;

    public void sendTempPassword(Account account){
        String emailAddressTo = account.getEmail();
        String userName = "John Doe";
        String temporaryPassword = generateTempEmail(12);
        String companyName = "JewelryManagementSystem";

        String resetPasswordEmail = "Subject: Password Reset Request\n\n" +
                "Dear " + account.getAccountName() + ",\n\n" +
                "We have received a request to reset the password for your account. Please find your new temporary password below:\n\n" +
                "Temporary Password: " + temporaryPassword + "\n\n" +
                "Use this temporary password to log in and set a new, secure password.\n\n" +
                "If you did not request a password reset, please disregard this email. Your account will remain secure.\n\n" +
                "Thank you for your attention.\n\n" +
                "Best regards,\n" +
                companyName + " Support Team";



        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(emailAddressTo);
        mailMessage.setSubject("Subject: Password Reset Request");
        mailMessage.setText(resetPasswordEmail);
        mailSender.send(mailMessage);


    }
    public void sendMailTemplate(EmailDetail emailDetail){
        try{
            Context context = new Context();

            context.setVariable("name", "Gia Bảo");

            String text = templateEngine.process("emailtemplate", context);

            // Creating a simple mail message
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

            // Setting up necessary details
            mimeMessageHelper.setFrom("admin@gmail.com");
            mimeMessageHelper.setTo(emailDetail.getRecipient());
            mimeMessageHelper.setText(text, true);
            mimeMessageHelper.setSubject(emailDetail.getSubject());
            javaMailSender.send(mimeMessage);
        }catch (MessagingException messagingException){
            messagingException.printStackTrace();
        }
    }
    public String generateTempEmail(int length){
        StringBuilder randomString = new StringBuilder(length);
        for (int i = 0; i < length/3; i++) {
            int index = RANDOM.nextInt(UPCASE.length());
            randomString.append(UPCASE.charAt(index));
        }
        for (int i = length/3; i < length*2/3; i++) {
            int index = RANDOM.nextInt(LOWERCASE.length());
            randomString.append(LOWERCASE.charAt(index));
        }
        for (int i = length*2/3; i < length; i++) {
            int index = RANDOM.nextInt(NUMBER.length());
            randomString.append(NUMBER.charAt(index));
        }
        return randomString.toString();
    }

    public boolean vallidEmail(String email){

        return false;
    }

}
