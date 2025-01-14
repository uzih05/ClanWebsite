package demo.csecircle.controller;

import demo.csecircle.domain.Member;
import demo.csecircle.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final LoginService loginService;


    @GetMapping("member/register")
    public String registerPage(Model model) {
        Member member =loginService.login("test","TEST");
        model.addAttribute("member",member);
        model.addAttribute("newMember", new Member());
        return "member/register";
    }

    @PostMapping("member/register")
    public String register(@ModelAttribute Member member) {
        Member registerMember = makeMember(member);
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

}
