package demo.csecircle.controller;

import demo.csecircle.UserConst;
import demo.csecircle.classification.MemberRole;
import demo.csecircle.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.Base64;

@Controller
@RequiredArgsConstructor
public class MainScreenController {

    @GetMapping("/")
    public String sessionValidate(@SessionAttribute (name = UserConst.LOGIN_MEMBER, required = false) Member loginMember, Model model) {
        if (loginMember == null) {
            return "member/login";
        }
        if (loginMember.getProfileImage() != null) {
            String base64Image = Base64.getEncoder().encodeToString(loginMember.getProfileImage());
            model.addAttribute("base64Image", base64Image);
        }
        boolean isAdmin = loginMember.getMemberRole().equals(MemberRole.WEBSITE_ADMIN);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("member", loginMember);
        return "index";
    }

}
