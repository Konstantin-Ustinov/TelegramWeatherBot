package com.bot.telegramweatherbot.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class Weather {
    private Long chatId;
    private String weatherText;

    public Weather(Long chatId, String weatherText) {
        this.chatId = chatId;
        this.weatherText = weatherText;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "chatId=" + chatId +
                ", weatherText='" + weatherText + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Weather weather = (Weather) o;
        return chatId.equals(weather.chatId) && weatherText.equals(weather.weatherText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId, weatherText);
    }
}
