package com.app.todo;

import com.app.todo.enums.Role;
import com.app.todo.model.User;
import com.app.todo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class TodoApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(TodoApplication.class, args);
    }

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public TodoApplication(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByEmail("rabah@gmail.com").isEmpty()) {
            User user = new User();
            user.setFirstName("Rabah");
            user.setLastName("Touahri");
            user.setEmail("rabah@gmail.com");
            user.setPassword(passwordEncoder.encode("12345"));
            user.setEnabled(true);
            user.setRole(Role.ADMIN);
            userRepository.save(user);
        }
    }
}
