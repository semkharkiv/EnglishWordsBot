package com.example.englishwords.bot;

import com.example.englishwords.config.BotConfig;
import com.example.englishwords.entity.EnglishWord;
import com.example.englishwords.service.TelegramBotService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Autowired
    private TelegramBotService telegramBotService;

    private final BotConfig botConfig;

    public TelegramBot(BotConfig botConfig) {
        this.botConfig = botConfig;
        List<BotCommand> listCommands = new ArrayList<>();
        listCommands.add(new BotCommand("/start","Get to start"));
        listCommands.add(new BotCommand("/go","Go learn together"));
        try{
            this.execute(new SetMyCommands(listCommands, new BotCommandScopeDefault(),"en"));
        } catch (TelegramApiException e) {
            e.printStackTrace();
//            log.error("Error setting bot's command list: " + e.getMessage());
        }
    }

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
//            log.error("Error occurred:" + e.getMessage());
        }
    }

    public Optional<EnglishWord> getNextWord(){
        return Optional.ofNullable(telegramBotService.getNextWord());
    }
}
