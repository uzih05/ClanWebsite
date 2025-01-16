package demo.csecircle.service;

import demo.csecircle.classification.Major;
import demo.csecircle.controller.form.ClanSearchForm;
import demo.csecircle.domain.Clan;
import demo.csecircle.domain.Member;
import demo.csecircle.repository.ClanRepository;
import demo.csecircle.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class ClanServiceTest {

    @Autowired
    private ClanRepository clanRepository;

    @Autowired
    private MemberRepository memberRepository;


    @Autowired
    private ClanService clanService;

    @Test
    public void clan() {
        Member member = new Member();
        member.setName("tester");
        member.setLoginId("abc");
        member.setPassword("abc");
        member.setEmail("tester@school.com");
        member.setMajor(Major.CSE);
        member.setSex(1); // 성별 (여자)
        member.setGrade(3); // 학년
        memberRepository.save(member);

        // 동아리 생성
        Clan clan = new Clan();
        clan.setClanName("ICBM");
        clan.setLeaderName("tester");
        clanRepository.save(clan);

        // 동아리와 멤버 관계 설정
        clan.getMembers().add(member); // 클랜에 멤버 추가
        member.setClan(clan);           // 멤버에 클랜 설정

        // 양방향 관계 저장
        clanRepository.save(clan);      // 클랜 업데이트 (멤버와 연관 관계를 포함한)
        memberRepository.save(member);  // 멤버 업데이트 (클랜 정보 포함)

        Assertions.assertThat(member.getClan()).isEqualTo(clan);
        List<Clan> clans = clanService.getMemberClanList(member);
        for (Clan clan1 : clans) {
            System.out.println("clan1 = " + clan1);
        }
    }

    @Test
    void search(){
        Clan clan = new Clan();
        clan.setClanName("ICBM");
        clanRepository.save(clan);

        ClanSearchForm form = new ClanSearchForm();
        form.setClanName("ICBM");
        List<Clan> foundClan = clanService.findBySearch(form);
        Assertions.assertThat(foundClan).hasSize(1);

    }

}