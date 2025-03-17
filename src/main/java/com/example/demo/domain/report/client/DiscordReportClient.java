package com.example.demo.domain.report.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "discord-report-client",
        url = "${discord.webhook.url.report}")
public interface DiscordReportClient {

    @PostMapping
    void sendReport(@RequestBody DiscordMessage message);
}
