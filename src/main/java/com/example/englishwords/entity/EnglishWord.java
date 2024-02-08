package com.example.englishwords.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.Id;

import java.util.Objects;

@Entity
@Table(name = "words")
public class EnglishWord {

    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "english_word")
    private String word;
    @Column(name = "translation")
    private String translate;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getTranslate() {
        return translate;
    }

    public void setTranslate(String translate) {
        this.translate = translate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EnglishWord that = (EnglishWord) o;
        return Objects.equals(id, that.id) && Objects.equals(word, that.word) && Objects.equals(translate, that.translate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, word, translate);
    }


}
