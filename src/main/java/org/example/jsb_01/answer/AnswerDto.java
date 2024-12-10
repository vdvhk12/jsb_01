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
    private LocalDateTime createdDate;

    public static AnswerDto from(Answer answer) {
        return AnswerDto.builder()
            .id(answer.getId())
            .content(answer.getContent())
            .createdDate(answer.getCreateDate())
            .build();
    }
}
