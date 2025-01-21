package demo.csecircle.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;
    private final Map<String, String> codeStorage = new HashMap<>(); // 이메일-인증번호 저장소

    // 6자리 인증번호 생성
    public String generateVerificationCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }

    // 인증번호 이메일로 전송
    public void sendVerificationCode(String toEmail, String verificationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("이메일 인증번호");
        message.setText("안녕하세요. 인증번호는 다음과 같습니다: " + verificationCode);
        mailSender.send(message);
    }

    // 이메일 인증번호 전송 통합 로직
    public void sendVerificationEmail(String toEmail) {
        String verificationCode = generateVerificationCode(); // 인증번호 생성
        sendVerificationCode(toEmail, verificationCode); // 이메일 전송
        codeStorage.put(toEmail, verificationCode); // 메모리에 저장
    }

    // 인증번호 검증
    public boolean verifyCode(String email, String code) {
        String storedCode = codeStorage.get(email);
        return storedCode != null && storedCode.equals(code);
    }

    // 인증번호 삭제
    public void removeCode(String email) {
        codeStorage.remove(email);
    }
}