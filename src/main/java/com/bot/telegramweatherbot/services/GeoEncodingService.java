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

    public String geoEncodingLocationToPlace(Message message) {
        GeoEncodingDto geoEncodingDto = restTemplate.getForObject(String.join("", "https://nominatim.openstreetmap.org/reverse?format=json&lat="
                , message.location().latitude().toString(), "&lon=", message.location().longitude().toString()
                , "&zoom=18&addressdetails=1)"), GeoEncodingDto.class);

        StringBuilder textForSendMessage = new StringBuilder("Ваша локация: \n");

        try {
            assert geoEncodingDto != null;
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
        } catch (AssertionError e) {
            textForSendMessage.append("не известна.");
            e.printStackTrace();
            logger.error("Cant parse json or GeoEncoding service not available now.");
        }

        return textForSendMessage.toString();
    }
}
