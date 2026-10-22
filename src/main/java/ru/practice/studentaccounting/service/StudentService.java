package ru.practice.studentaccounting.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practice.studentaccounting.model.Student;
import ru.practice.studentaccounting.repository.StudentRepository;

import java.util.List;

@Service
@Transactional
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Transactional(readOnly = true)
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student saveStudent(Student student) {
        if (student.getHasDebt() == null) {
            student.setHasDebt(false);
        }
        return studentRepository.save(student);
    }
}
