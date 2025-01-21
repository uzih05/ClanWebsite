package demo.csecircle;

import demo.csecircle.classification.Major;
import demo.csecircle.domain.Clan;
import demo.csecircle.domain.Member;
import demo.csecircle.repository.ClanRepository;
import demo.csecircle.repository.MemberRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootApplication
public class CseCircleApplication {

    private final JavaMailSender mailSender;

    public CseCircleApplication(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public static void main(String[] args) {
        SpringApplication.run(CseCircleApplication.class, args);
    }

//    @EventListener(ApplicationReadyEvent.class)
//    public void insertData() {
//
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom("557987@jj.ac.kr");
//        message.setTo("557987@naver.com");
//        message.setSubject("Test Subject");
//        message.setText("Test Message");
//
//        mailSender.send(message);
//    }

}
