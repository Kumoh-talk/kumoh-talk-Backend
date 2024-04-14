//package com.example.demo.domain.category.repository;
//
//
//import com.example.demo.base.RepositoryTest;
//import com.example.demo.domain.category.domain.entity.Category;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.*;
//
//public class CategoryRepositoryTest extends RepositoryTest {
//
//    @Autowired
//    private CategoryRepository categoryRepository;
//
//
//    private Category category;
//
//
//    @AfterEach
//    void afterEach() {
//        category = null;
//    }
//
//    @Test
//    @DisplayName("카테고리 이름을 찾기 성공")
//    void name_success() {
//        //given -> when
//        String category1 = "category1";
//        category = new Category(category1);
//        categoryRepository.save(category);
//
//        String category2 = "category2";
//        category = new Category(category2);
//        categoryRepository.save(category);
//
//        String category3 = "category3";
//        category = new Category(category3);
//        categoryRepository.save(category);
//
//
//        //then
//        List<Category> byName = categoryRepository.findByName(category1);
//        Category savedCate = byName.stream().findAny().get();
//        assertThat(savedCate.getName()).isEqualTo(category1);
//        assertThat(byName.size()).isEqualTo(1);
//
//    }
//
//    @Test
//    @DisplayName("카테고리 없는 이름 찾기 성공")
//    void none_name_success() {
//        //given -> when
//        String category1 = "category1";
//        category = new Category(category1);
//        categoryRepository.save(category);
//
//        String category2 = "category2";
//        category = new Category(category2);
//        categoryRepository.save(category);
//
//        String category3 = "category3";
//        category = new Category(category3);
//        categoryRepository.save(category);
//
//        //then
//        List<Category> byName = categoryRepository.findByName("not category");
//        assertThat(byName.size()).isEqualTo(0);
//
//    }
//}
