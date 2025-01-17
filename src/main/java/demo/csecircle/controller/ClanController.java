package demo.csecircle.controller;

import demo.csecircle.UserConst;
import demo.csecircle.classification.ClanRecruit;
import demo.csecircle.controller.form.ClanForm;
import demo.csecircle.controller.form.ClanSearchForm;
import demo.csecircle.domain.Clan;
import demo.csecircle.domain.Member;
import demo.csecircle.service.ClanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ClanController {

    private final ClanService clanService;

    @GetMapping("/clans")
    public String clans(Model model, @RequestParam(defaultValue = "") String clanName){
        ClanSearchForm form = new ClanSearchForm();
        form.setClanName(clanName);
        List<Clan> clanList = clanService.findBySearch(form);
        model.addAttribute("clanList", clanList);
        return "clan/clans";
    }

    @GetMapping("/clans/{clanId}")
    public String clan(@PathVariable Long clanId, Model model, @SessionAttribute(name = UserConst.LOGIN_MEMBER,required = false) Member member){
        Clan clan = clanService.findClanById(clanId);
        model.addAttribute("clan", clan);
        model.addAttribute("member", member);
        return "clan/clan";
    }

    @GetMapping("/clans/clanSave")
    public String clanSave(@ModelAttribute ClanForm clanForm, Model model, @SessionAttribute(name = UserConst.LOGIN_MEMBER,required = false) Member member){
        model.addAttribute("member",member);
        model.addAttribute("clanForm", clanForm);
        return "clan/clanSave";
    }

    @PostMapping("/clans/clanSave")
    public String clanSave(@ModelAttribute ClanForm clanForm){
        Clan clan = new Clan();
        clan.setClanName(clanForm.getClanName());
        clan.setLeaderName(clanForm.getLeaderName());
        clan.setClanLocation(clanForm.getClanLocation());
        clan.setTelNum(clanForm.getTelNum());
        clan.setMeetingTime(clanForm.getMeetingTime());
        clan.setDescription(clanForm.getDescription());
        clan.setIsRecruit(ClanRecruit.NO);
        clanService.saveClan(clan);
        return "redirect:/clans";
    }

    @GetMapping("/clans/signup/{clanId}")
    public String signup(@PathVariable Long clanId, Model model){
        Clan clan = clanService.findClanById(clanId);
        model.addAttribute("clan", clan);
        return "clan/signup";
    }

    @PostMapping("/clans/signup/{clanId}")
    public String postSignup(@PathVariable Long clanId, @ModelAttribute ClanForm clanForm,
                             @SessionAttribute(name = UserConst.LOGIN_MEMBER,required = false) Member member){
        Clan clan = clanService.findClanById(clanId);
        clanService.signupClan(clan,member);
        return "redirect:/clans";
    }



}
