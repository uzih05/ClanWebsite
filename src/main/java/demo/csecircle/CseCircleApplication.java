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

    @EventListener(ApplicationReadyEvent.class)
    public void insertData() {


//        // 동아리 생성
//        Clan clan = new Clan();
//        clan.setClanName("CSE");
//        clan.setLeaderName("김준영");
//        clan.setDescription("cse 입니다");
//        clan.setClanLocation("공학1관");
//        clan.setMeetingTime("화요일");
//        clan.setTelNum("010-5217-4898");
//        clanRepository.save(clan);
//
//          // 멤버 업데이트 (클랜 정보 포함)

        System.out.println("초기 데이터가 성공적으로 삽입되었습니다.");
    }

}
