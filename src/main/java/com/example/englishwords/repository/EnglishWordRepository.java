package com.example.englishwords.repository;

import com.example.englishwords.entity.EnglishWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EnglishWordRepository extends JpaRepository<EnglishWord,Long> {
    Optional<EnglishWord> findEnglishWordById(Long id);


}
