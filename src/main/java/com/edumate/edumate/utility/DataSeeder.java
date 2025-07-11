package com.edumate.edumate.utility;

import com.edumate.edumate.entities.books.Book;
import com.edumate.edumate.entities.books.Unit;
import com.edumate.edumate.entities.user.AppUser;
import com.edumate.edumate.entities.user.Role;
import com.edumate.edumate.entities.vocabulary.Level;
import com.edumate.edumate.entities.vocabulary.Vocabulary;
import com.edumate.edumate.repositories.BookRepository;
import com.edumate.edumate.repositories.UnitRepository;
import com.edumate.edumate.repositories.UserRepository;
import com.edumate.edumate.repositories.Vocabulary.VocabularyRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//@Component
@RequiredArgsConstructor
public class DataSeeder {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private VocabularyRepository vocabularyRepository;

  @Autowired
  private BookRepository bookRepository;

  @Autowired
  private UnitRepository unitRepository;
  private final ObjectMapper objectMapper;


  @Autowired
  private BCryptPasswordEncoder passwordEncoder;
  @PostConstruct
  public void seedData() {
    createUsers();
//    importVocabularyFromCsv();
  }

//  @PostConstruct
  public void init() throws Exception {
    Book impulse3 = Book.builder()
        .title("Impulse 3 B1+")
        .isbn("9788381523950")
        .authors("Catherine McBeth, Patricia Reilly, Joanna Sobierska-Paczesny")
        .build();

    Set<Unit> units = new HashSet<>();

    for (int i = 1; i <= 4; i++) {
      // Wczytaj JSON
      String fileName = "/impulse_3_unit_" + i + ".json";
      InputStream inputStream = getClass().getResourceAsStream(fileName);
      if (inputStream == null) {
        throw new RuntimeException("Nie znaleziono pliku: " + fileName);
      }

      List<JsonWord> vocabularies = objectMapper.readValue(
          inputStream,
          new TypeReference<List<JsonWord>>() {}
      );

      Set<Vocabulary> vocabularySet = new HashSet<>();
      for (JsonWord dto : vocabularies) {
        Vocabulary vocabulary = Vocabulary.builder()
            .word(dto.getEnglish())
            .phonetic(dto.getPhonetic())
            .translation(dto.getTranslation())
            .build();
        vocabularyRepository.save(vocabulary);
        vocabularySet.add(vocabulary);
      }

      // Utwórz unit
      Unit unit = Unit.builder()
          .unitNumber(i)
          .book(impulse3)
          .vocabularySet(vocabularySet)
          .build();

      units.add(unit);
    }

    impulse3.setUnits(units);

    // Zapisz wszystko kaskadowo
    bookRepository.save(impulse3);
  }
  private void importVocabularyFromJsons() {
    ObjectMapper mapper = new ObjectMapper();

    try {
      // Załóżmy, że masz te pliki w resources i nazwy ich w liście
      List<String> jsonFiles = List.of(
          "/impulse_3_unit_1.json",
          "/impulse_3_unit_2.json",
          "/impulse_3_unit_3.json",
          "/impulse_3_unit_4.json"
      );

      // Znajdź lub stwórz książkę tylko raz, bez pierdolenia
      Book book = bookRepository.findByIsbn("9788381523950")
          .orElseGet(() -> {
            Book newBook = Book.builder()
                .title("Impulse 3 B1+")
                .isbn("9788381523950")
                .authors("Catherine McBeth, Patricia Reilly, Joanna Sobierska-Paczesny")
                .build();
            return bookRepository.save(newBook);
          });

      List<Vocabulary> allVocabList = new ArrayList<>();

      // Przerabiamy każdy json jak trzeba
      for (int i = 0; i < jsonFiles.size(); i++) {
        String file = jsonFiles.get(i);

        try (InputStream inputStream = getClass().getResourceAsStream(file)) {
          if (inputStream == null) {
            System.err.println("❌ Nie znalazłem pliku " + file + ", spadaj");
            continue;
          }

          TypeReference<List<JsonWord>> typeRef = new TypeReference<>() {};
          List<JsonWord> jsonWords = mapper.readValue(inputStream, typeRef);

          // Tworzymy unit dla tego jsona
          Unit unit = Unit.builder()
              .unitNumber(i + 1)  // unitNumber od 1 do 4
              .book(book)
              .build();
          unitRepository.save(unit);

          for (JsonWord jsonWord : jsonWords) {
            if (jsonWord.getEnglish() == null || jsonWord.getTranslation() == null) {
              System.out.println("⚠️ Pominięto skibidi null w " + file);
              continue;
            }

            Vocabulary vocab = Vocabulary.builder()
                .word(jsonWord.getEnglish())
                .phonetic(jsonWord.getPhonetic())
                .translation(jsonWord.getTranslation())
                .level(Level.B1)
                .units(Set.of(unit))  // każdy vocab podpięty do odpowiedniego unitu
                .build();

            allVocabList.add(vocab);
          }
        }
      }

      vocabularyRepository.saveAll(allVocabList);
      System.out.println("✅ Wjebane słówka z wszystkich JSONów, full gyat.");

    } catch (IOException e) {
      e.printStackTrace();
      System.err.println("❌ JSON padł, sprawdź format i nazwę plików, ziomek.");
    }
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
//            .wordClass(wordClass)
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
