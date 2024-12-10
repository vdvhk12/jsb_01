package org.example.jsb_01;

import static org.assertj.core.api.Assertions.*;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import org.example.jsb_01.repository.QuestionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Jsb01ApplicationTests {

	@Autowired
	private QuestionRepository questionRepository;

	@Test
	@Transactional
	@DisplayName("Question 생성 테스트")
	void testJpa() {
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
		Question question1 = questionRepository.findById(q1.getId()).get();
		Question question2 = questionRepository.findById(q2.getId()).get();

		//then
		assertThat(q1.getContent()).isEqualTo(question1.getContent());
		assertThat(q2.getContent()).isEqualTo(question2.getContent());
	}


}
