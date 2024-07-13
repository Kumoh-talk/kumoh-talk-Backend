package com.example.demo.global.jwt.refresh_token;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "refreshToken", timeToLive = 1209600)
@AllArgsConstructor
@Getter
public class RefreshToken {

    @Id
    private String refreshToken;
    private Long userId;
}
