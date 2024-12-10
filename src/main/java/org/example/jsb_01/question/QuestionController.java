package org.example.jsb_01.question;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/question")
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping("/list")
    public String list(Model model) {
        List<QuestionDto> questions = questionService.getList();
        model.addAttribute("questions", questions);
        return "question_list";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        System.out.println("id = " + id);
        QuestionDto question = questionService.getQuestion(id);
        model.addAttribute("question", question);
        return "question_detail";
    }

    @GetMapping("/create")
    public String create() {
        return "question_form";
    }

    @PostMapping("/create")
    public String create(@RequestParam("subject") String subject,
        @RequestParam("content") String content) {
        questionService.createQuestion(subject, content);
        return "redirect:/question/list";
    }
}
