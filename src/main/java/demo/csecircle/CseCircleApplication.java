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

@SpringBootApplication
public class CseCircleApplication {

    private final MemberRepository memberRepository;
    private final ClanRepository clanRepository;

    public CseCircleApplication(MemberRepository memberRepository, ClanRepository clanRepository) {
        this.memberRepository = memberRepository;
        this.clanRepository = clanRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(CseCircleApplication.class, args);
    }

//    @EventListener(ApplicationReadyEvent.class)
//    public void insertData() {
//        // 멤버 생성
//        Member member = new Member();
//        member.setName("tester");
//        member.setLoginId("abc");
//        member.setPassword("abc");
//        member.setEmail("tester@school.com");
//        member.setMajor(Major.CSE);
//        member.setSex(1); // 성별 (여자)
//        member.setGrade(3); // 학년
//        memberRepository.save(member);
//
//        // 동아리 생성
//        Clan clan = new Clan();
//        clan.setClanName("ICBM");
//        clan.setLeaderName("tester");
//        clanRepository.save(clan);
//
//        // 동아리와 멤버 관계 설정
//        clan.getMembers().add(member); // 클랜에 멤버 추가
//        member.setClan(clan);           // 멤버에 클랜 설정
//
//        // 양방향 관계 저장
//        clanRepository.save(clan);      // 클랜 업데이트 (멤버와 연관 관계를 포함한)
//        memberRepository.save(member);  // 멤버 업데이트 (클랜 정보 포함)
//
//        System.out.println("초기 데이터가 성공적으로 삽입되었습니다.");
//    }

}
