package com.example.demo.domain.fake.entity;

import com.example.demo.domain.user_addtional_info.vo.StudentStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
public class FakeUserAdditionalInfo {
    public final String fakeEmail;
    public final String fakeDepartment;
    public final int fakeStudentId;
    public final int fakeGrade;
    public final StudentStatus fakeStudentStatus;
    public final String fakePhoneNumber;

    @Builder
    public FakeUserAdditionalInfo(String fakeEmail, String fakeDepartment, int fakeStudentId, int fakeGrade, StudentStatus fakeStudentStatus, String fakePhoneNumber) {
        this.fakeEmail = fakeEmail;
        this.fakeDepartment = fakeDepartment;
        this.fakeStudentId = fakeStudentId;
        this.fakeGrade = fakeGrade;
        this.fakeStudentStatus = fakeStudentStatus;
        this.fakePhoneNumber = fakePhoneNumber;
    }
}
