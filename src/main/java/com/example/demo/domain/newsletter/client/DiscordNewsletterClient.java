package com.example.demo.domain.newsletter.client;

import com.example.demo.domain.report.client.DiscordMessage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "discord-newsletter-client",
        url = "${discord.webhook.url.newsletter}")
public interface DiscordNewsletterClient {

    @PostMapping
    void sendNewsletter(@RequestBody DiscordMessage message);
}
