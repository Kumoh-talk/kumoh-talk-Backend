package com.example.demo.domain.post.Repository;

import com.example.demo.domain.post.domain.Post;
import com.example.demo.domain.user.domain.vo.Track;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p JOIN FETCH p.comments q WHERE p.id = :id")
    Optional<Post> findPostByIdWithComments(@Param("id") Long id);

    @Query("SELECT p FROM Post p JOIN FETCH p.postQnas q WHERE p.id = :id")
    Optional<Post> findPostByIdWithPostQnas(@Param("id") Long id);

    Page<Post> findAllByTrack(Track track, PageRequest pageRequest);
}

