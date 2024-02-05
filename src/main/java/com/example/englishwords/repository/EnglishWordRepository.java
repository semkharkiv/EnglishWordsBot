package com.example.englishwords.repository;

import com.example.englishwords.entity.EnglishWord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EnglishWordRepository extends JpaRepository<EnglishWord,Long> {
    Optional<EnglishWord> findFirstById();
}
