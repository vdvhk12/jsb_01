package org.example.jsb_01.answer;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.jsb_01.user.SiteUserDto;

@Getter
@Setter
@Builder
public class AnswerDto {

    private Long id;
    private String content;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
    private SiteUserDto author;
    private Long questionId;

    public static AnswerDto toDto(Answer answer) {
        return AnswerDto.builder()
            .id(answer.getId())
            .content(answer.getContent())
            .createDate(answer.getCreateDate())
            .modifyDate(answer.getModifyDate())
            .author(SiteUserDto.toDto(answer.getAuthor()))
            .questionId(answer.getQuestion().getId())
            .build();
    }
}
