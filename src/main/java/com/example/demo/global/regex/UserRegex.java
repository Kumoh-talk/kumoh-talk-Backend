package com.example.demo.global.regex;

public class UserRegex {
    // 2~20, 한글, 영문 대소문자, 숫자, '[]<>-' 기호 허용
    public static final String NAME_REGEXP = "^[가-힣a-zA-Z\\d\\[\\]<>-]{2,20}";

    // 일반적인 이메일 형식 체크
    public static final String EMAIL_REGEXP = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";

    // 최소 8, 최대 25자 이내. 적어도 하나의 영문자와 하나의 숫자를 포함해야 한다.
    public static final String PASSWORD_REGEXP = "^(?=.*[a-zA-Z])(?=.*[0-9]).{8,25}$";
}