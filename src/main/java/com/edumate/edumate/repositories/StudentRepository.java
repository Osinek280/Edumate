package com.edumate.edumate.repositories;

import com.edumate.edumate.entities.user.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Integer> {
}