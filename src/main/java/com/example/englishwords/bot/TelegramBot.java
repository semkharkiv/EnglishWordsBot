package com.example.englishwords.bot;

import com.example.englishwords.config.BotConfig;
import com.example.englishwords.entity.EnglishWord;
import com.example.englishwords.service.TelegramBotService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

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

    public Optional<EnglishWord> getNextWord(){
        return Optional.ofNullable(telegramBotService.getNextWord());
    }
}
