package com.stefanini.taskmanager.service;

import com.stefanini.taskmanager.model.Task;
import com.stefanini.taskmanager.model.User;

import java.util.List;

public interface UserService {
    void createUser(String username, String firstname, String lastname);

    void addTaskFor(String taskTitle, String taskDescription, String username);

    List<Task> getTasksFor(String username);

    List<User> getAllUsers();
}
