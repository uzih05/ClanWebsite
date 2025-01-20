package demo.csecircle.service;

import demo.csecircle.controller.form.ClanSearchForm;
import demo.csecircle.domain.Clan;
import demo.csecircle.domain.Member;
import demo.csecircle.repository.ClanRepository;
import demo.csecircle.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClanService {

    private final ClanRepository clanRepository;
    private final MemberRepository memberRepository;

    public List<Clan> getMemberClanList(Member member){
        return clanRepository.findClanByMembersId(member.getId());
    }

    public List<Clan> getAllClans(){
        return clanRepository.findAll();
    }

    public List<Clan> findBySearch(ClanSearchForm form){
        return clanRepository.findClansByNames(form);
    }

    public void saveClan(Clan clan){
        clanRepository.save(clan);
    }

    public Clan findClanById(Long id){
        return clanRepository.findById(id).orElse(null);
    }

    public void signupClan(Clan clan, Member member){
        if(member.getClan()!=null){
            return;
        }
        clan.getSignupMembers().add(member);
        clanRepository.save(clan);
        member.setSignupClan(clan);
        memberRepository.save(member);
    }

    public void acceptMember(Clan clan, Member member){
        clan.getMembers().add(member);
        clan.getSignupMembers().remove(member);
        member.setSignupClan(null);
        member.setClan(clan);
        memberRepository.save(member);
        clanRepository.save(clan);
    }
    public void rejectMember(Clan clan, Member member){
        clan.getSignupMembers().remove(member);
        member.setSignupClan(null);
        memberRepository.save(member);
        clanRepository.save(clan);
    }
    public void deleteMember(Clan clan, Member member){
        clan.getMembers().remove(member);
        member.setClan(null);
        memberRepository.save(member);
        clanRepository.save(clan);
    }








}
