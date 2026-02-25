package com.oscar.conduit.follow;

import org.springframework.stereotype.Service;

import com.oscar.conduit.dto.response.ProfileResponse;
import com.oscar.conduit.user.User;
import com.oscar.conduit.user.UserService;

@Service
public class FollowService {
  private final FollowRepository followRepository;
  private final UserService userService;

  public FollowService(FollowRepository followRepository, UserService userService) {
    this.followRepository = followRepository;
    this.userService = userService;
  }

  public ProfileResponse getProfile(String id, String username) {
    User user = userService.getUserByUsername(username);
    boolean exists = id != null && followRepository.existsByFollowerIdAndFollowingId(Long.parseLong(id), user.getId());

    return ProfileResponse.from(user, exists);
  }

  public ProfileResponse followUser(String userId, String username) {
    Long id = Long.parseLong(userId);
    User following = userService.getUserByUsername(username);
    boolean exists = followRepository.existsByFollowerIdAndFollowingId(id, following.getId());

    if (!exists) {
      User follower = userService.getUserById(id);
      Follow follow = new Follow(follower, following);
      followRepository.save(follow);
    }

    return ProfileResponse.from(following, true);
  }

  public ProfileResponse unfollowUser(String userId, String username) {
    Long id = Long.parseLong(userId);
    User following = userService.getUserByUsername(username);
    followRepository.findByFollowerIdAndFollowingId(id, following.getId()).ifPresent(
        follow -> followRepository.delete(follow));

    return ProfileResponse.from(following, true);
  }

}
