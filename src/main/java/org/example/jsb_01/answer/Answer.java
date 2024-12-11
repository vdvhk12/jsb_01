package org.example.jsb_01.answer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.jsb_01.question.Question;
import org.example.jsb_01.user.SiteUser;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    @ManyToOne
    private SiteUser author;

    @ManyToOne
    private Question question;

    public static Answer create(Question question, String content, SiteUser author) {
        return Answer.builder()
            .content(content)
            .createDate(LocalDateTime.now())
            .question(question)
            .author(author)
            .build();
    }

    public Answer modify(String content) {
        return Answer.builder()
            .id(this.id)
            .content(content)
            .createDate(this.createDate)
            .modifyDate(LocalDateTime.now())
            .author(this.author)
            .question(this.question)
            .build();
    }
}
