package com.oscar.conduit.follow;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRespository extends JpaRepository<Follow, Long> {
  boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);

  Optional<Follow> findByFollowerIdAndFollowingId(Long followerId, Long followingId);

}
