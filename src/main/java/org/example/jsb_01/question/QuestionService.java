package org.example.jsb_01.question;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.jsb_01.DataNotFoundException;
import org.example.jsb_01.answer.Answer;
import org.example.jsb_01.user.SiteUser;
import org.example.jsb_01.user.SiteUserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    public Page<QuestionDto> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(new Sort.Order(Sort.Direction.DESC, "createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Specification<Question> spec = search(kw);

        // 키워드로 직접 쿼리문 날려서 데이터 가져오는 방법
        // return questionRepository.findAllByKeyword(kw, pageable).map(QuestionDto::toDto);

        return questionRepository.findAll(spec, pageable).map(QuestionDto::toDto);
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

    public Specification<Question> search(String kw) {
        return new Specification<>() {
            @Serial
            private static final long serialVersionUID = 1L;

            // 자바로 쿼리문 작성 방법
            @Override
            public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query,
                CriteriaBuilder cb) {
                query.distinct(true); //중복 제거
                Join<Question, SiteUser> u1 = q.join("author", JoinType.LEFT);
                Join<Question, Answer> a = q.join("answerList", JoinType.LEFT);
                Join<Answer, SiteUser> u2 = a.join("author", JoinType.LEFT);

                return cb.or(cb.like(q.get("subject"), "%" + kw + "%"), // 제목
                    cb.like(q.get("content"), "%" + kw + "%"),      // 내용
                    cb.like(u1.get("username"), "%" + kw + "%"),    // 질문 작성자
                    cb.like(a.get("content"), "%" + kw + "%"),      // 답변 내용
                    cb.like(u2.get("username"), "%" + kw + "%"));   // 답변 작성자
            }
        };
    }

}
