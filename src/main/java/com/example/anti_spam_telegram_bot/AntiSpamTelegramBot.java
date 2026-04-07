package com.example.anti_spam_telegram_bot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class AntiSpamTelegramBot extends TelegramLongPollingBot {

	private final String botUsername;
	private final String botToken;
	private final WebClient webClient;

	public AntiSpamTelegramBot(
			@Value("${bot.name}") String botUsername,
			@Value("${bot.token}") String botToken,
			@Value("${model.service.url:http://127.0.0.1:8001}") String serviceUrl
	) {
		this.botUsername = botUsername;
		this.botToken = botToken;
		this.webClient = WebClient.create(serviceUrl);
	}

	@Override
	public void onUpdateReceived(Update update) {
		if (!update.hasMessage()) {
			return;
		}

		Message message = update.getMessage();

		if (!message.hasText()) {
			return;
		}

		String text = message.getText();

		try {
			// Проверяем на спам
			Map result = checkSpam(text).block();
			boolean isSpam = (Boolean) result.get("spam");

			SendMessage response = new SendMessage();
			response.setChatId(message.getChatId().toString());

			if (isSpam) {
				response.setText("Обнаружен спам! Сообщение: " + text);
			} else {
				response.setText("Hello, " + message.getFrom().getFirstName() + "! You said: " + text);
			}

			execute(response);
		} catch (Exception e) {
			// В случае ошибки, просто отвечаем как обычно
			SendMessage response = new SendMessage();
			response.setChatId(message.getChatId().toString());
			response.setText("Hello, " + message.getFrom().getFirstName() + "! You said: " + text + " (ошибка проверки спама)");
			try {
				execute(response);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	private Mono<Map> checkSpam(String text) {
		return webClient.post()
			.uri("/predict")
			.bodyValue(Map.of("text", text))
			.retrieve()
			.bodyToMono(Map.class);
	}

	@Override
	public String getBotUsername() {
		return botUsername;
	}

	@Override
	public String getBotToken() {
		return botToken;
	}
}