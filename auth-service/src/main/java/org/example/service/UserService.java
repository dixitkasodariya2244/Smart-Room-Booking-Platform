package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.RegisterRequest;
import org.example.dto.UserDTO;
import org.example.exceptionHandling.UserNotFoundException;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
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

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        return new UserDTO(user.getId(), user.getUsername(), user.getEmail());
    }

    public UserDTO updateUser(Long id, RegisterRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);
        return new UserDTO(user.getId(), user.getUsername(), user.getEmail());
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

}

