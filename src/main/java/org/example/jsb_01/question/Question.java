package org.example.jsb_01.question;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.jsb_01.answer.Answer;
import org.example.jsb_01.user.SiteUser;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    @ManyToOne
    private SiteUser author;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Answer> answerList = new ArrayList<>();

    public static Question create(String subject, String content, SiteUser siteUser) {
        return Question.builder()
            .subject(subject)
            .content(content)
            .createDate(LocalDateTime.now())
            .author(siteUser)
            .answerList(new ArrayList<>())
            .build();
    }

    public Question update(QuestionForm questionForm) {
        return Question.builder()
            .id(this.id)
            .subject(questionForm.getSubject())
            .content(questionForm.getContent())
            .createDate(this.createDate)
            .modifyDate(LocalDateTime.now())
            .author(this.author)
            .answerList(this.answerList)
            .build();
    }
}
