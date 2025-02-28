package com.example.demo.base;

import com.example.demo.global.config.db.QueryDSLConfig;
import com.example.demo.infra.builder.JpaBuilderSupporter;
import com.example.demo.infra.builder.JpaTestFixtureBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Import({QueryDSLConfig.class, JpaTestFixtureBuilder.class, JpaBuilderSupporter.class})
public abstract class RepositoryTest {

	@Autowired
	protected JpaTestFixtureBuilder testFixtureBuilder;
}
