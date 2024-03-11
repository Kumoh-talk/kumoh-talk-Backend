package com.example.demo.domain.post.Repository;

import com.example.demo.domain.post.domain.Post;
import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.domain.vo.Track;
import com.example.demo.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class PostRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PostRepository postRepository;

    @Test
    void save() {
        Post post = new Post();
        post.setContents("contenst");
        post.setTitle("title");
        post.setTrack(Track.BACK);
        // given
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm:ss");
        User user = User.builder()
                .id(1L)
                .name("테스트")
                .email("test@kumoh.ac.kr")
                .password("password")
                .track(Track.BACK)
                .major("컴퓨터공학과")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        userRepository.save(user);
        post.setUser(user);
        postRepository.save(post);
    }





}