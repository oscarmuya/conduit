package com.oscar.conduit.article;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
  boolean existsByName(String name);

  Optional<Tag> findByName(String name);

}
