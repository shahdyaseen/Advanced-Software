package com.example.Rental.Services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    public void sendLoginNotification(String email,String type) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        if(type.equals("success")){
            message.setSubject("Successful Login Notification");
            message.setText("We’re confirming that a successful login to your account was just made. If this was you, no further action is required.\n" +
                    "\n" +
                    "If you did not initiate this login or have any concerns about your account’s security, please update your password immediately.\n" +
                    "\n" +
                    "Thank you for staying secure with us!");

        }
        else if(type.equals("warning")){
            message.setSubject("Unsuccessful Login Attempt on Your Account");
            message.setText("We detected an unsuccessful login attempt on your account. If this was you, please double-check your credentials and try again. \n" +
                    "\n" +
                    "If you suspect any unusual activity or think someone else might be attempting to access your account, please consider updating your password as a precaution.\n" +
                    "\n" +
                    "Thank you for helping us keep your account secure.");
        }

        emailSender.send(message);
    }


    public void sendUpdateNotification(String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Notification of Successful Profile Update");
        message.setText("We’re confirming that your profile was successfully updated. If these changes were made by you, no further action is required.\n" +
                "\n" +
                "If you did not make these changes or have any concerns about your account’s security, please review your account settings or update your password immediately.\n" +
                "\n" +
                "Thank you for keeping your profile up-to-date with us!");
        emailSender.send(message);
    }


}
