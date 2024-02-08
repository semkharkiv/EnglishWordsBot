package com.example.englishwords.service;

import com.example.englishwords.entity.EnglishWord;
import com.example.englishwords.repository.EnglishWordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;
//todo Specified result type [java.lang.String] did not match Query selection type [com.example.englishwords.entity.EnglishWord] - multiple selections: use Tuple or array
@Service
public class TelegramBotService {

    private EnglishWordRepository wordRepository;

    @Autowired
    public TelegramBotService(EnglishWordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    private static final String ANSWER = " ,hi, nice to meet you!" + "\n" +
            "Go learn English together";

    private boolean isWaitingForAnswer = false;

    private long currentWordId = 1L;

    public SendMessage handleUpdate(Update update) {

        long chatId = update.getMessage().getChatId();
        String message = "";

        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();

            if (messageText.equals("/start")) {
                return startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
            }
            if (messageText.equals("go")) {
                processIncomingMessage(messageText);
                return new SendMessage(String.valueOf(chatId),getNextWord());
            }
        }
        return sendMessage(chatId, "Unknown command. Type '/start' or 'go'.");
    }

    public String getNextWord() {
        return wordRepository.getWordById(currentWordId);
    }

    public String processIncomingMessage(String messageText) {
        if (isWaitingForAnswer) {
            // Если ожидается ответ от пользователя, обрабатываем его
            isWaitingForAnswer = false;
            return processUserAnswer(messageText);
        } else {
            // Если не ожидается ответ, обрабатываем как запрос на новое слово
            return "Слово: " + getNextWord();
        }
    }

    private String processUserAnswer(String userAnswer) {
        String response = processUserAnswerLogic(userAnswer);

        // Устанавливаем флаг ожидания ответа от пользователя
        isWaitingForAnswer = true;

        return response;
    }

    private String processUserAnswerLogic(String userAnswer) {
        String correctTranslation = getCorrectTranslation();

        if (userAnswer.equalsIgnoreCase(correctTranslation)) {
            currentWordId++;
            // Если ответ правильный, возвращаем сообщение "Правильно" и следующее слово
            return "Правильно! Следующее слово: " + getNextWord();
        } else {
            currentWordId++;
            // Если ответ неправильный, возвращаем сообщение с правильным переводом
            return "Неправильно. Правильный перевод: " + correctTranslation + getNextWord();
        }
    }

    private String getCorrectTranslation() {
        Optional<EnglishWord> currentWordOptional = wordRepository.findFirstById(currentWordId);

        return currentWordOptional.map(EnglishWord::getTranslate)
                .orElse("No translation available.");
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