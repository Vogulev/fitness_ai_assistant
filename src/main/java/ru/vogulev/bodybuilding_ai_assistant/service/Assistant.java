package ru.vogulev.bodybuilding_ai_assistant.service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface Assistant {

    @SystemMessage("Системное сообщение вызова метода chat")
    String chat(String text);
}
