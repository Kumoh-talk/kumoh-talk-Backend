package com.example.demo.infra.token.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "refreshToken", timeToLive = 1209600)
@AllArgsConstructor
@Getter
public class RefreshToken {

    @Id
    private Long userId;
    private String refreshToken;
}
