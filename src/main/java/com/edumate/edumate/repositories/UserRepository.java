package com.edumate.edumate.repositories;

import com.edumate.edumate.entities.user.AppUser;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
public interface UserRepository extends JpaRepository<AppUser, Integer> {
  Optional<AppUser> findByEmail(String email);
}
