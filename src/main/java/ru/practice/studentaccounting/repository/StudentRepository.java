package ru.practice.studentaccounting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practice.studentaccounting.model.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
