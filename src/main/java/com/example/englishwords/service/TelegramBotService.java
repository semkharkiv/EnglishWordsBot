package com.example.englishwords.service;

import com.example.englishwords.entity.EnglishWord;
import com.example.englishwords.repository.EnglishWordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

@Service
public class TelegramBotService {

    private EnglishWordRepository wordRepository;

    @Autowired
    public TelegramBotService(EnglishWordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    private static final String ANSWER = " ,hi, nice to meet you!" + "\n" +
            "Go learn English together";

    public SendMessage handleUpdate(Update update) {

        long chatId = update.getMessage().getChatId();
        String message = "";

        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();

            if (messageText.equals("/start")) {
                return startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
            }
        }
        return sendMessage(chatId, message);
    }

    public String getNextWord() {
        Optional<EnglishWord> nextWordOptional = wordRepository.findFirstById();
        return nextWordOptional.map(EnglishWord::getWord).orElse("No more words available.");
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