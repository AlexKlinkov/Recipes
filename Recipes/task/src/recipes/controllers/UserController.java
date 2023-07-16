package recipes.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import recipes.dto.UserDTOInputRegistration;
import recipes.services.CommonUserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/register")
public class UserController {
    @Autowired
    private CommonUserService userService;

    @PostMapping
    public ResponseEntity<Object> createUserInSystem(@Valid
                                                         @RequestBody UserDTOInputRegistration userRegistrationInput) {
        if (!isValidEmail(userRegistrationInput.getEmail())) {
            return ResponseEntity.badRequest().build();
        }
        if (!isValidPassword(userRegistrationInput.getPassword())) {
            return ResponseEntity.badRequest().build();
        }
        return userService.createUserInSystem(userRegistrationInput);
    }
    private boolean isValidEmail(String email) {
        return email != null && email.matches(".*@.*\\..*");
    }

    private boolean isValidPassword(String password) {
        return password != null && !password.isBlank() && password.length() >= 8;
    }
}
