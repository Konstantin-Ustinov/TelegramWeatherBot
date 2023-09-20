package com.bot.telegramweatherbot.listeners;

import com.bot.telegramweatherbot.dto.GeoEncodingDto;
import com.bot.telegramweatherbot.entities.User;
import com.bot.telegramweatherbot.services.TelegramBotService;
import com.bot.telegramweatherbot.services.UserService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TelegramBot telegramBot;
    private final TelegramBotService telegramBotService;
    private final UserService userService;

    private static final Pattern TIME_PATTERN = Pattern.compile("^([0-1]?[0-9]|2[0-3]):([0-5][0-9])$"); //Шаблон для парсинга времени
    private static final Pattern WITHOUT_LETTER_PATTERN = Pattern.compile("\\d\\d:\\d\\d"); //Шаблон для парсинга времени

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

                if (update.message() != null) {

                    Message message = update.message();
                    Long chatId = message.chat().id();
                    String text = message.text();
                    Matcher withoutLetterMather = WITHOUT_LETTER_PATTERN.matcher(text);

                    // Обрабатываем команду start
                    if ("/start".equals(text)) {
                        if (userService.save(new User(message.chat().firstName(), chatId)) != null) {
                            telegramBot.execute(welcomeMessage(chatId));
                        } else {
                            logger.error("Error wile save newUser");
                            telegramBot.execute(new SendMessage(chatId,
                                    """
                                            Не получилось сохранить Вас.
                                            Попробуйте снова отправить "/start"
                                            """));
                        }
                    }

                    // Обрабатываем нажатие кнопки "отправить локацию"
                    else if (message.location() != null) { // Если пришла локация от кнопки
                        GeoEncodingDto geoEncodingDto = telegramBotService.coordinatesToPlace(message.location().longitude(),
                                message.location().latitude());

                        if (userService.addLocationToUser(chatId, geoEncodingDto) != null) {
                            telegramBot.execute(new SendMessage(chatId, "Твоя локация: \n" + geoEncodingDto.getPlace()
                                    + "\n\nЯ буду присылать погоду каждый день в 08:00 утра. Если хочешь поменять время, нажми на кнопку внизу."));
                        } else {
                            telegramBot.execute(new SendMessage(chatId,
                                    """
                                            Не получилось сохранить Ваше местоположение.
                                            Попробуйте снова нажать на кнопку.
                                            """));
                        }
                    }

                    // Обрабатываем нажатие кнопки смены времени
                    else if ("Сменить время получения погоды".equals(text)) {
                        telegramBot.execute(timeKeyboardMessage(chatId));
                    }

                    // Обрабатываем сообщение похожее на время
                    else if (withoutLetterMather.find()) {
                        telegramBot.execute(setTimeNotification(text, chatId));
                    }
                }

             // Обрабатываем выбор нового времени
              if (Objects.nonNull(update.callbackQuery())) {
                  String text = update.callbackQuery().data();
                  Long callBackChatId = update.callbackQuery().message().chat().id();
                  telegramBot.execute(setTimeNotification(text, callBackChatId));
             }
            });
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return TelegramBotUpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private SendMessage welcomeMessage(Long chatId) {
        SendMessage message = new SendMessage(chatId, """
                                     Привет, я бот и я присылаю погоду раз в день в определенное время, которое ты выберешь.
                                     По умолчанию - это 08:00 по твоему местному времени.
                                     
                                     А сейчас нажми на кнопку передать локацию, что бы я понял, где ты сейчас находишься.                                     
                                    """);
        KeyboardButton geoButton = new KeyboardButton("Отправить свое местоположение").requestLocation(true);
        KeyboardButton timeButton = new KeyboardButton("Сменить время получения погоды");
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup(geoButton, timeButton);

        message.replyMarkup(keyboard);
        return message;
    }

    private SendMessage timeKeyboardMessage(Long chatId) {
        SendMessage message = new SendMessage(chatId, "Выбери удобное тебе время или напиши его в сообщении в формате (15:30)");

        InlineKeyboardButton seven = new InlineKeyboardButton("07:00");
        seven.callbackData("07:00");
        InlineKeyboardButton eight = new InlineKeyboardButton("08:00");
        eight.callbackData("08:00");
        InlineKeyboardButton nine = new InlineKeyboardButton("09:00");
        nine.callbackData("09:00");
        InlineKeyboardButton ten = new InlineKeyboardButton("10:00");
        ten.callbackData("10:00");
        InlineKeyboardButton eleven = new InlineKeyboardButton("11:00");
        eleven.callbackData("11:00");
        InlineKeyboardButton twelve = new InlineKeyboardButton("12:00");
        twelve.callbackData("12:00");

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();

        keyboardMarkup.addRow(seven, eight, nine);
        keyboardMarkup.addRow(ten, eleven, twelve);

        message.replyMarkup(keyboardMarkup);

        return message;
    }

    private SendMessage setTimeNotification(String text, Long chatId) {
        Matcher matcher = TIME_PATTERN.matcher(text);
        String messageText;
        if (matcher.find()) {
            User user = userService.setNotificationTime(chatId, matcher);
            if (Objects.nonNull(user)) {
                messageText = """
                                 Время успешно установлено. 
                                 Погода будет приходить ежедневно в """ + " " + user.getNotificationTime();
            } else {
                logger.error("Error in set new time notification");
                messageText = "Не удалось установить новое время. Попробуйте еще раз.";
            }
        } else  {
            messageText = "Неправильно указано время. Укажите в формате(чч:мм)";
        }
        return new SendMessage(chatId, messageText);
    }
}
