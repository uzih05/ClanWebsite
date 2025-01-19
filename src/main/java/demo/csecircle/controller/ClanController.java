package demo.csecircle.controller;

import demo.csecircle.UserConst;
import demo.csecircle.classification.ClanRecruit;
import demo.csecircle.controller.form.ClanForm;
import demo.csecircle.controller.form.ClanSearchForm;
import demo.csecircle.domain.Clan;
import demo.csecircle.domain.Member;
import demo.csecircle.service.ClanService;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
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
        if (clan.getImage() != null) {
            String base64Image = Base64.getEncoder().encodeToString(clan.getImage());
            model.addAttribute("base64Image", base64Image);
        }
        if (clan.getDocument() != null) {
            String base64Document = Base64.getEncoder().encodeToString(clan.getDocument());
            model.addAttribute("base64Document", base64Document);
        }
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
    public String clanSave(@ModelAttribute ClanForm clanForm, @RequestParam("image") MultipartFile image,
                           @RequestParam("document") MultipartFile document) throws IOException {
        Clan clan = new Clan();
        clan.setClanName(clanForm.getClanName());
        clan.setLeaderName(clanForm.getLeaderName());
        clan.setClanLocation(clanForm.getClanLocation());
        clan.setTelNum(clanForm.getTelNum());
        clan.setMeetingTime(clanForm.getMeetingTime());
        clan.setDescription(clanForm.getDescription());
        clan.setIsRecruit(ClanRecruit.NO);
        // 이미지 파일을 byte[] 배열로 변환 후 Clan 객체에 저장
        if (!image.isEmpty()) {
            byte[] imageBytes = image.getBytes();
            clan.setImage(imageBytes);
        }

        // 문서 파일을 byte[] 배열로 변환 후 Clan 객체에 저장
        if (!document.isEmpty()) {
            byte[] documentBytes = document.getBytes();
            clan.setDocument(documentBytes);
        }
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




    @GetMapping("/clans/downloadDocument/{clanId}")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable Long clanId) {
        Clan clan = clanService.findClanById(clanId);
        byte[] documentBytes = clan.getDocument();

        if (documentBytes != null && documentBytes.length > 0) {
            // 다운로드할 파일명을 .docx로 설정
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"document.docx\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)  // MIME 타입을 octet-stream으로 설정 (기본적인 바이너리 데이터)
                    .body(documentBytes);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }





}
