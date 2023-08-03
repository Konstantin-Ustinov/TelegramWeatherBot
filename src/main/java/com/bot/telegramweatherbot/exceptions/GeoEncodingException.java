package com.bot.telegramweatherbot.exceptions;

public class GeoEncodingException extends RuntimeException {

    GeoEncodingException(String e) {
        super(e);
    }
    GeoEncodingException() {
        super("Cant parse json or GeoEncoding service not available now.");
    }
}
