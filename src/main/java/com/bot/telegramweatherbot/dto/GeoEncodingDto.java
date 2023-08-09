package com.bot.telegramweatherbot.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeoEncodingDto {
    private Address address;
    private String place;
    private Float longitude;
    private Float latitude;
}
