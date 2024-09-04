package com.example.demo.global.aop.valid;


import com.example.demo.domain.study_project_board.domain.dto.request.StudyProjectFormQuestionRequest;
import com.example.demo.domain.study_project_board.domain.dto.vo.QuestionType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AnswerListValidator implements ConstraintValidator<ValidAnswerList, StudyProjectFormQuestionRequest> {
    @Override
    public boolean isValid(StudyProjectFormQuestionRequest obj, ConstraintValidatorContext context) {
        if (obj.getType() == QuestionType.DESCRIPTION) {
            return true;
        } else {
            return obj.getAnswerList() != null && !obj.getAnswerList().isEmpty();
        }
    }
}