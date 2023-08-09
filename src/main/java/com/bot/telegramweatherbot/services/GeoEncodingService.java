package com.bot.telegramweatherbot.services;

import com.bot.telegramweatherbot.dto.Address;
import com.bot.telegramweatherbot.dto.GeoEncodingDto;
import com.bot.telegramweatherbot.exceptions.GeoEncodingException;
import com.pengrad.telegrambot.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class GeoEncodingService {

    Logger logger = LoggerFactory.getLogger(GeoEncodingService.class);

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Get place by coordinates
     * @param longitude
     * @param latitude
     * @return String place
     */
    public GeoEncodingDto geoEncodingLocationToPlace(Float longitude, Float latitude) {
        // Делаем http запрос и сразу маппим его на объект
        return restTemplate.getForObject(String.join("", "https://nominatim.openstreetmap.org/reverse?format=json&lat="
                , latitude.toString(), "&lon=", longitude.toString()
                , "&zoom=18&addressdetails=1)"), GeoEncodingDto.class);
    }
}
