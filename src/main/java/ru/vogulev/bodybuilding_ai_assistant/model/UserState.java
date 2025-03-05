package ru.vogulev.bodybuilding_ai_assistant.model;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;
import java.util.Map;

public enum UserState {

    START {
        @Override
        public UserState nextState() {
            return GENDER;
        }

        @Override
        public String text() {
            return "Привет! Я бот который поможет тебе создать идеальный тренировочный план! Нажимай кнопку -> \"создать тренировочный план\"";
        }

        @Override
        public ReplyKeyboard replyKeyboard() {
            var row = new KeyboardRow(new KeyboardButton("Создать новую программу тренировок"));
            return new ReplyKeyboardMarkup(List.of(row));
        }
    },
    GENDER {
        @Override
        public UserState nextState() {
            return AGE;
        }

        @Override
        public String text() {
            return "Начнем составление тренировочной программы с выбора пола";
        }

        @Override
        public ReplyKeyboard replyKeyboard() {
            var row = new KeyboardRow(
                    new KeyboardButton("Мужчина"),
                    new KeyboardButton("Женщина"));
            return new ReplyKeyboardMarkup(List.of(row));
        }
    },
    AGE {
        @Override
        public UserState nextState() {
            return WEIGHT;
        }

        @Override
        public String text() {
            return "Введите ваш возраст";
        }

        @Override
        public ReplyKeyboard replyKeyboard() {
            return new ReplyKeyboardRemove(true);
        }
    },
    WEIGHT {
        @Override
        public UserState nextState() {
            return HEIGHT;
        }

        @Override
        public String text() {
            return "Введите ваш вес";
        }

        @Override
        public ReplyKeyboard replyKeyboard() {
            return new ReplyKeyboardRemove(true);
        }
    },
    HEIGHT {
        @Override
        public UserState nextState() {
            return PLACE;
        }

        @Override
        public String text() {
            return "Введите ваш рост";
        }

        @Override
        public ReplyKeyboard replyKeyboard() {
            return new ReplyKeyboardRemove(true);
        }
    },
    PLACE {
        @Override
        public UserState nextState() {
            return AI_ANSWER;
        }

        @Override
        public String text() {
            return "Выберите место ваших тренировок";
        }

        @Override
        public ReplyKeyboard replyKeyboard() {
            var row = new KeyboardRow(
                    new KeyboardButton("В тренажерном зале"),
                    new KeyboardButton("Дома"));
            return new ReplyKeyboardMarkup(List.of(row));
        }
    },
    AI_ANSWER {
        @Override
        public UserState nextState() {
            return START;
        }

        @Override
        public String text() {
            return "";
        }

        @Override
        public ReplyKeyboard replyKeyboard() {
            return new ReplyKeyboardRemove(true);
        }

        @Override
        public boolean isFinal() {
            return true;
        }
    };

    public abstract UserState nextState();

    public abstract String text();

    public abstract ReplyKeyboard replyKeyboard();

    public boolean isFinal() {
        return false;
    }
}
