package org.example.jsb_01.repository;

import java.util.Optional;
import org.example.jsb_01.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    Optional<Question> findById(@NonNull Long id);
}
