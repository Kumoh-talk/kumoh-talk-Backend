package com.example.demo.domain.post.post_qna.repository;

import com.example.demo.domain.post.post_qna.domain.Post_Qna;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QnaRepository  extends JpaRepository<Post_Qna, Long> {
}
