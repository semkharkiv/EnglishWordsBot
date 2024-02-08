package com.example.englishwords.bot;

import com.example.englishwords.config.BotConfig;
import com.example.englishwords.entity.EnglishWord;
import com.example.englishwords.service.TelegramBotService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@AllArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {

    private TelegramBotService telegramBotService;

    private final BotConfig botConfig;

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        SendMessage sendMessage = telegramBotService.handleUpdate(update);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public void onUpdateReceived(Update update) {
//        // Получение текста из входящего сообщения
//        String messageText = update.getMessage().getText();
//
//        // Обработка входящего сообщения с использованием EnglishWordsBot
//        String response = telegramBotService.processIncomingMessage(messageText);
//
//        // Отправка ответа пользователю
//        sendResponse(update.getMessage().getChatId(), response);
//    }
//
//    private void sendResponse(Long chatId, String response) {
//        SendMessage message = new SendMessage();
//        message.setChatId(chatId);
//        message.setText(response);
//
//        try {
//            execute(message);
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
//    }

    public String getNextWord(){
        return telegramBotService.getNextWord();
    }
}
