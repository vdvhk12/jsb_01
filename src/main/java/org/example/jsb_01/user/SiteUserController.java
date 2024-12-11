package org.example.jsb_01.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class SiteUserController {

    private final SiteUserService siteUserService;

    @GetMapping("/signup")
    public String signup(SiteUserCreateForm siteUserCreateForm) {
        return "signup_form";
    }

    @PostMapping("/signup")
    public String signup(@Valid SiteUserCreateForm form, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "signup_form";
        }

        if (!form.getPassword1().equals(form.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordIncorrect", "2개의 패스워드가 일치하지 않습니다.");
            return "signup_form";
        }

        try {
            siteUserService.createSiteUser(form.getUsername(), form.getPassword1(), form.getEmail());
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "signup_form";
        } catch(Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "signup_form";
        }

        return "redirect:/";
    }
}
