package com.edumate.edumate.services;

import com.edumate.edumate.entities.user.Student;
import com.edumate.edumate.repositories.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class StudentService {

  private final StudentRepository repository;

  public Integer uploadStudents(MultipartFile file) throws IOException {
    Set<Student> students = parseCsv(file);
    repository.saveAll(students);
    return students.size();
  }

  private Set<Student> parseCsv(MultipartFile file) throws IOException {
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
          .map(csvLine -> Student.builder()
              .firstname(csvLine.getFirstname())
              .lastname(csvLine.getLastname())
              .age(csvLine.getAge())
              .build()
          )
          .collect(Collectors.toSet());
    }
  }
}