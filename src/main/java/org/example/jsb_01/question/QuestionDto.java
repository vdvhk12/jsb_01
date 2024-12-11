package org.example.jsb_01.question;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.jsb_01.answer.AnswerDto;
import org.example.jsb_01.user.SiteUserDto;

@Getter
@Setter
@Builder
public class QuestionDto {

    private Long id;
    private String subject;
    private String content;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
    private SiteUserDto author;
    private List<AnswerDto> answerList;
    private Set<SiteUserDto> voter;

    // Question 엔티티를 DTO로 변환하는 메서드
    public static QuestionDto toDto(Question question) {
        return QuestionDto.builder()
            .id(question.getId())
            .subject(question.getSubject())
            .content(question.getContent())
            .createDate(question.getCreateDate())
            .modifyDate(question.getModifyDate())
            .author(SiteUserDto.toDto(question.getAuthor()))
            .answerList(question.getAnswerList().stream()
                .map(AnswerDto::toDto)
                .toList())
            .voter(question.getVoter().stream().map(SiteUserDto::toDto).collect(Collectors.toSet()))
            .build();
    }
}
