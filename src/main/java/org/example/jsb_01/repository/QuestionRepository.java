package org.example.jsb_01.repository;

import org.example.jsb_01.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    Question findBySubject(String subject);
}
