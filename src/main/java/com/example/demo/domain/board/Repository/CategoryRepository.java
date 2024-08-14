package com.example.demo.domain.board.Repository;

import com.example.demo.domain.board.domain.dto.response.BoardTitleInfoResponse;
import com.example.demo.domain.board.domain.entity.Category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {

    Optional<Category> findByName(String name);

    @Query("SELECT new com.example.demo.domain.board.domain.dto.response.BoardTitleInfoResponse"
        + "(b.id, b.title,b.user.nickname,b.tag,count(v) ,count(l), b.createdAt) "
        + "FROM Board b "
        + "left join Like l on b.id = l.board.id "
        + "left join View v on b.id = v.board.id "
        + "left join BoardCategory bc on b.id = bc.board.id "
        + "left join Category c on bc.category.id = c.id "
        + "where c.name = :categoryName")
    Page<BoardTitleInfoResponse> findBoardPageByCategoryName(String categoryName, Pageable pageable); //TODO : 추후 QueryDSL로 변경
}
