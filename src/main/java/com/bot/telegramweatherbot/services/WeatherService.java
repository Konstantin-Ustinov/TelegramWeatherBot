package com.bot.telegramweatherbot.services;

import com.bot.telegramweatherbot.dto.YaWeatherDto;
import com.bot.telegramweatherbot.entities.User;
import com.bot.telegramweatherbot.entities.Weather;
import org.springframework.stereotype.Service;
import com.bot.telegramweatherbot.utils.WindDir;
import com.bot.telegramweatherbot.utils.Condition;

import java.util.Locale;

@Service
public class WeatherService {
    private final YaWeatherService getWeatherService;

    public WeatherService(YaWeatherService getWeatherService) {
        this.getWeatherService = getWeatherService;
    }

    public Weather getWeather(User user) {
        YaWeatherDto weatherDto = getWeatherService.getWeather(user);
        String text = "Сервис погоды в данный момент не доступен";

        if (weatherDto != null) {
            StringBuilder weatherText = new StringBuilder("Погода на сегодня:\n");

            String condition = weatherDto.getForecast().parts[0].condition.toUpperCase(Locale.ROOT).replace("-", "_");

            weatherText.append(Condition.valueOf(condition).getCondition());
            weatherText.append("\n");
            weatherText.append("Температура воздуха: ");

            int temp = weatherDto.getForecast().parts[0].temp_avg;

            if (temp > 0) {
                weatherText.append("+");
            } else if (temp < 0 ){
                weatherText.append("-");
            }

            weatherText.append(temp);
            weatherText.append(" ºС\n");
            weatherText.append("Ветер: ");
            weatherText.append(WindDir.valueOf(weatherDto.getForecast().parts[0].wind_dir.toUpperCase(Locale.ROOT)).getWind());
            weatherText.append("\n");
            weatherText.append("Скорость ветра: ");
            weatherText.append(weatherDto.getForecast().parts[0].wind_speed);
            weatherText.append(" м/с");

            text = weatherText.toString();
        }

        return new Weather(user.getChatId(), text);
    }
}
