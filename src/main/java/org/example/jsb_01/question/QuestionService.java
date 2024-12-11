package org.example.jsb_01.question;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.jsb_01.DataNotFoundException;
import org.example.jsb_01.user.SiteUser;
import org.example.jsb_01.user.SiteUserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    public Page<QuestionDto> getList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(new Sort.Order(Sort.Direction.DESC, "createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return questionRepository.findAll(pageable).map(QuestionDto::toDto);
    }

    public QuestionDto getQuestion(Long id) {
        return questionRepository.findById(id).map(QuestionDto::toDto)
            .orElseThrow(() -> new DataNotFoundException("question not fount"));
    }

    public void createQuestion(String subject, String content, SiteUserDto siteUserDto) {
        questionRepository.save(Question.create(subject, content, SiteUserDto.fromDto(siteUserDto)));
    }

    public void modifyQuestion(QuestionDto questionDto, QuestionForm questionForm) {
        // 나중에 dto를 entity로 변환하는 메서드를 작성해보자. 지금은 헷갈려서 못하겠다.
        Question question = questionRepository.findById(questionDto.getId())
            .orElseThrow(() -> new DataNotFoundException("question not fount"));
        questionRepository.save(question.update(questionForm));
    }

    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }

    public void vote(Long id, SiteUserDto siteUserDto) {
        Question question = questionRepository.findById(id)
            .orElseThrow(() -> new DataNotFoundException("question not fount"));
        question.getVoter().add(SiteUserDto.fromDto(siteUserDto));
        questionRepository.save(question);
    }

}
