package com.shop.JewleryMS.service;
import com.shop.JewleryMS.entity.Account;
import com.shop.JewleryMS.model.EmailDetail;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
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

    public String sendTempPassword(Account account){
        String emailAddressTo = account.getEmail();
        String userName = "John Doe";
        String temporaryPassword = generateTempEmail(12);
        String companyName = "JewelryManagementSystem";


        String resetPasswordEmail =
                "Dear " + account.getAccountName() + ",<br>" +
                        "We have received a request to reset the password for your account. Please find your new temporary password below:<br>" +
                        "Temporary Password: " + temporaryPassword + "<br>" +
                        "Use this temporary password to log in and set a new, secure password.<br>" +
                        "If you did not request a password reset, please disregard this email. Your account will remain secure.";


        EmailDetail emailDetail = new EmailDetail();
        emailDetail.setRecipient(emailAddressTo);
        emailDetail.setSubject("Subject: Password Reset Request");
        emailDetail.setMsgBody(resetPasswordEmail);

        sendMailTemplate(emailDetail);
        return temporaryPassword;

    }
    public void sendMailTemplate(EmailDetail emailDetail) {
        try {
            Context context = new Context();
            // Set the content of the email

            context.setVariable("name", emailDetail.getRecipient());
            context.setVariable("content", emailDetail.getMsgBody());

            String text = templateEngine.process("emailtemplate", context);

            // Creating a simple mail message
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

            // Setting up necessary details
            mimeMessageHelper.setFrom("jewelryms44@gmail.com");
            mimeMessageHelper.setTo(emailDetail.getRecipient());
            mimeMessageHelper.setText(text, true);
            mimeMessageHelper.setSubject(emailDetail.getSubject());
            javaMailSender.send(mimeMessage);
        } catch (MessagingException messagingException) {
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
