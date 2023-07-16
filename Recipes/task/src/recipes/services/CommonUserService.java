package recipes.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import recipes.dto.UserDTOInputRegistration;
import recipes.models.User;
import recipes.repositories.UserRepository;

@Service
public class CommonUserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PasswordEncoder encoder;

    public ResponseEntity<Object> createUserInSystem(UserDTOInputRegistration userRegistrationInput) {
        if (userRepository.existsByEmail(userRegistrationInput.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        User user = new User();
        user.setEmail(userRegistrationInput.getEmail());
        user.getRoles().add(roleService.saveRoleInBD("USER"));
        user.setPassword(encoder.encode(userRegistrationInput.getPassword()));
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
