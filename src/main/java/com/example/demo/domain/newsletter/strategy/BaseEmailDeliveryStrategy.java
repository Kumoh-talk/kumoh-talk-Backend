package com.example.demo.domain.newsletter.strategy;

public abstract class BaseEmailDeliveryStrategy implements EmailDeliveryStrategy {
    protected String youtubeUrl = "https://www.youtube.com/@midnight_kumoh_talk";
    protected String changeSubscribeUrl = "https://FrontUrl/change"; // TODO. 프론트 배포 후 변경 필요
    protected String cancelSubscribeUrl = "https://FrontUrl/cancel"; // TODO. 프론트 배포 후 변경 필요
}