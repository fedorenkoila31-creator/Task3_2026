package ru.practice.studentaccounting.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practice.studentaccounting.service.UserService;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping({"/", "/login"})
    public String loginForm(@RequestParam(required = false) String registered,
                            @RequestParam(required = false) String error,
                            Model model) {
        if ("true".equals(registered)) {
            model.addAttribute("notice", "Регистрация завершена. Теперь войдите в систему.");
        }
        if ("true".equals(error)) {
            model.addAttribute("error", "Неверный логин или пароль.");
        }
        return "login-form";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session) {
        return userService.authenticate(username, password)
                .map(user -> {
                    session.setAttribute("userId", user.getId());
                    session.setAttribute("username", user.getUsername());
                    return "redirect:/students";
                })
                .orElse("redirect:/login?error=true");
    }

    @GetMapping("/register")
    public String registerForm(@RequestParam(required = false) String error, Model model) {
        if ("true".equals(error)) {
            model.addAttribute("error", "Не удалось зарегистрировать пользователя. Проверьте данные или выберите другой логин.");
        }
        return "register-form";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password) {
        if (username.trim().length() > 50 || password.length() > 100) {
            return "redirect:/register?error=true";
        }
        try {
            if (!userService.register(username, password)) {
                return "redirect:/register?error=true";
            }
        } catch (DataIntegrityViolationException exception) {
            return "redirect:/register?error=true";
        }
        return "redirect:/login?registered=true";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
