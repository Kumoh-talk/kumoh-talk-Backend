package com.example.demo.global.regex;

public interface UserRegex {
    // 5~20자의 영문 소문자, 숫자와 특수기호(_),(-)만 사용 가능
    String USERID_REGEXP = "^[a-z0-9_-]{5,20}$";

    // 한글 2글자 ~ 6글자 / 영어 4글자 ~ 8글자 / 다른 글자 X
    String NICKNAME_REGEXP = "^(([가-힣]{2,6})|([A-Za-z]{4,8}))$";

    // 이메일 형식
    String EMAIL_REGEXP = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    // 8~16자의 영문 대/소문자, 숫자, 특수문자를 사용
    String PASSWORD_REGEXP = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()\\-_=+\\[\\]{}|;:'\",.<>/?~]).{8,16}$";

}