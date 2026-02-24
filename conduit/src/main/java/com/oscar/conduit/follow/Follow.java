package com.oscar.conduit.follow;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.oscar.conduit.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "follows")
public class Follow {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "follower_id", nullable = false)
  private User follower;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "following_id", nullable = false)
  private User following;

  @CreationTimestamp
  private LocalDateTime createdAt;

  public Follow() {
  }

  public Follow(User follower, User following) {
    this.follower = follower;
    this.following = following;
  }

  public User getFollower() {
    return follower;
  }

  public void setFollower(User follower) {
    this.follower = follower;
  }

  public User getFollowing() {
    return following;
  }

  public void setFollowing(User following) {
    this.following = following;
  }
}
