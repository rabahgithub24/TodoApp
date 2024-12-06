package com.app.todo.controller;

import com.app.todo.dto.RegistrationRequest;
import com.app.todo.model.User;
import com.app.todo.service.UserService;
import com.app.todo.util.Constants;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
@AllArgsConstructor
public class AuthenticationController {

    private final UserService userService;


    @GetMapping("/login")
    public String login(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<User> user = userService.findByEmail(username);
        if (user.isPresent()) {
            return "redirect:/";
        } else {
            model.addAttribute("error", false);
            return Constants.AUTH_LOGIN_PAGE;
        }
    }

    @GetMapping("/register/form")
    public String registrationForm(Model model) {
        model.addAttribute("registerDTO", new RegistrationRequest());
        model.addAttribute("emailAlreadyExists", false);
        model.addAttribute("success", false);
        return Constants.AUTH_REGISTER_PAGE;
    }

    @PostMapping("/register")
    public String register(@ModelAttribute(name = "registerDTO") RegistrationRequest registerDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return Constants.AUTH_REGISTER_PAGE;
        } else {
            String response = userService.register(registerDTO);
            if (response.equals("Email already exists")) {
                model.addAttribute("emailAlreadyExists", true);
                bindingResult.rejectValue("email", "", "Email already exists, try any other email");
                return Constants.AUTH_REGISTER_PAGE;
            } else if (response.equals("User registered successfully")) {
                model.addAttribute("success", true);
                model.addAttribute("registerDTO", new RegistrationRequest());
                return Constants.AUTH_REGISTER_PAGE;
            } else {
                model.addAttribute("error", true);
                model.addAttribute("errorMessage", response);
                return Constants.AUTH_REGISTER_PAGE;
            }
        }
    }
}
