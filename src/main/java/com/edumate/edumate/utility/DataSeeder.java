package com.edumate.edumate.utility;

import com.edumate.edumate.entities.user.AppUser;
import com.edumate.edumate.entities.user.Role;
import com.edumate.edumate.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Component
public class DataSeeder {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private BCryptPasswordEncoder passwordEncoder;
  @PostConstruct
  public void seedData() {
    createUsers();
  }

  private void createUsers() {
    AppUser user = AppUser.builder()
        .email("admin@admin.com")
        .firstname("Adam")
        .lastname("Admin")
        .password(passwordEncoder.encode("123"))
        .role(Role.STUDENT)
        .build();

    userRepository.save(user);
  }
}
