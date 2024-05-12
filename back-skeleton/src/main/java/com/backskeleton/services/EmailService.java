package com.backskeleton.services;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.backskeleton.models.User;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;


@Service
public class EmailService {
    
    // Méthode pour envoyer un e-mail de remerciement
    public void sendThankYouEmail(User user) {
        // String recipientAddress = user.getEmail();
        // String subject = "Merci pour vos avis !";
        // String message = "Bonjour " + user.getFirstName() + ",\n\nMerci d'avoir partagé vos avis avec nous. Nous apprécions votre contribution à notre plateforme.\n\nCordialement,\nL'équipe de notre plateforme.";

        // MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        // MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        // try {
        //     helper.setTo(recipientAddress);
        //     helper.setSubject(subject);
        //     helper.setText(message);
        //     javaMailSender.send(mimeMessage);
        // } catch (MessagingException e) {
        //     e.printStackTrace();
        // }
    }
}
