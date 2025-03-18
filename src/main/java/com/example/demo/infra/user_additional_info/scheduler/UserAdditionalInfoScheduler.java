package com.example.demo.infra.user_additional_info.scheduler;

import com.example.demo.infra.user_additional_info.repository.UserAdditionalInfoJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserAdditionalInfoScheduler {

    private final UserAdditionalInfoJpaRepository userAdditionalInfoJpaRepository;

    // 3월 첫 월요일에 실행
    @Scheduled(cron = "0 0 0 1-7 3 ?")
    public void checkAcademicInfoUpdateMarch() {
        if (LocalDate.now().getDayOfWeek() == DayOfWeek.MONDAY) {
            updateUserAcademicInfoStatus();
        }
    }

    // 9월 첫 월요일에 실행
    @Scheduled(cron = "0 0 0 1-7 9 ?")
    public void checkAcademicInfoUpdateSeptember() {
        if (LocalDate.now().getDayOfWeek() == DayOfWeek.MONDAY) {
            updateUserAcademicInfoStatus();
        }
    }

    private void updateUserAcademicInfoStatus() {
        userAdditionalInfoJpaRepository.updateAllIsUpdatedToFalse();
        log.info("모든 학생들의 학적 정보 변경 여/부가 false 로 변경되었습니다.");
    }
}
