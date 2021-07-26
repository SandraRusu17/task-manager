package com.stefanini.taskmanager.repo;

import com.stefanini.taskmanager.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    void save(User user);

    Optional<User> findByUsername(String username);

    List<User> findAll();

    void update(User user);
}
