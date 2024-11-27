package com.quizapp.quizApp.services;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${spring.sendgrid.api-key}")
    private String sendGridApiKey;

    public boolean sendEmail(String toEmail, String subject, String body, String replyToEmail) {
        try {
            // Create email components
            Email from = new Email("dholakiaharshil7@gmail.com");
            Email to = new Email(toEmail);
            Content content = new Content("text/plain", body);

            // Create the mail object
            Mail mail = new Mail(from, subject, to, content);

            // Set the reply-to address
            if (replyToEmail != null && !replyToEmail.isEmpty()) {
                Email replyTo = new Email(replyToEmail);
                mail.setReplyTo(replyTo);
            }

            // Send the email
            SendGrid sendGrid = new SendGrid(sendGridApiKey);
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sendGrid.api(request);

            System.out.println("Response Status: " + response.getStatusCode());
            return response.getStatusCode() == 202;
        } catch (Exception e) {
            System.err.println("Error sending email: " + e.getMessage());
            return false;
        }
    }
}
