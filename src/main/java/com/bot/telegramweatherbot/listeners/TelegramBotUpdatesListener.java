package com.bot.telegramweatherbot.listeners;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TelegramBot telegramBot;

    public TelegramBotUpdatesListener(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> list) {
        try {
            list.forEach(update -> {
            logger.info("Check update: {}", update);

             Message message = update.message();
             Long chatId = message.chat().id();
             String text = message.text();

             if ("/start".equals(text)) {
                telegramBot.execute(new SendMessage(chatId, "Привет, я бот и я присылаю погоду раз в день или чаще."));
             }

            });
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return TelegramBotUpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
