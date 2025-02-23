package com.example.demo.infra.board.Repository;

import com.example.demo.application.board.dto.response.BoardTitleInfoResponse;
import com.example.demo.infra.board.entity.Category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {

    Optional<Category> findByName(String name);

    @Query("SELECT new com.example.demo.application.board.dto.response.BoardTitleInfoResponse"
        + "(b.id, b.title, b.user.nickname, b.boardType, b.viewCount, COUNT(DISTINCT l),b.headImageUrl, b.createdAt) "
        + "FROM Board b "
        + "LEFT JOIN b.likes l "
        + "LEFT JOIN BoardCategory bc ON b.id = bc.board.id "
        + "LEFT JOIN Category c ON bc.category.id = c.id "
        + "WHERE c.name = :categoryName "
        + "GROUP BY b.id, b.title, b.user.nickname, b.boardType, b.createdAt")
    Page<BoardTitleInfoResponse> findBoardByPage(String categoryName, Pageable pageable);//TODO : 추후 QueryDSL로 변경
}
