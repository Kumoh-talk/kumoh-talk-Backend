package com.example.demo.domain.user.vo;

public enum Role {
    ROLE_GUEST, // 정보가 존재하지 않는 첫 로그인한 사용자
    ROLE_USER, // 일반 사용자 (댓글 작성 및 뉴스레터 구독 같은 자잘한 활동 가능)
    ROLE_ACTIVE_USER, // 추가정보를 입력한 사용자 (세미나 신청, 스터디/프로젝트 글 작성 및 신청 가능)
    ROLE_SEMINAR_WRITER, // 세미나 신청을 한 번 이상한 사용자 (세미나 글 작성 가능)
    ROLE_ADMIN // 관리용 계정
}
