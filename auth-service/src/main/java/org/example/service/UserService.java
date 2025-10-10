package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.RegisterRequest;
import org.example.dto.UserDTO;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public UserDTO register(RegisterRequest request){
        if (userRepository.existsByUsername(request.getUsername())){
            throw  new IllegalArgumentException("Username already exists!!");
        }
        if (userRepository.existsByEmail(request.getEmail())){
            throw new IllegalArgumentException("Email already exists!!");
        }
        User user= User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        user.getRoles().add("ROLE_USER");

        User saved = userRepository.save(user);

        return new UserDTO(saved.getId(),saved.getUsername(), saved.getEmail());
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(u -> new UserDTO(u.getId(), u.getUsername(), u.getEmail()))
                .collect(Collectors.toList());
    }

}

