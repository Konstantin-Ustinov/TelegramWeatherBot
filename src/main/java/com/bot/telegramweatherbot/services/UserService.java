package com.bot.telegramweatherbot.services;

import com.bot.telegramweatherbot.dto.GeoEncodingDto;
import com.bot.telegramweatherbot.entities.User;
import com.bot.telegramweatherbot.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.regex.Matcher;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(User newUser) {
        return userRepository.save(newUser);
    }

    public User addLocationToUser(Long chatId, GeoEncodingDto geoEncodingDto) {
        User user = userRepository.findByChatId(chatId);
        user.setLatitude(geoEncodingDto.getLatitude());
        user.setLongitude(geoEncodingDto.getLongitude());
        user.setPlace(geoEncodingDto.getPlace());

        return userRepository.save(user);
    }

    public User setNotificationTime(Long chatId, Matcher matcher) {

        LocalTime notificationTime = LocalTime.of(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));

        User user = userRepository.findByChatId(chatId);
        user.setNotificationTime(notificationTime);
        userRepository.save(user);

        return user;
    }

    public List<User> findAllUsersNotificationTimeNow() {
        return userRepository.findAllByNotificationTime(LocalTime.parse(LocalTime.now().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))));
    }
}
