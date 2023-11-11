package com.bot.telegramweatherbot.utils;

public enum WindDir {

    NW ("Северо-западный"),
    N ("Северный"),
    NE ("Северо-восточный"),
    E ("Восточный"),
    SE ("Юго-восточный"),
    S ("Южный"),
    SW ("Юго-западный"),
    W ("Северный"),
    C ("Штиль");

    private String wind;

    WindDir(String wind) {
        this.wind = wind;
    }

    public String getWind(){
        return wind;
    }
}
