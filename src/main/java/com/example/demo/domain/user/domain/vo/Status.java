package com.example.demo.domain.user.domain.vo;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Status {
    ATTENDING("재학"),
    DEFERRAL("휴학"),
    GRADUATE("졸업");

    private String status;

}