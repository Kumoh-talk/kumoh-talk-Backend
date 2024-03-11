package com.example.demo.domain.post.Repository;

import com.example.demo.domain.post.domain.Post;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    @EntityGraph(attributePaths = {"comments"})
    Optional<Post> findByIdWithComments(Long id);

    @EntityGraph(attributePaths = {"postQnas"})
    Optional<Post> findByIdWithPostQnas(Long id);
}
