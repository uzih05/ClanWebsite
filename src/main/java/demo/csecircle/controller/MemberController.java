package demo.csecircle.controller;

import demo.csecircle.UserConst;
import demo.csecircle.domain.Clan;
import demo.csecircle.domain.Member;
import demo.csecircle.service.ClanService;
import demo.csecircle.service.LoginService;
import demo.csecircle.service.MailService;
import demo.csecircle.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final LoginService loginService;
    private final ClanService clanService;
    private final MemberService memberService;
    private final MailService mailService;


    @GetMapping("member/register")
    public String registerPage(Model model) {
        Member member =loginService.login("test","TEST");
        model.addAttribute("member",member);
        model.addAttribute("newMember", new Member());
        return "member/register";
    }


    @PostMapping("/send")
    @ResponseBody
    public ResponseEntity<Map<String, String>> sendVerificationEmail(@RequestParam String email) {
        try {
            mailService.sendVerificationEmail(email);
            Map<String, String> response = new HashMap<>();
            response.put("message", "인증번호가 이메일로 전송되었습니다.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "이메일 전송 실패");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/verify")
    @ResponseBody
    public ResponseEntity<Map<String, String>> verifyCode(@RequestParam String email, @RequestParam String code) {
        Map<String, String> response = new HashMap<>();
        boolean isValid = mailService.verifyCode(email, code);
        if (isValid) {
            mailService.removeCode(email);
            response.put("message", "인증 성공");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "인증 실패");
            return ResponseEntity.badRequest().body(response);
        }
    }


    @PostMapping("member/register")
    public String register(@ModelAttribute Member member, @RequestParam("image") MultipartFile image) throws IOException {

        Member registerMember = makeMember(member);
        if (!image.isEmpty()) {
            byte[] imageBytes = image.getBytes();
            registerMember.setProfileImage(imageBytes);
        }
        loginService.register(registerMember);
        return "redirect:/";
    }

    private static Member makeMember(Member member) {
        Member registerMember = new Member();
        registerMember.setEmail(member.getEmail());
        registerMember.setPassword(member.getPassword());
        registerMember.setName(member.getName());
        registerMember.setLoginId(member.getLoginId());
        registerMember.setSex(member.getSex());
        registerMember.setId(member.getId());
        registerMember.setLoginId(member.getLoginId());
        registerMember.setGrade(member.getGrade());
        return registerMember;
    }

    @GetMapping("/member/mypage")
    public String memberPage(Model model, @SessionAttribute(name = UserConst.LOGIN_MEMBER,required = false) Member member) {
        Member memberById = memberService.findMemberById(member.getId());
        List<Clan> clans = clanService.getMemberClanList(memberById);
        model.addAttribute("clanList",clans);
        model.addAttribute("member",memberById);
        return "member/myPage";
    }

    @GetMapping("/clans/{clanId}/member/{memberId}")
    @ResponseBody
    public Member getMemberDetails(@PathVariable Long clanId, @PathVariable Long memberId) {
        return memberService.findMemberById(memberId); // 멤버 객체를 JSON 형식으로 반환
    }

    @GetMapping("/clans/{clanId}/signupMember/{applicantId}")
    @ResponseBody
    public Member getApplicantDetails(@PathVariable Long clanId, @PathVariable Long applicantId) {
        Clan clan = clanService.findClanById(clanId); // 클럽 정보 찾기

        if (clan == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Clan not found"); // 클럽이 없으면 404 에러
        }

        return clan.getSignupMembers().stream()
                .filter(member -> member.getId().equals(applicantId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Applicant not found")); // 신청자 정보 반환
    }

//    // 멤버 역할 수정
//    @PutMapping("/{memberId}/role")
//    public ResponseEntity<?> editRole(@PathVariable Long memberId, @RequestBody RoleRequest roleRequest) {
//        try {
//            clubService.updateMemberRoleById(memberId, roleRequest.getRole());
//            return ResponseEntity.ok(new ApiResponse(true, "Role updated successfully"));
//        } catch (Exception e) {
//            return ResponseEntity.status(400).body(new ApiResponse(false, "Failed to update role"));
//        }
//    }

    @DeleteMapping("/clans/{clanId}/members/{memberId}")
    public ResponseEntity<?> deleteMember(@PathVariable Long clanId, @PathVariable Long memberId) {
        log.info("Delete request received for memberId: {} from clanId: {}", memberId, clanId);

        try {
            // Find the clan by ID
            Clan clan = clanService.findClanById(clanId);
            if (clan == null) {
                return ResponseEntity.status(404).body("Clan not found");
            }

            // Find the member by ID
            Member memberById = memberService.findMemberById(memberId);
            if (memberById == null || !memberById.getClan().equals(clan)) {
                return ResponseEntity.status(404).body("Member not found in the specified clan");
            }

            // Delete the member from the clan
            clanService.deleteMember(clan, memberById);

            return ResponseEntity.ok("Member deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting member: {}", e.getMessage());
            return ResponseEntity.status(400).body("Failed to delete member");
        }
    }

}
