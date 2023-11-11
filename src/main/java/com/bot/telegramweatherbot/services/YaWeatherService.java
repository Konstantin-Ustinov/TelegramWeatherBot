package com.bot.telegramweatherbot.services;

import com.bot.telegramweatherbot.dto.YaWeatherDto;
import com.bot.telegramweatherbot.entities.User;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class YaWeatherService {
    RestTemplate restTemplate = new RestTemplate();

    public YaWeatherDto getWeather(User user) {
        String url = "https://api.weather.yandex.ru/v2/informers?lat=" + user.getLatitude() +
                "&lon=" + user.getLongitude();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Yandex-API-Key", "59cb3553-e894-4cf9-98d2-d46294deddda");

        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<YaWeatherDto> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                YaWeatherDto.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            return null;
        }


    }
}
