package demo.csecircle.controller;

import demo.csecircle.UserConst;
import demo.csecircle.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequiredArgsConstructor
public class MainScreenController {

    @GetMapping("/")
    public String sessionValidate(@SessionAttribute (name = UserConst.LOGIN_MEMBER, required = false) Member loginMember, Model model) {
        if (loginMember == null) {
            return "member/login";
        }
        model.addAttribute("member", loginMember);
        return "index";
    }

}
