package org.example.jsb_01.question;

import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.example.jsb_01.answer.AnswerForm;
import org.example.jsb_01.user.SiteUser;
import org.example.jsb_01.user.SiteUserDto;
import org.example.jsb_01.user.SiteUserService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/question")
public class QuestionController {

    private final QuestionService questionService;
    private final SiteUserService siteUserService;

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<QuestionDto> paging = questionService.getList(page);
        model.addAttribute("paging", paging);
        return "question_list";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model, AnswerForm answerForm) {
        System.out.println("id = " + id);
        QuestionDto question = questionService.getQuestion(id);
        model.addAttribute("question", question);
        return "question_detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String create(QuestionForm questionForm) {
        return "question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String create(@Valid QuestionForm questionForm, BindingResult bindingResult,
        Principal principal) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        SiteUserDto siteUser = siteUserService.getSiteUser(principal.getName());

        questionService.createQuestion(questionForm.getSubject(), questionForm.getContent(),
            siteUser);
        return "redirect:/question/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modify(QuestionForm questionForm, @PathVariable("id") Long id,
        Principal principal) {
        QuestionDto question = questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        questionForm.setSubject(question.getSubject());
        questionForm.setContent(question.getContent());
        return "question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modify(@Valid QuestionForm questionForm, BindingResult bindingResult,
        @PathVariable("id") Long id, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        QuestionDto question = questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        questionService.modifyQuestion(question, questionForm);
        return String.format("redirect:/question/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id, Principal principal) {
        QuestionDto question = questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        questionService.deleteQuestion(id);
        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String questionVote(@PathVariable("id") Long id, Principal principal) {
        SiteUserDto siteUser = siteUserService.getSiteUser(principal.getName());
        questionService.vote(id, siteUser);
        return String.format("redirect:/question/detail/%s", id);
    }
}
