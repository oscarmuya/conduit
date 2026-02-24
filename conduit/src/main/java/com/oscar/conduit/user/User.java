package com.oscar.conduit.user;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String email;

  @Column(unique = true)
  private String username;

  private String bio;
  private String image;
  private String passwordHash;

  @CreatedDate
  @Column(updatable = false, nullable = false)
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column(nullable = false)
  private LocalDateTime updatedAt;

  public User() {
  }

  public User(String email, String username, String bio, String image, String passwordHash) {
    this.email = email;
    this.username = username;
    this.bio = bio;
    this.image = image;
    this.passwordHash = passwordHash;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setpasswordHash(String passwordHash) {
    this.passwordHash = passwordHash;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setBio(String bio) {
    this.bio = bio;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public String getEmail() {
    return email;
  }

  public String getpasswordHash() {
    return passwordHash;
  }

  public Long getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public String getBio() {
    return bio;
  }

  public String getImage() {
    return image;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

}
