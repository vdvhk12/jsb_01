package org.example.jsb_01;

import static org.assertj.core.api.Assertions.*;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.example.jsb_01.repository.AnswerRepository;
import org.example.jsb_01.repository.QuestionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Jsb01ApplicationTests {

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private AnswerRepository answerRepository;

	@Test
	@Transactional
	@DisplayName("findById 테스트")
	void test01() {
	    //given
		Question q1 = new Question();
		q1.setSubject("sbb가 무엇인가요?");
		q1.setContent("sbb에 대해서 알고 싶습니다.");
		q1.setCreateDate(LocalDateTime.now());
		this.questionRepository.save(q1);

		Question q2 = new Question();
		q2.setSubject("스프링부트 모델 질문입니다.");
		q2.setContent("id는 자동으로 생성되나요?");
		q2.setCreateDate(LocalDateTime.now());
		this.questionRepository.save(q2);

		//when
		Optional<Question> byId1 = questionRepository.findById(q1.getId());
		Optional<Question> byId2 = questionRepository.findById(q2.getId());

		//then
		if(byId1.isPresent() && byId2.isPresent()) {
			assertThat(q1.getContent()).isEqualTo(byId1.get().getContent());
			assertThat(q2.getContent()).isEqualTo(byId2.get().getContent());
		}

	}

	@Test
	@Transactional
	@DisplayName("findAll 테스트")
	void test02() {
		//given
		Question q1 = new Question();
		q1.setSubject("sbb가 무엇인가요?");
		q1.setContent("sbb에 대해서 알고 싶습니다.");
		q1.setCreateDate(LocalDateTime.now());
		this.questionRepository.save(q1);

		Question q2 = new Question();
		q2.setSubject("스프링부트 모델 질문입니다.");
		q2.setContent("id는 자동으로 생성되나요?");
		q2.setCreateDate(LocalDateTime.now());
		this.questionRepository.save(q2);

		//when
		List<Question> all = questionRepository.findAll();

		//then
		assertThat(all.size()).isEqualTo(2);
	}

	@Test
	@Transactional
	@DisplayName("findBySubject 테스트")
	void test03() {
		//given
		Question q1 = new Question();
		q1.setSubject("sbb가 무엇인가요?");
		q1.setContent("sbb에 대해서 알고 싶습니다.");
		q1.setCreateDate(LocalDateTime.now());
		this.questionRepository.save(q1);

		//when
		Question bySubject = questionRepository.findBySubject("sbb가 무엇인가요?");

		//then
		assertThat(bySubject.getContent()).isEqualTo(q1.getContent());
	}

	@Test
	@Transactional
	@DisplayName("findBySubjectAndContent 테스트")
	void test04() {
		//given
		Question q1 = new Question();
		q1.setSubject("sbb가 무엇인가요?");
		q1.setContent("sbb에 대해서 알고 싶습니다.");
		q1.setCreateDate(LocalDateTime.now());
		this.questionRepository.save(q1);

		//when
		Question bySubjectAndContent = questionRepository.findBySubjectAndContent("sbb가 무엇인가요?",
			"sbb에 대해서 알고 싶습니다.");

		//then
		assertThat(bySubjectAndContent.getContent()).isEqualTo(q1.getContent());
		assertThat(bySubjectAndContent.getCreateDate()).isEqualTo(q1.getCreateDate());
	}

	@Test
	@Transactional
	@DisplayName("findBySubjectLike 테스트")
	void test05() {
		//given
		Question q1 = new Question();
		q1.setSubject("sbb가 무엇인가요?");
		q1.setContent("sbb에 대해서 알고 싶습니다.");
		q1.setCreateDate(LocalDateTime.now());
		this.questionRepository.save(q1);

		//when
		List<Question> bySubjectLike = questionRepository.findBySubjectLike("sbb%");

		//then
		assertThat(bySubjectLike.getFirst().getContent()).isEqualTo(q1.getContent());
	}

	@Test
	@Transactional
	@DisplayName("UPDATE 테스트")
	void test06() {
		//given
		Question q1 = new Question();
		q1.setSubject("sbb가 무엇인가요?");
		q1.setContent("sbb에 대해서 알고 싶습니다.");
		q1.setCreateDate(LocalDateTime.now());
		this.questionRepository.save(q1);

		//when
		questionRepository.findById(q1.getId()).ifPresent(q -> {
			q.setSubject("수정된 제목");
			this.questionRepository.save(q);
		});

		//then
		questionRepository.findById(q1.getId())
			.ifPresent(question -> assertThat(question.getSubject()).isEqualTo("수정된 제목"));
	}

	@Test
	@Transactional
	@DisplayName("DELETE 테스트")
	void test07() {
		//given
		Question q1 = new Question();
		q1.setSubject("sbb가 무엇인가요?");
		q1.setContent("sbb에 대해서 알고 싶습니다.");
		q1.setCreateDate(LocalDateTime.now());
		this.questionRepository.save(q1);

		Question q2 = new Question();
		q2.setSubject("스프링부트 모델 질문입니다.");
		q2.setContent("id는 자동으로 생성되나요?");
		q2.setCreateDate(LocalDateTime.now());
		this.questionRepository.save(q2);

		//when
		assertThat(questionRepository.count()).isEqualTo(2);
		Optional<Question> byId = questionRepository.findById(q1.getId());
        byId.ifPresent(question -> questionRepository.delete(question));

		//then
		assertThat(questionRepository.count()).isEqualTo(1);
	}

	@Test
	@Transactional
	@DisplayName("Answer CREATE, findById 테스트")
	void test08() {
		//given
		Question q1 = new Question();
		q1.setSubject("스프링부트 모델 질문입니다.");
		q1.setContent("id는 자동으로 생성되나요?");
		q1.setCreateDate(LocalDateTime.now());
		this.questionRepository.save(q1);

		Question question = questionRepository.findById(q1.getId()).orElse(null);

		Answer a1 = new Answer();
		a1.setContent("네 자동으로 생성됩니다.");
		a1.setCreateDate(LocalDateTime.now());
		a1.setQuestion(question);
		this.answerRepository.save(a1);

		//when
		Optional<Answer> byId = answerRepository.findById(a1.getId());

		//then
		byId.ifPresent(answer -> assertThat(answer.getContent()).isEqualTo(a1.getContent()));
	}

}
