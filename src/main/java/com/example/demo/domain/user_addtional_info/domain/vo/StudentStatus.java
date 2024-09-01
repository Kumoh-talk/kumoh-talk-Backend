package com.example.demo.domain.user_addtional_info.domain.vo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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
        throw new IllegalArgumentException("학생 상태에 포함되는 값이 아닙니다.");
    }
}
