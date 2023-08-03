package com.bot.telegramweatherbot.listeners;

import com.bot.telegramweatherbot.dto.GeoEncodingDto;
import com.bot.telegramweatherbot.services.GeoEncodingService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
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
    private final GeoEncodingService geoEncodingService;

    public TelegramBotUpdatesListener(TelegramBot telegramBot, GeoEncodingService geoEncodingService) {
        this.telegramBot = telegramBot;
        this.geoEncodingService = geoEncodingService;
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
                telegramBot.execute(welcomeMessage(chatId));
             }

             if (message.location() != null) { // Если пришла локация от кнопки
                 logger.info("Location: {}", message.location());
                 String place = geoEncodingService.geoEncodingLocationToPlace(message);

                 telegramBot.execute(new SendMessage(chatId,  place));
             }
            });
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return TelegramBotUpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private SendMessage welcomeMessage(Long chatId) {
        SendMessage message = new SendMessage(chatId, """
                                     Привет, я бот и я присылаю погоду раз в день или чаще.
                                     
                                     А сейчас нажми на кнопку передать локацию,
                                     что бы я понял, где ты сейчас находишься.
                                     
                                     Или можешь выбрать любой город, просто напиши его.
                                    """);
        KeyboardButton geoButton = new KeyboardButton("Отправить свое местоположение").requestLocation(true);
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup(geoButton);
        message.replyMarkup(keyboard);
        return message;
    }
}
