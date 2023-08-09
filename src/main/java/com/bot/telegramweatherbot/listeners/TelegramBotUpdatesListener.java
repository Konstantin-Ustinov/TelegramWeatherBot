package com.bot.telegramweatherbot.listeners;

import com.bot.telegramweatherbot.dto.GeoEncodingDto;
import com.bot.telegramweatherbot.entities.User;
import com.bot.telegramweatherbot.services.TelegramBotService;
import com.bot.telegramweatherbot.services.UserService;
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
    private final TelegramBotService telegramBotService;
    private final UserService userService;

    public TelegramBotUpdatesListener(TelegramBot telegramBot, TelegramBotService telegramBotService, UserService userService) {
        this.telegramBot = telegramBot;
        this.telegramBotService = telegramBotService;
        this.userService = userService;
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
                 if (userService.save(new User(message.chat().firstName(), message.chat().id())) != null) {
                     telegramBot.execute(welcomeMessage(chatId));
                 } else {
                     logger.error("Error wile save newUser");
                     telegramBot.execute(new SendMessage(message.chat().id(),
                 """
                        Не получилось сохранить Вас.
                        Попробуйте снова отправить "/start"
                        """));
                 }
             }

             if (message.location() != null) { // Если пришла локация от кнопки
                 logger.info("Location: {}", message.location());
                 GeoEncodingDto geoEncodingDto = telegramBotService.coordinatesToPlace(message.location().longitude(),
                         message.location().latitude());

                 if (userService.addLocationToUser(message.chat().id(), geoEncodingDto) != null) {
                     telegramBot.execute(new SendMessage(chatId, "Ваша локация: \n" + geoEncodingDto.getPlace()));
                 } else {
                     telegramBot.execute(new SendMessage(message.chat().id(),
                             """
                                    Не получилось сохранить Ваше местоположение.
                                    Попробуйте снова нажать на кнопку.
                                    """));
                 }

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
