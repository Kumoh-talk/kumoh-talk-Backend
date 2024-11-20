package com.example.demo.domain.newsletter.strategy;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ByPassEmailDeliveryStrategy extends BaseEmailDeliveryStrategy {
    private String subject;
    private String htmlContent;

    @Override
    public String getTemplateName() {
        return "seminar_notice";
    }

    @Override
    public Map<String, Object> getVariables() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("youtubeUrl", youtubeUrl);
        variables.put("changeSubscribeUrl", changeSubscribeUrl);
        variables.put("cancelSubscribeUrl", cancelSubscribeUrl);
        return variables;
    }

    public static ByPassEmailDeliveryStrategy create(String subject, String htmlContent) {
        return new ByPassEmailDeliveryStrategy(subject, htmlContent);
    }
}