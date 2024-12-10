package org.example.jsb_01.question;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionRepository questionRepository;

    @GetMapping("/question/list")
    public String list(Model model) {
        List<QuestionDto> questionDtos = questionRepository.findAll().stream()
            .map(QuestionDto::from).toList();
        model.addAttribute("questions", questionDtos);
        return "question_list";
    }
}
