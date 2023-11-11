package com.bot.telegramweatherbot.utils;

public enum Condition {

    CLEAR ("Ясно"),
    PARTLY_CLOUDY ("Малооблачно"),
    CLOUDY ("Облачно с прояснениями"),
    OVERCAST ("Пасмурно"),
    LIGHT_RAIN ("Небольшой дождь"),
    RAIN ("Дождь"),
    HEAVY_RAIN ("Сильный дождь"),
    SHOWERS ("Ливень"),
    WET_SNOW ("Дождь со снегом"),
    LIGHT_SNOW ("Небольшой снег"),
    SNOW ("Снег"),
    SNOW_SHOWERS ("Снегопад"),
    HAIL ("Град"),
    THUNDERSTORM ("Гроза"),
    THUNDERSTORM_WITH_RAIN ("Дождь с грозой"),
    THUNDERSTORM_WITH_HAIL ("Гроза с градом");

    private String condition;

    Condition(String condition) {
        this.condition = condition;
    }

    public String getCondition() {
        return condition;
    }
}
