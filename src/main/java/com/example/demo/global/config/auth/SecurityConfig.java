package com.example.demo.global.config.auth;

import com.example.demo.global.jwt.CustomAccessDeniedHandler;
import com.example.demo.global.jwt.CustomAuthenticationEntryPoint;
import com.example.demo.global.jwt.JwtAuthenticationFilter;
import com.example.demo.global.jwt.TokenProvider;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final CustomAuthenticationEntryPoint authenticationEntryPoint;
	private final CustomAccessDeniedHandler accessDeniedHandler;

	@Bean
	public SecurityFilterChain httpSecurity(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
		http
			.csrf(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.sessionManagement((s) -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.rememberMe(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.logout(AbstractHttpConfigurer::disable)
			.requestCache(RequestCacheConfigurer::disable)

			.authorizeHttpRequests((authorizeRequests) -> authorizeRequests
				.requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
                .requestMatchers(HttpMethod.GET).permitAll()
				.anyRequest().authenticated())

            .addFilterAfter(new JwtAuthenticationFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class)


			.exceptionHandling(e -> e
				.accessDeniedHandler(accessDeniedHandler)
				.authenticationEntryPoint(authenticationEntryPoint))
		;

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

    @Bean
    public AuthenticationManager authenticationManager(
        TokenProvider tokenProvider
    ){
        ProviderManager providerManager = new ProviderManager(tokenProvider);
        providerManager.setEraseCredentialsAfterAuthentication(true);
        return providerManager;
    }
}
