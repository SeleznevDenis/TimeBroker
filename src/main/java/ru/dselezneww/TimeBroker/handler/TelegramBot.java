package ru.dselezneww.TimeBroker.handler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.CopyMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final String name;
    private final TelegramBotsApi telegramBotApi;

    public TelegramBot(
            @Value("${bot.name}")String name,
            @Value("${bot.token}")String token,
            TelegramBotsApi telegramBotsApi) {
        super(token);
        System.out.println(name + token);
        this.name = name;
        this.telegramBotApi = telegramBotsApi;
    }

    @PostConstruct
    public void init() throws TelegramApiException {
        this.telegramBotApi.registerBot(this);
    }

    @Override
    public void onUpdateReceived(Update update) {
        var msg = update.getMessage();
        var user = msg.getFrom();
        var id = user.getId();
        copyMessage(id, msg.getMessageId());
    }

    public void copyMessage(Long who, Integer msgId){
        CopyMessage cm = CopyMessage.builder()
                .fromChatId(who.toString())  //We copy from the user
                .chatId(who.toString())      //And send it back to him
                .messageId(msgId)            //Specifying what message
                .build();
        try {
            execute(cm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return this.name;
    }
}
