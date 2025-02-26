package com.example.demo.application.user_additional_info.dto.vo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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

    @JsonValue
    public String getStatus() {
        return status;
    }

    @JsonCreator
    public static StudentStatus fromString(String status) {
        for (StudentStatus studentStatus : StudentStatus.values()) {
            if (studentStatus.status.equals(status)) {
                return studentStatus;
            }
        }
        return null;
    }
}
