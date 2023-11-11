package com.bot.telegramweatherbot.services;

import com.bot.telegramweatherbot.dto.GeoEncodingDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
