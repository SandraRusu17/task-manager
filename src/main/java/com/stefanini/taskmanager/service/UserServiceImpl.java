package com.stefanini.taskmanager.service;

import com.stefanini.taskmanager.exceptions.UserNotFoundException;
import com.stefanini.taskmanager.model.Task;
import com.stefanini.taskmanager.model.User;
import com.stefanini.taskmanager.repo.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public void createUser(String username, String firstname, String lastname){
        if(!userRepository.findByUsername(username).isPresent()) {
            final User user = new User(username, firstname, lastname);
            userRepository.save(user);
            System.out.println(user.getFormattedDetails() + " created successfully!");
        } else {
            System.out.println("Username " + username + " already exists!");
        }
    }

    @Override
    public void addTaskFor(String taskTitle, String taskDescription, String username) throws UserNotFoundException {
        Task task = new Task(taskTitle, taskDescription);
        User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        user.addTask(task);
        userRepository.update(user);
        System.out.println(task + " added successfully!");
    }

    @Override
    public List<Task> getTasksFor(String username) throws UserNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        return user.getTasks();
    }

    //one more logical feature
    @Override
    public void deleteTaskByTitleFor(String taskTitle, String username) throws UserNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        user.getTasks().removeIf(t -> t.getTitle().equals(taskTitle));
        userRepository.update(user);
        System.out.println(taskTitle + " deleted successfully!");
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
