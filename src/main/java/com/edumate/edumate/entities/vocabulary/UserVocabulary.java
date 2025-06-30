package com.edumate.edumate.entities.vocabulary;

import com.edumate.edumate.entities.user.AppUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserVocabulary {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  private AppUser user;

  @ManyToOne
  private Vocabulary vocabulary;

  @Enumerated(EnumType.STRING)
  private LearningStatus status;

  private double difficulty;  // Indywidualna trudność dla tego użytkownika (np. 0.5)
  private double easiness;    // Indywidualny współczynnik łatwości (np. 2.5)
  private int repetitions;    // Liczba powtórek
  private LocalDate nextReviewDate;

  @OneToMany(cascade = CascadeType.ALL)
  private List<ReviewLog> reviewLogs = new ArrayList<>();
}
