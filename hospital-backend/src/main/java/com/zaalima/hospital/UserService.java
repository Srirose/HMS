package com.zaalima.hospital;

import com.zaalima.hospital.Doctor.Doctor;
import com.zaalima.hospital.Doctor.DoctorRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       DoctorRepository doctorRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createUser(String username, String rawPassword, String role) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username '" + username + "' is already taken.");
        }
        User u = new User();
        u.setUsername(username);
        u.setPassword(passwordEncoder.encode(rawPassword));
        u.setRole(role);

        userRepository.save(u); 
        userRepository.flush();
        System.out.println(" userRepository.save() executed");

        if ("DOCTOR".equalsIgnoreCase(role)) {
            Doctor doctor = new Doctor();
            doctor.setUsername(username);
            doctor.setFullName(username); // Using username as default full name
            doctor.setSpecialization("General Physician"); // Default specialization
            doctor.setActive(true);
            doctorRepository.save(doctor);
            System.out.println(" Doctor record created for user: " + username);
        }
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
