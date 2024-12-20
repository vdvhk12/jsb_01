package org.example.jsb_01;

import static org.assertj.core.api.Assertions.*;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.example.jsb_01.answer.Answer;
import org.example.jsb_01.answer.AnswerRepository;
import org.example.jsb_01.question.Question;
import org.example.jsb_01.question.QuestionRepository;
import org.example.jsb_01.question.QuestionService;
import org.example.jsb_01.user.SiteUser;
import org.example.jsb_01.user.SiteUserDto;
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

    @Autowired
    private QuestionService questionService;

	@Test
	@Transactional
	@DisplayName("findById 테스트")
	void test01() {
	    //given
		Question q1 = Question.builder()
			.subject("sbb가 무엇인가요?")
			.content("sbb에 대해서 알고 싶습니다.")
			.createDate(LocalDateTime.now())
			.build();
		questionRepository.save(q1);

		Question q2 = Question.builder()
			.subject("스프링부트 모델 질문입니다.")
			.content("id는 자동으로 생성되나요?")
			.createDate(LocalDateTime.now())
			.build();
		questionRepository.save(q2);

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
		long count = questionRepository.count();

		Question q1 = Question.builder()
			.subject("sbb가 무엇인가요?")
			.content("sbb에 대해서 알고 싶습니다.")
			.createDate(LocalDateTime.now())
			.build();
		questionRepository.save(q1);

		Question q2 = Question.builder()
			.subject("스프링부트 모델 질문입니다.")
			.content("id는 자동으로 생성되나요?")
			.createDate(LocalDateTime.now())
			.build();
		questionRepository.save(q2);

		//when
		List<Question> all = questionRepository.findAll();

		//then
		assertThat(all.size()).isEqualTo(count + 2);
	}

	@Test
	@Transactional
	@DisplayName("findBySubject 테스트")
	void test03() {
		//given
		Question q1 = Question.builder()
			.subject("테스트 관련 질문입니다.")
			.content("TDD를 현업에서 많이 하나요?")
			.createDate(LocalDateTime.now())
			.build();
		questionRepository.save(q1);

		//when
		Question bySubject = questionRepository.findBySubject("테스트 관련 질문입니다.");

		//then
		assertThat(bySubject.getContent()).isEqualTo(q1.getContent());
	}

	@Test
	@Transactional
	@DisplayName("findBySubjectAndContent 테스트")
	void test04() {
		//given
		Question q1 = Question.builder()
			.subject("테스트 관련 질문입니다.")
			.content("TDD를 현업에서 많이 하나요?")
			.createDate(LocalDateTime.now())
			.build();
		questionRepository.save(q1);

		//when
		Question bySubjectAndContent = questionRepository.findBySubjectAndContent("테스트 관련 질문입니다.",
			"TDD를 현업에서 많이 하나요?");

		//then
		assertThat(bySubjectAndContent.getContent()).isEqualTo(q1.getContent());
		assertThat(bySubjectAndContent.getCreateDate()).isEqualTo(q1.getCreateDate());
	}

	@Test
	@Transactional
	@DisplayName("findBySubjectLike 테스트")
	void test05() {
		//given
		Question q1 = Question.builder()
			.subject("Like 테스트 입니다.")
			.content("id는 자동으로 생성되나요?")
			.createDate(LocalDateTime.now())
			.build();
		questionRepository.save(q1);

		//when
		List<Question> bySubjectLike = questionRepository.findBySubjectLike("Like%");

		//then
		assertThat(bySubjectLike.getFirst().getContent()).isEqualTo(q1.getContent());
	}

	@Test
	@Transactional
	@DisplayName("UPDATE 테스트")
	void test06() {
		//given
		Question q1 = Question.builder()
			.subject("스프링부트 모델 질문입니다.")
			.content("id는 자동으로 생성되나요?")
			.createDate(LocalDateTime.now())
			.build();
		questionRepository.save(q1);

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
		long count = questionRepository.count();

		Question q1 = Question.builder()
			.subject("sbb가 무엇인가요?")
			.content("sbb에 대해서 알고 싶습니다.")
			.createDate(LocalDateTime.now())
			.build();
		questionRepository.save(q1);

		Question q2 = Question.builder()
			.subject("스프링부트 모델 질문입니다.")
			.content("id는 자동으로 생성되나요?")
			.createDate(LocalDateTime.now())
			.build();
		questionRepository.save(q2);

		//when
		assertThat(questionRepository.count()).isEqualTo(count + 2);
		Optional<Question> byId = questionRepository.findById(q1.getId());
        byId.ifPresent(question -> questionRepository.delete(question));

		//then
		assertThat(questionRepository.count()).isEqualTo(count + 1);
	}

	@Test
	@Transactional
	@DisplayName("Answer CREATE, findById 테스트")
	void test08() {
		//given
		Question q1 = Question.builder()
			.subject("스프링부트 모델 질문입니다.")
			.content("id는 자동으로 생성되나요?")
			.createDate(LocalDateTime.now())
			.build();
		questionRepository.save(q1);

		Question question = questionRepository.findById(q1.getId()).orElse(null);

		Answer a1 = Answer.builder()
			.content("네 자동으로 생성됩니다.")
			.createDate(LocalDateTime.now())
			.question(question)
			.build();
		answerRepository.save(a1);

		//when
		Optional<Answer> byId = answerRepository.findById(a1.getId());

		//then
		byId.ifPresent(answer -> assertThat(answer.getContent()).isEqualTo(a1.getContent()));
	}

	@Test
	@Transactional
	@DisplayName("Answer-Question Read 테스트")
	void test09() {
		//given
		Question q1 = Question.builder()
			.subject("스프링부트 모델 질문입니다.")
			.content("id는 자동으로 생성되나요?")
			.createDate(LocalDateTime.now())
			.build();
		questionRepository.save(q1);

		Question question = questionRepository.findById(q1.getId()).orElse(null);

		Answer a1 = Answer.builder()
			.content("네 자동으로 생성됩니다.")
			.createDate(LocalDateTime.now())
			.question(question)
			.build();
		answerRepository.save(a1);

		//when
		Optional<Answer> byId = answerRepository.findById(a1.getId());

		//then
		byId.ifPresent(answer -> assertThat(answer.getQuestion().getSubject()).isEqualTo(q1.getSubject()));
	}

	@Test
	@Transactional
	@DisplayName("Question-Answer Read 테스트")
	void test10() {
		//given
		Question q1 = Question.builder()
			.subject("스프링부트 모델 질문입니다.")
			.content("id는 자동으로 생성되나요?")
			.createDate(LocalDateTime.now())
			.answerList(new ArrayList<>())
			.build();
		questionRepository.save(q1);

		Question question = questionRepository.findById(q1.getId()).orElse(null);

		Answer a1 = Answer.builder()
			.content("네 자동으로 생성됩니다.")
			.createDate(LocalDateTime.now())
			.question(question)
			.build();
		answerRepository.save(a1);

		q1.getAnswerList().add(a1); // 이 부분을 명시적으로 작성해야하는지? 작성을 안했을 때는 answerList의 사이즈가 0이 됨.

		//when
		Optional<Question> byId = questionRepository.findById(q1.getId());

		//then
		byId.ifPresent(q -> {
			List<Answer> answerList = byId.get().getAnswerList();
			assertThat(answerList.size()).isEqualTo(1);
			assertThat(answerList.getFirst().getContent()).isEqualTo("네 자동으로 생성됩니다.");
		});
	}

	@Test
	@DisplayName("테스트용 더미 데이터 300개 생성")
	void test11() {
		SiteUser siteUser = SiteUser.create("dummy", "dummy", "dummy@gamil.com");

		for (int i = 1; i <= 300; i++) {
			String subject = String.format("테스트 데이터입니다.:[%03d]", i);
			questionService.createQuestion(subject, "테스트용", SiteUserDto.toDto(siteUser));
		}
	}

}
