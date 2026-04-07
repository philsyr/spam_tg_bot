package com.example.anti_spam_telegram_bot.config;

import com.example.anti_spam_telegram_bot.AntiSpamTelegramBot;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
public class BotInitializer {

    private final AntiSpamTelegramBot telegramBot;

    public BotInitializer(AntiSpamTelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void init() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(telegramBot);
        } catch (Exception e) {
            throw new RuntimeException("Failed to register bot", e);
        }
    }
}