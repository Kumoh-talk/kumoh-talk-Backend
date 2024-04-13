package com.example.demo.domain.file.controller;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;

@WebMvcTest(value = FileController.class)
@AutoConfigureRestDocs
@ExtendWith(MockitoExtension.class)
@WithMockUser
class FileControllerTest {

}