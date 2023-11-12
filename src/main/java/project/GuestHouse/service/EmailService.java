package project.GuestHouse.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import project.GuestHouse.util.RedisUtil;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Random;


@Service
@PropertySource("classpath:application.yml")
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final RedisUtil redisUtil;

    //인증번호 생성
    private String ePw;

    @Value("${spring.mail.username}")
    private String id;

    public MimeMessage createMessage(String to)throws MessagingException, UnsupportedEncodingException {
        ePw = createCode();

        log.info("보내는 대상 : "+ to);
        MimeMessage  message = javaMailSender.createMimeMessage();

        message.addRecipients(MimeMessage.RecipientType.TO, to); // to: 보내는 대상
        message.setSubject("[in-vit] 회원가입 인증 코드: "); // 메일 제목

        // 메일 내용 메일의 subtype을 html로 지정하여 html문법 사용 가능
        String msg="";
        msg += "<br>";
        msg += "<h1 style=\"font-size: 30px; padding-right: 30px; padding-left: 15px;\">이메일 주소 확인</h1>";
        msg += "<p style=\"font-size: 16px; padding-right: 15px; padding-left: 15px;\">아래 확인 코드를 인증번호 확인란에 입력해주세요.</p>";
        msg += "<div style=\"padding-right: 60px; padding-left: 60px; margin: 32px 0 40px;\"><table style=\"border-collapse: collapse; border: 0; background-color: #F4F4F4; height: 70px; table-layout: fixed; word-wrap: break-word; border-radius: 10px;\"><tbody><tr><td style=\"text-align: center; vertical-align: middle; font-size: 30px;\">";
        msg += ePw;
        msg += "</td></tr></tbody></table></div>";

        message.setText(msg, "utf-8", "html"); // 내용, charset타입, subtype
        message.setFrom(new InternetAddress(id,"in-vit_admin")); // 보내는 사람의 메일 주소, 보내는 사람 이름

        return message;
    }

    // 인증코드 생성
    public static String createCode() {
        StringBuffer code = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 6; i++) { // 인증번호 6자리
            code.append((rnd.nextInt(10)));
        }

        return code.toString();
    }


    /*
        메일 발송
        sendSimpleMessage의 매개변수로 들어온 to는 인증번호를 받을 메일주소
        MimeMessage 객체 안에 내가 전송할 메일의 내용을 담아준다.
        bean으로 등록해둔 javaMailSender 객체를 사용하여 이메일 send
     */
    public String sendSimpleMessage(String to)throws Exception {
        MimeMessage message = createMessage(to);
        try{
            redisUtil.setDataExpire(to, ePw, 60 * 10L); // 유효시간 10분 설정
            javaMailSender.send(message); // 메일 발송
        }catch(MailException e){
            e.printStackTrace();
            throw new IllegalArgumentException();
        }

        log.info("인증번호: " + ePw);
        return ePw; // 메일로 보냈던 인증 코드를 서버로 리턴
    }

    public Boolean verifyEmail(String key, String code) throws ChangeSetPersister.NotFoundException {
        String correctCode = redisUtil.getData(key);

        if (correctCode == null) {
            throw new ChangeSetPersister.NotFoundException();
        } else if (correctCode.equals(code)) {
            return true;
        }

        return false;
    }

    public void deleteEmail(String key) {
        redisUtil.deleteData(key);
    }
}
