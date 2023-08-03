package com.bot.telegramweatherbot.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Address {
    private String municipality;
    private String suburb;
    private String city;
    private String state;
    private String country;

    @Override
    public String toString() {
        return "Address{" +
                "municipality='" + municipality + '\'' +
                ", city='" + city + '\'' +
                ", suburb='" + suburb + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
