package org.example.jsb_01.answer;

import lombok.RequiredArgsConstructor;
import org.example.jsb_01.DataNotFoundException;
import org.example.jsb_01.question.Question;
import org.example.jsb_01.question.QuestionDto;
import org.example.jsb_01.question.QuestionRepository;
import org.example.jsb_01.user.SiteUserDto;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    public AnswerDto createAnswer(QuestionDto questionDto, String content, SiteUserDto siteUserDto) {
        Question question = questionRepository.findById(questionDto.getId())
            .orElseThrow(() -> new DataNotFoundException("Question with ID " + questionDto.getId() + " not found"));

        return AnswerDto.toDto(answerRepository.save(
            Answer.create(question, content, SiteUserDto.fromDto(siteUserDto))));
    }

    public AnswerDto getAnswer(Long id) {
        return AnswerDto.toDto(answerRepository.findById(id)
            .orElseThrow(() -> new DataNotFoundException("answer not found")));
    }

    public void modifyAnswer(Long id, AnswerForm answerForm) {
        Answer answer = answerRepository.findById(id)
            .orElseThrow(() -> new DataNotFoundException("answer not found"));
        answerRepository.save(answer.modify(answerForm.getContent()));
    }

    public void deleteAnswer(Long id) {
        answerRepository.deleteById(id);
    }

    public void vote(Long id, SiteUserDto siteUserDto) {
        Answer answer = answerRepository.findById(id)
            .orElseThrow(() -> new DataNotFoundException("answer not found"));
        answer.getVoter().add(SiteUserDto.fromDto(siteUserDto));
        answerRepository.save(answer);
    }
}
