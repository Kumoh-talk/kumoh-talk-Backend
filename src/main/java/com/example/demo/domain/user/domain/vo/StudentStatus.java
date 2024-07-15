package com.example.demo.domain.user.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum StudentStatus {
    LEAVE_OF_ABSENCE("휴학"),
    ENROLLED("재학"),
    GRADUATED("졸업");

    String status;

}
