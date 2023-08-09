package com.bot.telegramweatherbot.repositories;

import com.bot.telegramweatherbot.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByChatId(Long chatId);
}
