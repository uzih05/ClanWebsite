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






}
