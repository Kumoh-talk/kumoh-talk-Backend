//package com.example.demo.base;
//
//import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
//import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
//
//import com.example.demo.domain.auth.api.AuthController;
//import com.example.demo.domain.auth.application.AuthService;
//import com.example.demo.domain.user.api.UserController;
//import com.example.demo.domain.user.application.UserService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
//import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//
//@WebMvcTest(
//        value = {
//                AuthController.class, UserController.class
//        },
//        excludeAutoConfiguration = {SecurityAutoConfiguration.class}
//)
//@AutoConfigureRestDocs
//@WithMockUser
//public abstract class ControllerTest {
//
//    @Autowired
//    protected ObjectMapper objectMapper;
//
//    @Autowired
//    protected MockMvc mockMvc;
//
//    @MockBean
//    protected AuthService authService;
//
//    @MockBean
//    protected UserService userService;
//
//
//}
