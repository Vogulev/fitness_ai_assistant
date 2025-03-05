package ru.vogulev.bodybuilding_ai_assistant.model;

import lombok.Data;

@Data
public class User {
    private String weight;
    private String height;
    private String age;
    private String gender;
    private String place;
    private UserState state;

    public User(UserState state) {
        this.state = state;
    }
}
