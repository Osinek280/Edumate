package com.edumate.edumate.services;

import com.edumate.edumate.entities.user.AppUser;
import com.edumate.edumate.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import java.security.SecureRandom;
import java.util.Random;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class StudentService {

  private final BCryptPasswordEncoder passwordEncoder;
  private final UserRepository repository;

  private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
  private static final int PASSWORD_LENGTH = 12;

  public Integer uploadStudents(MultipartFile file) throws IOException {
    Set<AppUser> students = parseCsv(file);
    repository.saveAll(students);
    return students.size();
  }

  private Set<AppUser> parseCsv(MultipartFile file) throws IOException {
    try(Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
      HeaderColumnNameMappingStrategy<StudentCsvRepresentation> strategy =
          new HeaderColumnNameMappingStrategy<>();
      strategy.setType(StudentCsvRepresentation.class);
      CsvToBean<StudentCsvRepresentation> csvToBean =
          new CsvToBeanBuilder<StudentCsvRepresentation>(reader)
              .withMappingStrategy(strategy)
              .withIgnoreEmptyLine(true)
              .withIgnoreLeadingWhiteSpace(true)
              .build();
      return csvToBean.parse()
          .stream()
          .map(csvLine -> AppUser.builder()
              .firstname(csvLine.getFirstname())
              .lastname(csvLine.getLastname())
              .email(csvLine.getFirstname())
              .password(passwordEncoder.encode("test"))
              .build()
          )
          .collect(Collectors.toSet());
    }
  }

  private String generatePassword(String email) {
    Random random = new SecureRandom();
    StringBuilder password = new StringBuilder(PASSWORD_LENGTH);

    for (int i = 0; i < PASSWORD_LENGTH; i++) {
      int index = random.nextInt(CHARACTERS.length());
      password.append(CHARACTERS.charAt(index));
    }

    System.out.println(email);
    System.out.println(password.toString());

    return passwordEncoder.encode(password.toString());
  }
}