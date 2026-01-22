package com.zaalima.hospital;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createUser(String username, String rawPassword, String role) {
        User u = new User();
        u.setUsername(username);
        u.setPassword(passwordEncoder.encode(rawPassword));
        u.setRole(role);

        userRepository.save(u); 
        userRepository.flush();
        System.out.println(" userRepository.save() executed");
    }

    public User authenticate(String username, String password) {
        User u = userRepository.findByUsername(username)
                .orElseThrow(()-> new RuntimeException("User not found"));
                if(passwordEncoder.matches(password,u.getPassword())){
                    return u;
                }else{
                    throw new RuntimeException("Invalid credentials");
                }
    }
}
