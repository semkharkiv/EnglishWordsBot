package com.example.englishwords.service;

import com.example.englishwords.config.BotConfig;
import com.example.englishwords.entity.EnglishWord;
import com.example.englishwords.repository.EnglishWordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Service
public class TelegramBotService {

    private EnglishWordRepository wordRepository;

    private static final String ANSWER = " ,hi, nice to meet you!" + "\n" +
            "Go learn English together";
    private EnglishWord currentWord;
    private Long currentId = 1L;

    @Autowired
    public TelegramBotService(EnglishWordRepository wordRepository) {
        this.wordRepository = wordRepository;
        this.currentWord = getNextWord();
    }

    public SendMessage handleUpdate(Update update) {

        long chatId = update.getMessage().getChatId();
        String messageText = update.getMessage().getText();

        if (update.hasMessage() && update.getMessage().hasText()) {
            if (messageText.equals("/start")) {
                return startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
            }
            if (messageText.equals("/go")) {
                EnglishWord word = getNextWord();
                return new SendMessage(String.valueOf(chatId), word.getWord());
            }
            return processUserAnswer(chatId,messageText);
        }
        return sendMessage(chatId, "Unknown command. Type '/start' or 'go'.");
    }

    public EnglishWord getNextWord() {
        return wordRepository.findEnglishWordById(currentId)
                .orElseThrow(() -> new RuntimeException("Word not found"));
    }

    private SendMessage processUserAnswer(Long chatId,String userAnswer) {
        String correctTranslation = getNextWord().getTranslate();
        currentId++;
        if (userAnswer.equalsIgnoreCase(correctTranslation)) {
            EnglishWord nextWord = getNextWord();
            return new SendMessage(String.valueOf(chatId),"Correct! Next word: " + nextWord.getWord());
        } else {
            return  new SendMessage(String.valueOf(chatId),"Incorrect.Correct translation: " + correctTranslation
                        + " Next word: " + getNextWord().getWord());
        }
    }

    private SendMessage startCommandReceived(Long chatId, String name) {
        String answer = name + ANSWER;
        return sendMessage(chatId, answer);
    }

    private SendMessage sendMessage(Long chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);

        return sendMessage;
    }
}