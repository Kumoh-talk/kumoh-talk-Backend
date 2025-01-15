package com.example.demo.domain.board.controller;


import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class BoardControllerIntegrationTest {

	@Autowired
	private Environment environment;

	@Test
	public void testActiveProfile() {
		String[] activeProfiles = environment.getActiveProfiles();
		assertEquals("test", activeProfiles[0]);
	}
}
