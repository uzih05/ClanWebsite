package demo.csecircle.controller;

import demo.csecircle.UserConst;
import demo.csecircle.controller.form.LoginForm;
import demo.csecircle.domain.Member;
import demo.csecircle.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("member/login")
    public String login(@ModelAttribute LoginForm loginForm) {
        return "member/login";
    }

    @PostMapping("member/login")
    public String login(@Validated @ModelAttribute("loginForm") LoginForm loginForm, BindingResult bindingResult,
                        HttpServletRequest request, @RequestParam(defaultValue = "/") String redirectURL) {
        if (bindingResult.hasErrors()) {
            return "member/login";
        }
        Member loginMember = loginService.login(loginForm.getLoginId(), loginForm.getPassword());
        if (loginMember == null){
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지않습니다");
            return "member/login";
        }
        HttpSession session = request.getSession();
        session.setAttribute(UserConst.LOGIN_MEMBER, loginMember);
        return "redirect:" + redirectURL;
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null){
            session.invalidate();
        }
        return "redirect:/member/login";
    }
}
