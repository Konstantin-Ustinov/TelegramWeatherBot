package com.bot.telegramweatherbot.services;

import com.bot.telegramweatherbot.dto.GeoEncodingDto;
import com.bot.telegramweatherbot.entities.User;
import com.bot.telegramweatherbot.repositories.UserRepository;
import org.springframework.stereotype.Service;

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
}
