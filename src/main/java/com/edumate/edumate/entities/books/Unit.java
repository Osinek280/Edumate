package com.edumate.edumate.entities.books;

import com.edumate.edumate.entities.vocabulary.Vocabulary;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Unit {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  private Integer unitNumber;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "book_id")
  private Book book;

  @Lob
  @Column(name = "wordlist_image")
  private byte[] reviewImage;

  @ManyToMany(mappedBy = "units")
  private Set<Vocabulary> vocabularySet;
}
