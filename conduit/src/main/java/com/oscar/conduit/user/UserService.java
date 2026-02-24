package com.oscar.conduit.user;

import java.util.function.Consumer;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.oscar.conduit.auth.dto.request.LoginRequest;
import com.oscar.conduit.auth.dto.request.RegisterRequest;
import com.oscar.conduit.dto.response.UserResponse;
import com.oscar.conduit.exception.InvalidEmailOrPasswordException;
import com.oscar.conduit.exception.UserNotFoundException;
import com.oscar.conduit.security.JwtService;
import com.oscar.conduit.user.dto.request.UpdateUserRequest;

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

  public User getUserByUsername(String username) {
    return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException());
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

  public UserResponse getCurrentUser(Long id) {
    User user = getUserById(id);
    return UserResponse.from("", user);
  }

  public UserResponse updateUser(Long id, UpdateUserRequest request) {
    User user = getUserById(id);
    UpdateUserRequest.User updateData = request.user();

    updateIfPresent(user::setBio, updateData.bio());
    updateIfPresent(user::setEmail, updateData.email());
    updateIfPresent(user::setUsername, updateData.username());
    updateIfPresent((value) -> {
      String hashed = passwordEncoder.encode(value);
      user.setpasswordHash(hashed);
    }, updateData.password());
    updateIfPresent(user::setImage, updateData.image());

    return UserResponse.from("", userRepository.save(user));
  }

  private <T> void updateIfPresent(Consumer<T> setter, T value) {
    if (value != null) {
      setter.accept(value);
    }
  }

}
