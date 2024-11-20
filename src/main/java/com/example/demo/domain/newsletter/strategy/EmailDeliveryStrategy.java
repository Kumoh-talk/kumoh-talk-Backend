package com.example.demo.domain.newsletter.strategy;

import java.util.Map;

public interface EmailDeliveryStrategy {
    String getTemplateName();
    Map<String, Object> getVariables();
    String getSubject();
}
