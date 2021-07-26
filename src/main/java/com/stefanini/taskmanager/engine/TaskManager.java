package com.stefanini.taskmanager.engine;

import com.stefanini.taskmanager.model.User;
import com.stefanini.taskmanager.repo.UserFileRepositoryImpl;
import com.stefanini.taskmanager.service.UserService;
import com.stefanini.taskmanager.service.UserServiceImpl;

import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class TaskManager {

    private final UserFileRepositoryImpl userFileRepository = new UserFileRepositoryImpl();
    private final UserService userService = new UserServiceImpl(userFileRepository);

    public void parseCommandLine(String[] commandLine) {
        final String command = commandLine[0];
        switch (command) {
            case "-createUser":
                userService.createUser(
                        getStringValue(commandLine[3]),
                        getStringValue(commandLine[2]),
                        getStringValue(commandLine[1]));
                break;
            case "-showAllUsers":
                System.out.println(formatUsers(userService.getAllUsers()));
                break;
            case "-addTask":
                userService.addTaskFor(
                        getStringValue(commandLine[2]),
                        getStringValue(commandLine[3]),
                        getStringValue(commandLine[1]));
                break;
            case "-showTasks":
                System.out.println(userService.getTasksFor(getStringValue(commandLine[1])));
                break;
            default:
                System.out.println("Unknown command");
        }
    }

    private String formatUsers(final List<User> users) {
        StringJoiner joiner = new StringJoiner(", \n");
        for (User user : users) {
            String formattedDetails = user.getFormattedDetails();
            joiner.add(formattedDetails);
        }
        return joiner.toString();
    }

    private String getStringValue(final String s) {
        return s.substring(s.indexOf('\'') + 1, s.lastIndexOf('\''));
    }
}
