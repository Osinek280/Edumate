package com.edumate.edumate.utility;

import com.edumate.edumate.entities.user.AppUser;
import com.edumate.edumate.entities.user.Role;
import com.edumate.edumate.entities.vocabulary.Level;
import com.edumate.edumate.entities.vocabulary.Vocabulary;
import com.edumate.edumate.repositories.UserRepository;
import com.edumate.edumate.repositories.Vocabulary.VocabularyRepository;
import com.opencsv.CSVReader;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

//@Component
public class DataSeeder {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private VocabularyRepository vocabularyRepository;

  @Autowired
  private BCryptPasswordEncoder passwordEncoder;
  @PostConstruct
  public void seedData() {
    createUsers();
    importVocabularyFromCsv();
  }

  private void importVocabularyFromCsv() {
    try (
        Reader reader = new InputStreamReader(getClass().getResourceAsStream("/oxford-5000.csv"));
        CSVReader csvReader = new CSVReader(reader)
    ) {
      List<Vocabulary> words = new ArrayList<>();
      String[] nextLine;
      boolean firstLine = true;

      while ((nextLine = csvReader.readNext()) != null) {
        System.out.println("huj");
        if (firstLine) {
          firstLine = false; // skip header
          continue;
        }
        String word = nextLine[0];
        String wordClass = nextLine[1];
        String levelStr = nextLine[2];

        Level level;
        try {
          level = Level.valueOf(levelStr.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
          System.err.printf("⚠️  Pominięto słowo '%s' z nieprawidłowym levelem: '%s'%n", word, levelStr);
          continue; // pomiń błędne rekordy
        }

        Vocabulary vocab = Vocabulary.builder()
            .word(word)
            .wordClass(wordClass)
            .level(level)
            .build();

        words.add(vocab);
      }

      vocabularyRepository.saveAll(words);
      System.out.println("Wjebane słówka z CSV, skibidi gyat!");
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println("Błąd przy imporcie słówek, sprawdź plik i ścieżkę!");
    }
  }

  private void createUsers() {
    AppUser user = AppUser.builder()
        .email("admin@admin.com")
        .firstname("Adam")
        .lastname("Admin")
        .password(passwordEncoder.encode("admin"))
        .role(Role.ADMIN)
        .build();

    userRepository.save(user);
  }
}
