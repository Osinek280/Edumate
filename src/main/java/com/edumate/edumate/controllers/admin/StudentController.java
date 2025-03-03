package com.edumate.edumate.controllers.admin;

import com.edumate.edumate.services.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/admin/students")
@RequiredArgsConstructor
public class StudentController {

  @Autowired
  private StudentService service;

  @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
  public ResponseEntity<Integer> uploadStudents(
      @RequestPart("file")MultipartFile file
  ) throws IOException {
    return ResponseEntity.ok(service.uploadStudents(file));
  }
}
