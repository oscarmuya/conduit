package com.oscar.conduit.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.oscar.conduit.auth.dto.request.LoginRequest;
import com.oscar.conduit.auth.dto.request.RegisterRequest;
import com.oscar.conduit.exception.InvalidEmailOrPasswordException;
import com.oscar.conduit.exception.UserNotFoundException;

@Service
public class UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public User getUserById(Long id) {
    return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException());
  }

  public User getUserByEmail(String email) {
    return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException());
  }

  public User create(RegisterRequest request) {
    String passwordHash = passwordEncoder.encode(request.user().password());
    User user = new User(
        request.user().email(),
        request.user().username(),
        "", "", passwordHash);

    return userRepository.save(user);
  }

  public User login(LoginRequest request) {
    User user = userRepository.findByEmail(request.user().email()).orElseThrow(() -> new UserNotFoundException());
    boolean isValid = passwordEncoder.matches(request.user().password(), user.getpasswordHash());
    if (!isValid) {
      throw new InvalidEmailOrPasswordException();
    }

    return user;
  }

  public boolean userExistsByEmail(String email) {
    return userRepository.existsByEmail(email);
  }

}
