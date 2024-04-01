package com.example.demo.global.config.auth;

import com.example.demo.global.utils.HttpServletUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final HttpServletUtil httpServletUtil;

    @Bean
    public SecurityFilterChain httpSecurity(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorizeRequests) -> authorizeRequests
                        .requestMatchers(
                                new AntPathRequestMatcher("/demo"),
                                new AntPathRequestMatcher("/api/auth/**"),
                                new AntPathRequestMatcher("/docs/**")
                        ).permitAll()
                        .anyRequest().authenticated())

//                .exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint)
//                .and()
//                .addFilterBefore(new JwtFilter(tokenProvider, refreshTokenRepository, httpServletUtil), UsernamePasswordAuthenticationFilter.class)
        ;


        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
