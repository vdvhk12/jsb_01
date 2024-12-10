package org.example.jsb_01.answer;

import lombok.RequiredArgsConstructor;
import org.example.jsb_01.DataNotFoundException;
import org.example.jsb_01.question.Question;
import org.example.jsb_01.question.QuestionDto;
import org.example.jsb_01.question.QuestionRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    public void create(QuestionDto questionDto, String content) {
        Question question = questionRepository.findById(questionDto.getId())
            .orElseThrow(() -> new DataNotFoundException("Question with ID " + questionDto.getId() + " not found"));

        answerRepository.save(Answer.create(question, content));
    }

}
