package demo.csecircle.controller;

import demo.csecircle.controller.form.ClanForm;
import demo.csecircle.controller.form.ClanSearchForm;
import demo.csecircle.domain.Clan;
import demo.csecircle.service.ClanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/clans/clanSave")
    public String clanSave(@ModelAttribute ClanForm clanForm, Model model){
        return "clan/clanSave";
    }

    @PostMapping("/clans/clanSave")
    public String clanSave(@ModelAttribute ClanForm clanForm){
        Clan clan = new Clan();
        clan.setClanName(clanForm.getClanName());
        clan.setLeaderName(clanForm.getLeaderName());
        clanService.saveClan(clan);
        return "redirect:/clans";
    }



}
