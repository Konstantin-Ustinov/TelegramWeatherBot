package com.bot.telegramweatherbot.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String userName;
    private Long chatId;
    private double longitude;
    private double latitude;
    private String place;
    private LocalTime notificationTime;

    public User(String name, Long chatId) {
        this.userName = name;
        this.chatId = chatId;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", chatId=" + chatId +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", place='" + place + '\'' +
                ", notificationTime=" + notificationTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Double.compare(user.longitude, longitude) == 0 && Double.compare(user.latitude, latitude) == 0 && id.equals(user.id) && Objects.equals(userName, user.userName) && chatId.equals(user.chatId) && Objects.equals(place, user.place) && Objects.equals(notificationTime, user.notificationTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userName, chatId, longitude, latitude, place, notificationTime);
    }
}
