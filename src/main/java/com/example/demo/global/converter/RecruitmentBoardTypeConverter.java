package com.example.demo.global.converter;

import com.example.demo.domain.recruitment_board.entity.vo.RecruitmentBoardType;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import org.springframework.core.convert.converter.Converter;

public class RecruitmentBoardTypeConverter implements Converter<String, RecruitmentBoardType> {
    @Override
    public RecruitmentBoardType convert(String source) {
        for (RecruitmentBoardType recruitmentBoardType : RecruitmentBoardType.values()) {
            if (recruitmentBoardType.toString().equalsIgnoreCase(source)) {
                return recruitmentBoardType;
            }
        }
        throw new ServiceException(ErrorCode.RECRUITMENT_BOARD_TYPE_PARSE_ERROR);
    }
}
