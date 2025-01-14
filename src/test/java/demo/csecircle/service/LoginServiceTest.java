package demo.csecircle.service;

import demo.csecircle.domain.Member;
import demo.csecircle.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class LoginServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private LoginService loginService;

    @BeforeEach
    void setUp() {
        Member member = new Member();
        member.setLoginId("test");
        member.setPassword("test");
        memberRepository.save(member);

    }

    @Test
    @DisplayName("로그인테스트")
    void login(){
        Member member = memberRepository.findByLoginId("test");
        Member member2 = memberRepository.findByLoginId("test2");
        Member foundMember = loginService.login("test", "test");
        Assertions.assertThat(foundMember).isEqualTo(member);
        Assertions.assertThat(foundMember).isNotEqualTo(member2);
    }

}