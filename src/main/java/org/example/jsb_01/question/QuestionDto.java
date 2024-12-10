package org.example.jsb_01.question;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.jsb_01.answer.AnswerDto;

@Getter
@Setter
@Builder
public class QuestionDto {

    private Long id;
    private String subject;
    private String content;
    private LocalDateTime createDate;
    private List<AnswerDto> answerList;

    // Question 엔티티를 DTO로 변환하는 메서드
    public static QuestionDto toDto(Question question) {
        return QuestionDto.builder()
            .id(question.getId())
            .subject(question.getSubject())
            .content(question.getContent())
            .createDate(question.getCreateDate())
            .answerList(question.getAnswerList().stream()
                .map(AnswerDto::toDto)
                .toList())
            .build();
    }
}
