package com.edumate.edumate.entities.books;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Book {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NotBlank
  private String title;

  @NotBlank
  private String isbn;

  private String authors;

  @Lob
  @Column(name = "cover_image")
  private byte[] coverImage;

  @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  @Builder.Default
  @JsonBackReference
  private Set<Unit> units = new HashSet<>();

  public void addUnit(Unit unit) {
    units.add(unit);
    unit.setBook(this);
  }

  public void removeUnit(Unit unit) {
    units.remove(unit);
    unit.setBook(null);
  }
}