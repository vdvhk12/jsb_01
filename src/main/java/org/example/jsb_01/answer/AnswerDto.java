package org.example.jsb_01.answer;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AnswerDto {

    private Long id;
    private String content;
    private LocalDateTime createDate;
    private Long questionId;

    public static AnswerDto toDto(Answer answer) {
        return AnswerDto.builder()
            .id(answer.getId())
            .content(answer.getContent())
            .createDate(answer.getCreateDate())
            .questionId(answer.getQuestion().getId())
            .build();
    }
}
