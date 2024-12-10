package org.example.jsb_01.question;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.jsb_01.DataNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    public List<QuestionDto> getList() {
        return questionRepository.findAll().stream()
            .map(QuestionDto::toDto).toList();
    }

    public QuestionDto getQuestion(Long id) {
        return questionRepository.findById(id).map(QuestionDto::toDto)
            .orElseThrow(() -> new DataNotFoundException("question not fount"));
    }

}
