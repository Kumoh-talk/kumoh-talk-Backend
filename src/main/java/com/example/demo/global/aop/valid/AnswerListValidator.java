package com.example.demo.global.aop.valid;


import com.example.demo.application.recruitment_board.dto.request.RecruitmentFormQuestionRequest;
import com.example.demo.domain.recruitment_board.entity.vo.QuestionType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AnswerListValidator implements ConstraintValidator<ValidAnswerList, RecruitmentFormQuestionRequest> {
    @Override
    public boolean isValid(RecruitmentFormQuestionRequest obj, ConstraintValidatorContext context) {
        if (obj.getType() == QuestionType.DESCRIPTION) {
            return true;
        } else {
            return obj.getAnswerList() != null && !obj.getAnswerList().isEmpty();
        }
    }
}