package com.medicin.demo.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
@Configuration
public class MailConfig {
    @Bean
    public JavaMailSender javaMailSender() {
        // Fallback sender bean so application can start
        // even when SMTP properties are not configured yet.
        return new JavaMailSenderImpl();
    }
}
