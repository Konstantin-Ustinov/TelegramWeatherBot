package com.bot.telegramweatherbot.services;

import com.bot.telegramweatherbot.dto.Address;
import com.bot.telegramweatherbot.dto.GeoEncodingDto;
import com.bot.telegramweatherbot.entities.User;
import com.bot.telegramweatherbot.entities.Weather;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TelegramBotService {

    private Logger logger = LoggerFactory.getLogger(TelegramBotService.class);

    private final UserService userService;
    private final GeoEncodingService geoEncodingService;
    private final YaWeatherService yaWeatherService;
    private final WeatherService weatherService;

    public TelegramBotService(UserService userService, GeoEncodingService geoEncodingService, YaWeatherService yaWeatherService, WeatherService weatherService) {
        this.userService = userService;
        this.geoEncodingService = geoEncodingService;
        this.yaWeatherService = yaWeatherService;
        this.weatherService = weatherService;
    }

    public GeoEncodingDto coordinatesToPlace(Float longitude, Float latitude) {
        GeoEncodingDto geoEncodingDto;
        geoEncodingDto = geoEncodingService.geoEncodingLocationToPlace(longitude, latitude);

        StringBuilder textForSendMessage = new StringBuilder();

        try {
            assert geoEncodingDto.getAddress() != null; // Утверждаем, что объект не null иначе исключение, которое перехватываем.
            Address address = geoEncodingDto.getAddress();

            textForSendMessage.append(address.getCountry());
            textForSendMessage.append(", ");
            textForSendMessage.append(address.getState());

            if (Objects.nonNull(address.getCity())) {
                textForSendMessage.append(", ");
                textForSendMessage.append(address.getCity());
            }
            if (Objects.nonNull(address.getSuburb())) {
                textForSendMessage.append(", ");
                textForSendMessage.append(address.getSuburb());
            }
            if (Objects.nonNull(address.getMunicipality())) {
                textForSendMessage.append(", ");
                textForSendMessage.append(address.getMunicipality());
            }

            geoEncodingDto.setPlace(textForSendMessage.toString());
            geoEncodingDto.setLongitude(longitude);
            geoEncodingDto.setLatitude(latitude);
        } catch (AssertionError e) {
            textForSendMessage.append("не известна. Похоже не поладки сервиса. Попробуйте снова.");
            e.printStackTrace();
            logger.error("Cant parse json or GeoEncoding service not available now.");
        }
        return geoEncodingDto;
    }

    public Set<Weather> findAllWeatherUsersNotificationTimeNow() {
        List<User> users = userService.findAllUsersNotificationTimeNow();
        Set<Weather> weatherList = new HashSet<>();
        users.forEach((user) -> {
            weatherList.add(weatherService.getWeather(user));
            logger.info(yaWeatherService.getWeather(user).toString());
        });
        return weatherList;
    }
}
