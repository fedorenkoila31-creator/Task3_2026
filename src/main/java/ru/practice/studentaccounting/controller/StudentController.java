package ru.practice.studentaccounting.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.practice.studentaccounting.model.Student;
import ru.practice.studentaccounting.service.StudentService;

@Controller
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/students")
    public String list(HttpSession session, Model model) {
        if (session.getAttribute("userId") == null) {
            return "redirect:/login";
        }
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("username", session.getAttribute("username"));
        return "students-list";
    }

    @GetMapping("/students/add")
    public String addForm(HttpSession session, @RequestParam(required = false) String error, Model model) {
        if (session.getAttribute("userId") == null) {
            return "redirect:/login";
        }
        if ("true".equals(error)) {
            model.addAttribute("error", "Введите имя длиной до 120 символов и корректный курс от 1 до 6.");
        }
        Student student = new Student();
        student.setHasDebt(false);
        model.addAttribute("student", student);
        return "students-form";
    }

    @PostMapping("/students/add")
    public String add(@ModelAttribute("student") Student student, HttpSession session) {
        if (session.getAttribute("userId") == null) {
            return "redirect:/login";
        }
        if (student.getName() == null || student.getName().isBlank()
                || student.getName().trim().length() > 120
                || student.getCourse() == null || student.getCourse() < 1 || student.getCourse() > 6) {
            return "redirect:/students/add?error=true";
        }
        student.setName(student.getName().trim());
        studentService.saveStudent(student);
        return "redirect:/students";
    }
}
