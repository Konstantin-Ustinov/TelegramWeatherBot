package com.bot.telegramweatherbot.entities;

import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.Objects;

@Entity
public class UserTg {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private Long chatId;
    private double longitude;
    private double latitude;
    private String city;
    private LocalTime notificationTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public LocalTime getNotificationTime() {
        return notificationTime;
    }

    public void setNotificationTime(LocalTime notificationTime) {
        this.notificationTime = notificationTime;
    }

    @Override
    public String toString() {
        return "UserTg{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", chatId=" + chatId +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", city='" + city + '\'' +
                ", notificationTime=" + notificationTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserTg user = (UserTg) o;
        return Double.compare(user.longitude, longitude) == 0 && Double.compare(user.latitude, latitude) == 0 && id.equals(user.id) && Objects.equals(name, user.name) && chatId.equals(user.chatId) && Objects.equals(city, user.city) && Objects.equals(notificationTime, user.notificationTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, chatId, longitude, latitude, city, notificationTime);
    }
}
