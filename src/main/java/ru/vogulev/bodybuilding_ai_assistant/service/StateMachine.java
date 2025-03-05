package ru.vogulev.bodybuilding_ai_assistant.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.vogulev.bodybuilding_ai_assistant.model.User;

import java.util.HashMap;
import java.util.Map;

import static ru.vogulev.bodybuilding_ai_assistant.model.UserState.START;

@Component
@RequiredArgsConstructor
public class StateMachine {
    private final Map<Long, User> users = new HashMap<>();
    private final Assistant assistant;

    public SendMessage eventHandler(Long chatId, String userMessage) {
        var user = users.get(chatId) == null ? new User(START) : users.get(chatId);
        handleUserAnswer(user, userMessage);
        var message = getMessage(chatId, user);
        user.setState(user.getState().nextState());
        users.put(chatId, user);
        return message;
    }

    private void handleUserAnswer(User user, String userMessage) {
        switch (user.getState()) {
            case AGE -> user.setAge(userMessage);
            case WEIGHT -> user.setWeight(userMessage);
            case HEIGHT -> user.setHeight(userMessage);
            case GENDER -> user.setGender(userMessage);
            case PLACE -> user.setPlace(userMessage);
        }
    }

    @NotNull
    private SendMessage getMessage(Long chatId, User user) {
        var state = user.getState();
        var text = state.text();
        if (state.isFinal()) {
            text = assistant.chat("Составь программу тренировок для " + user.getGender()
                    + " возрастом " + user.getAge() + " весом " + user.getWeight()
                    + " ростом " + user.getHeight() + " для тренировок " + user.getPlace());
        }
        var message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
        message.setReplyMarkup(state.replyKeyboard());
        return message;
    }
}