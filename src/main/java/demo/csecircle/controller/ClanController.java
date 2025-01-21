package demo.csecircle.controller;

import demo.csecircle.UserConst;
import demo.csecircle.classification.ClanRecruit;
import demo.csecircle.classification.MemberRole;
import demo.csecircle.controller.form.ClanForm;
import demo.csecircle.controller.form.ClanSearchForm;
import demo.csecircle.domain.Clan;
import demo.csecircle.domain.Member;
import demo.csecircle.service.ClanService;
import demo.csecircle.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ClanController {

    private final ClanService clanService;
    private final MemberService memberService;

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
        Member memberById = memberService.findMemberById(member.getId());
        boolean isPresident = memberById.getMemberRole().equals(MemberRole.CLUB_PRESIDENT);
        boolean isAdmin = memberById.getMemberRole().equals(MemberRole.WEBSITE_ADMIN);
        if(isPresident || isAdmin){
            model.addAttribute("isAdmin", true);
        }
        Clan clan = clanService.findClanById(clanId);

        if(clan.getIsRecruit().equals(ClanRecruit.YES) && memberById.getSignupClan() == null
                && !clan.getSignupMembers().contains(memberById) && memberById.getClan() == null){
            model.addAttribute("isRecruit", true);
        }
        if (clan.getImage() != null) {
            String base64Image = Base64.getEncoder().encodeToString(clan.getImage());
            model.addAttribute("base64Image", base64Image);
        }
        if (clan.getDocument() != null) {
            String base64Document = Base64.getEncoder().encodeToString(clan.getDocument());
            model.addAttribute("base64Document", base64Document);
        }
        model.addAttribute("clan", clan);
        model.addAttribute("member", memberById);
        return "clan/clan";
    }

    @GetMapping("/clans/clanSave")
    public String clanSave(@ModelAttribute ClanForm clanForm, Model model, @SessionAttribute(name = UserConst.LOGIN_MEMBER, required = false) Member member) {
        Member memberById = memberService.findMemberById(member.getId());
        if(!memberById.getMemberRole().equals(MemberRole.CLUB_PRESIDENT)){
            return "redirect:/";
        }
        model.addAttribute("member", memberById);
        model.addAttribute("clanForm", clanForm);
        return "clan/clanSave";
    }

    @PostMapping("/clans/clanSave")
    public String clanSave(@ModelAttribute ClanForm clanForm, @RequestParam("image") MultipartFile image,
                           @RequestParam("document") MultipartFile document,@SessionAttribute(name = UserConst.LOGIN_MEMBER,required = false) Member member) throws IOException {
        Clan clan = new Clan();
        clan.setClanName(clanForm.getClanName());
        clan.setLeaderName(member.getName());
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
        clanService.saveClanLeader(clan,member);
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

    @GetMapping("/clans/{clanId}/edit")
    public String edit(@PathVariable Long clanId, Model model,@SessionAttribute(name = UserConst.LOGIN_MEMBER, required = false) Member member){
        Clan clan = clanService.findClanById(clanId);
        boolean isAdmin = member.getMemberRole().equals(MemberRole.CLUB_PRESIDENT);
        model.addAttribute("member", member);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("clanForm", makeEditClanForm(clan));
        model.addAttribute("clan", clan);
        return "clan/clanEdit";
    }

    public ClanForm makeEditClanForm(Clan clan){
        ClanForm clanForm = new ClanForm();
        clanForm.setClanName(clan.getClanName());
        clanForm.setLeaderName(clan.getLeaderName());
        clanForm.setClanLocation(clan.getClanLocation());
        clanForm.setTelNum(clan.getTelNum());
        clanForm.setMeetingTime(clan.getMeetingTime());
        clanForm.setDescription(clan.getDescription());
        clanForm.setId(clan.getId());
        return clanForm;
    }

    @PostMapping("/clans/{clanId}/edit")
    public String editPost(@PathVariable Long clanId, @ModelAttribute ClanForm clanForm, @RequestParam("image") MultipartFile image,
                           @RequestParam("document") MultipartFile document,
                           Model model) throws IOException {

        Clan clan = clanService.findClanById(clanId);
        clan.setClanName(clanForm.getClanName());
        clan.setLeaderName(clanForm.getLeaderName());
        clan.setClanLocation(clanForm.getClanLocation());
        clan.setTelNum(clanForm.getTelNum());
        clan.setMeetingTime(clanForm.getMeetingTime());
        clan.setDescription(clanForm.getDescription());
        // 이미지가 있으면 업데이트
        if (image != null && !image.isEmpty()) {
            byte[] imageBytes = image.getBytes();
            clan.setImage(imageBytes);
        }

        // 문서가 있으면 업데이트
        if (document != null && !document.isEmpty()) {
            byte[] documentBytes = document.getBytes();
            clan.setDocument(documentBytes);
        }
        clanService.saveClan(clan);

        return "redirect:/clans/{clanId}";

    }

    @GetMapping("/clans/{clanId}/manage")
    public String manage(@PathVariable Long clanId, Model model,@SessionAttribute(name = UserConst.LOGIN_MEMBER, required = false) Member member){
        Clan clan = clanService.findClanById(clanId);
        Member memberById = memberService.findMemberById(member.getId());
        model.addAttribute("member", memberById);

        if (clan.getImage() != null) {
            String base64Image = Base64.getEncoder().encodeToString(clan.getImage());
            model.addAttribute("base64Image", base64Image);
        }

        if (member.getProfileImage() != null) {
            String base64Image = Base64.getEncoder().encodeToString(member.getProfileImage());
            model.addAttribute("base64ImageM", base64Image);
        }
        boolean isRecruit = clan.getIsRecruit().equals(ClanRecruit.YES);
        model.addAttribute("isRecruit", isRecruit);
        model.addAttribute("clan", clan);
        return "clan/clanManage";
    }

    @PostMapping("/clans/{clanId}/recruitChange")
    public String changeRecruit(@PathVariable Long clanId,@SessionAttribute(name = UserConst.LOGIN_MEMBER, required = false) Member member){
        if(member.getMemberRole().equals(MemberRole.CLUB_PRESIDENT) || member.getMemberRole().equals(MemberRole.WEBSITE_ADMIN)){
            Clan clan = clanService.findClanById(clanId);
            if(clan.getIsRecruit().equals(ClanRecruit.YES)){
                clan.setIsRecruit(ClanRecruit.NO);
            }
            else if(clan.getIsRecruit().equals(ClanRecruit.NO)){
                clan.setIsRecruit(ClanRecruit.YES);
            }
            clanService.saveClan(clan);
            return "redirect:/clans/{clanId}/manage";
        }
        return "redirect:/clans/{clanId}/manage";
    }


    @PostMapping("/clans/{clanId}/signupMember/{applicantId}/approve")
    @ResponseBody
    public ResponseEntity<String> approveApplicant(@PathVariable Long clanId, @PathVariable Long applicantId) {
        Clan clan = clanService.findClanById(clanId);  // 클럽 정보 조회

        if (clan == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Clan not found");
        }

        Member applicant = clan.getSignupMembers().stream()
                .filter(member -> member.getId().equals(applicantId))
                .findFirst()
                .orElse(null);

        if (applicant == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Applicant not found");
        }

        // 승인 처리 로직
        clanService.acceptMember(clan,applicant);
        log.info("accepted applicant " + applicantId);

        return ResponseEntity.ok("Applicant approved");
    }

    @PostMapping("/clans/{clanId}/signupMember/{applicantId}/reject")
    @ResponseBody
    public ResponseEntity<String> rejectApplicant(@PathVariable Long clanId, @PathVariable Long applicantId) {
        Clan clan = clanService.findClanById(clanId);  // 클럽 정보 조회

        if (clan == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Clan not found");
        }

        Member applicant = clan.getSignupMembers().stream()
                .filter(member -> member.getId().equals(applicantId))
                .findFirst()
                .orElse(null);

        if (applicant == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Applicant not found");
        }

        // 거부 처리 로직
        clanService.rejectMember(clan,applicant);
        log.info("rejected applicant " + applicantId);
        log.info("clan Name" + clan.getClanName());
        log.info("applicant " + applicant.getName());
        return ResponseEntity.ok("Applicant rejected");
    }






}
