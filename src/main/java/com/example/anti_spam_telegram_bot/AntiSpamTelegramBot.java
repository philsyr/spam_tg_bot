package com.example.anti_spam_telegram_bot;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import com.example.anti_spam_telegram_bot.utils.PythonServerClient;

@Service
public class AntiSpamTelegramBot extends TelegramLongPollingBot {

	private final String BOT_USERNAME = "anti_spam_tg_channels_bot";  // Replace with your bot username
	private final String BOT_TOKEN = "7753305953:AAEFJcit9w62W9YAjaDqGhnM_5Ewcqz1gc8";  // Replace with your bot token
	private PythonServerClient pythonServerClient = new PythonServerClient();

	@Override
	public void onUpdateReceived(Update update) {
		// Check if the update contains a message
		if (update.hasMessage()) {
			Message message = update.getMessage();

			// Create a SendMessage object to reply to the message
			SendMessage response = new SendMessage();
			response.setChatId(message.getChatId().toString());  // Make sure to pass the chat ID
			response.setText("Hello, " + message.getFrom().getFirstName() + "! You said: " + message.getText() + "\n\n" + "Это " + pythonServerClient.check_for_spam(message.getText()));

			try {
				// Execute the message
				execute(response);  // Sending the response to the user
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public String getBotUsername() {
		return BOT_USERNAME;
	}

	@Override
	public String getBotToken() {
		return BOT_TOKEN;
	}
}
