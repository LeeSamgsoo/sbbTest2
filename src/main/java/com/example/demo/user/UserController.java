package com.example.demo.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/signup")
    public String userCreate(UserCreateForm userCreateForm) {
        return "signup_form";
    }

    @PostMapping("/signup")
    public String userCreate(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup_form";
        }
        if (!userCreateForm.getPassword().equals(userCreateForm.getPasswordCheck())) {
            bindingResult.rejectValue("passwordCheck", "password incorrect", "패스워드가 다릅니다.");
            return "signup_form";
        }
        try {
            this.userService.create(userCreateForm.getUsername(), userCreateForm.getPassword(), userCreateForm.getNickname());
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signup_error", "이미 가입된 사용자입니다.");
            return "signup_form";
        } catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("signup_error", e.getMessage());
            return "signup_form";
        }
        return "redirect:/article/list";
    }

    @GetMapping("/login")
    public String userLogin() {
        return "login_form";
    }

    @PostMapping("/login")
    public String userLogin(HttpServletRequest request, @RequestParam(value = "username") String username,
                            @RequestParam(value = "password") String password) {
        // 사용자 인증 로직 (예시)
        SiteUser user = userService.getUser(username);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            return "redirect:/user/login";
        }
        HttpSession session = request.getSession();
        // 로그인 성공 시 세션에 사용자 정보 저장
        session.setAttribute("username", username);
        return "redirect:/article/list"; // 홈 페이지로 리다이렉트
    }

    @GetMapping("/logout")
    public String userLogout(HttpSession session) {
        session.invalidate(); // 세션 무효화
        return "redirect:/article/list"; // 로그인 페이지로 리다이렉트
    }
    /*public String login(HttpServletRequest request, String username, String password) {
        // 사용자 인증 로직 (예시)
        if (username.equals("admin") && password.equals("password")) {
            // 로그인 성공 시 세션에 사용자 정보 저장
            HttpSession session = request.getSession();
            session.setAttribute("user", username);
            return "redirect:/home"; // 홈 페이지로 리다이렉트
        }

        // 로그인 실패 시 다시 로그인 페이지로 이동
        return "redirect:/login";
    }*/
}
