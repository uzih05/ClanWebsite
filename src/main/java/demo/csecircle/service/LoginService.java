package demo.csecircle.service;

import demo.csecircle.domain.Member;
import demo.csecircle.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    public Member login(String loginId, String password) {
        Member member = memberRepository.findByLoginId(loginId);
        if (member == null) {
            return null;
        }
        if (member.getPassword().equals(password)) {
            return member;
        }
        return null;
    }

    public void register(Member member) {
        memberRepository.save(member);
    }


}
