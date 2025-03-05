package ru.vogulev.bodybuilding_ai_assistant.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.BotSession;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.AfterBotRegistration;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.vogulev.bodybuilding_ai_assistant.model.User;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Getter
@Component
public class TelegramBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {
    private final TelegramClient telegramClient;
    private final String botToken;
    private final StateMachine stateMachine;
    private final Map<Long, User> userChoiceMap;

    public TelegramBot(@Value("${BOT_TOKEN}") String botToken, @Autowired StateMachine stateMachine) {
        this.botToken = botToken;
        this.stateMachine = stateMachine;
        this.userChoiceMap = new HashMap<>();
        telegramClient = new OkHttpTelegramClient(botToken);
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            var chat_id = update.getMessage().getChatId();
            var message = stateMachine.eventHandler(chat_id, update.getMessage().getText());
            sendMessage(message);
        }
    }

    public void sendMessage(SendMessage message) {
        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    @AfterBotRegistration
    public void afterRegistration(BotSession botSession) {
        log.info("Registered bot running state is: {}", botSession.isRunning());
    }
}
